package com.example.offlinemusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Array;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class MainActivity extends AppCompatActivity {
    public String url;

    ArrayList<String> songTitlesArray = new ArrayList<String>();
    ArrayList<String> songAuthorsArray = new ArrayList<String>();
    ArrayList<String> downloadsArray = new ArrayList<String>();
    ArrayList<String> durationsArray = new ArrayList<String>();

    class FetchWebsiteData extends AsyncTask<Void, Void, Void> {
        String websiteTitle;
        Elements songTitles, songAuthors, durations, downloads;
        @Override
        protected void onPreExecute() {
            //progress dialog
            Toast.makeText(getApplication().getBaseContext(), "PROCESSING", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to website
                Document document = Jsoup.connect(url).get();
                // Get the html document title
                websiteTitle = document.title();
                songTitles   = document.select("div.audio-list-entry-inner div.track-name-container div.track div.title a");
                songAuthors  = document.select("div.audio-list-entry-inner div.track-name-container div.track div.title:nth-child(2)");
                durations    = document.select("div.audio-duration");
                downloads    = document.select("div.download-container a");

//                for (Element duration : durations) {
//                    Toast.makeText(getApplication().getBaseContext(), duration.text(), Toast.LENGTH_LONG).show();
//                    break;
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplication().getBaseContext(), (CharSequence) songTitles.get(0).text(), Toast.LENGTH_LONG).show();

            int counter = 0;
            for (Element duration : durations) {
//////                Toast.makeText(getApplication().getBaseContext(), duration.text(), Toast.LENGTH_LONG).show();
//////                break;
                songAuthorsArray.add(songAuthors.get(counter).text());
                songTitlesArray.add(songTitles.get(counter).text());
                downloadsArray.add(downloads.get(counter).attr("href"));
                durationsArray.add(duration.text());
//                Log.i("counter", Integer.toString(counter));
//                Log.i("counter: ", downloads.get(counter).attr("href"));
                counter++;
            }

//            Log.i("tag:", songAuthorsArray.get(1));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_online);


        final SearchView musicSearchView = (SearchView) findViewById(R.id.searchView);

        musicSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Query:", query);
                try {
                    url = "https://mp3-tut.net/search?query=" + URLEncoder.encode(query, "UTF-8");
                    Toast.makeText(getApplication().getBaseContext(), url, Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                new FetchWebsiteData().execute();
//                LinearLayout songsLinearLayout = (LinearLayout) findViewById(R.id.songsLinerLayout);
//                TextView dynamicTextView = new TextView(getApplicationContext());
//                dynamicTextView.setText(songAuthorsArray.get(0));
////
//                songsLinearLayout.addView(dynamicTextView);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//        new FetchWebsiteData().execute();

    }

}
