package com.example.rishabh.github;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etUser = findViewById(R.id.etInput);
        Button btn = findViewById(R.id.btnGo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://api.github.com/search/users?q="+etUser.getText().toString();
//                Mygithub mygithub = new Mygithub();
//                mygithub.execute("https://api.github.com/search/users?q="+etUser.getText().toString());
                makeNetworkCall(url);


            }
        });
    }

    private void makeNetworkCall(String url) {

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "Can't process your Request now!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string();
                final Gson gson = new Gson();
                final github_users gu = gson.fromJson(result, github_users.class);

                (MainActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Items> items = gu.getItems();

                        ListAdaptor la = new ListAdaptor(items,MainActivity.this);
                        RecyclerView rvList = findViewById(R.id.rvList);

                        rvList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        rvList.setAdapter(la);

                    }
                });

            }
        });

    }

    void addIntent(Items items)
    {
        Intent i = new Intent(getBaseContext(),SecondActivity.class);
        i.putExtra("url",items.getUrl());
        startActivity(i);
    }








//    private class Mygithub extends AsyncTask<String,Void,String> {
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            github_users gh_users = convertJSON(s);
//            ArrayList<Items> items = gh_users.getItems();
//
//            ListAdaptor la = new ListAdaptor(items,MainActivity.this);
//            RecyclerView rvList = findViewById(R.id.rvList);
//
//            rvList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//            rvList.setAdapter(la);
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            String string = strings[0];
//            Log.e("TAG", string);
//
//            try {
//
//                URL url = new URL(string);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                Scanner scanner = new Scanner(inputStream);
//                scanner.useDelimiter("\\A");
//                String result = "";
//
//                if(scanner.hasNext())
//                {
//                    result = scanner.next();
//                    Log.e("TAG", "doInBackground: ");
//                }
//                return result;
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return "";
//        }
//    }

//    private github_users convertJSON(String s) {
//
//        try {
//            JSONObject jsonObject = new JSONObject(s);
//            JSONArray jsonArray = jsonObject.getJSONArray("items");
//            ArrayList<Items> items = new ArrayList<>();
//
//            for(int i = 0; i < jsonArray.length(); i++) {
//                JSONObject itemObject = jsonArray.getJSONObject(i);
//
//                String login = itemObject.getString("login");
//                String avatar_url = itemObject.getString("avatar_url");
//                String url = itemObject.getString("url");
//
//                items.add(new Items(login, avatar_url, url));
//            }
//
//            return new github_users(items);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}