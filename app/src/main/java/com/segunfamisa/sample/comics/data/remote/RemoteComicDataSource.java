package com.segunfamisa.sample.comics.data.remote;


import com.segunfamisa.sample.comics.data.ComicDataSource;
import com.segunfamisa.sample.comics.data.model.Comic;
import com.segunfamisa.sample.comics.util.HashCalculator;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Remote data source for comics.
 */
public class RemoteComicDataSource implements ComicDataSource {

    private final ApiService apiService;
    private final HashCalculator hashCalculator;
    private final TimeStampProvider timeStampProvider;

    /**
     * Create new Remote Comic Data Source.
     *
     * @param apiService - api service
     * @param hashCalculator - hash calculator
     * @param timeStampProvider - timestamp provider
     */
    public RemoteComicDataSource(ApiService apiService, HashCalculator hashCalculator,
                                 TimeStampProvider timeStampProvider) {
        this.apiService = apiService;
        this.hashCalculator = hashCalculator;
        this.timeStampProvider = timeStampProvider;
    }

    @Override
    public Observable<List<Comic>> getComics() {
        final String timeStamp = timeStampProvider.getTimeStamp();

        return apiService.getComics(ApiService.DEFAULT_LIMIT, timeStamp,
                ApiService.PUBLIC_KEY,
                calculateHash(timeStamp, ApiService.PUBLIC_KEY, ApiService.PRIVATE_KEY))
                .map(new Function<ComicDataResponse, List<Comic>>() {
                    @Override
                    public List<Comic> apply(ComicDataResponse comicDataResponse) throws Exception {
                        return comicDataResponse.getData().getResults();
                    }
                });
    }

    @Override
    public Observable<Comic> getComic(long comicId) {
        return null;
    }

    private String calculateHash(String timeStamp, String publicKey, String privateKey) {
        return hashCalculator.calculate(timeStamp, publicKey, privateKey);
    }
}
