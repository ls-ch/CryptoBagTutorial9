package au.edu.unsw.infs3634.cryptobag;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.infs3634.cryptobag.Entities.Coin;
import au.edu.unsw.infs3634.cryptobag.Entities.CoinLoreResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private CoinAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
        }

        RecyclerView mRecyclerView = findViewById(R.id.rvList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CoinAdapter(this, new ArrayList<Coin>(), mTwoPane);
        mRecyclerView.setAdapter(mAdapter);

        new MyTask().execute();
        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coinlore.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CoinService service = retrofit.create(CoinService.class);
        Call<CoinLoreResponse> coinsCall = service.getCoins();
        coinsCall.enqueue(new Callback<CoinLoreResponse>() {
            @Override
            public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
                List<Coin> coins = response.body().getData();
                mAdapter.setCoins(coins);
            }

            @Override
            public void onFailure(Call<CoinLoreResponse> call, Throwable t) {

            }
        });
        */
    }

    public class MyTask extends AsyncTask<Void, Void, List<Coin>> {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coinlore.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CoinService service = retrofit.create(CoinService.class);

        @Override
        protected List<Coin> doInBackground(Void... voids) {
            List<Coin> mList;
            Call<CoinLoreResponse> coinsCall = service.getCoins();
            try {
                Response<CoinLoreResponse> response = coinsCall.execute();
                mList = response.body().getData();
                return mList;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Coin> mCoins) {
            super.onPostExecute(mCoins);
            mAdapter.setCoins(mCoins);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
