package twitter4j;

import org.junit.Before;
import org.junit.Test;
import twitter4j.auth.Authorization;
import twitter4j.auth.NullAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.Consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBaseImpl;

    @Before
    public void setUp() throws Exception {
        Configuration conf = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();

        Authorization auth = new NullAuthorization();

        twitterBaseImpl = new TwitterBaseImpl(conf, auth) {
            @Override
            public String getScreenName() throws TwitterException, IllegalStateException {
                return "";
            }

            @Override
            public long getId() throws TwitterException, IllegalStateException {
                return 0;
            }

            @Override
            public void addRateLimitStatusListener(RateLimitStatusListener listener) {

            }

            @Override
            public void onRateLimitStatus(Consumer<RateLimitStatusEvent> action) {

            }

            @Override
            public void onRateLimitReached(Consumer<RateLimitStatusEvent> action) {

            }

            @Override
            public void httpResponseReceived(HttpResponseEvent event) {

            }

            @Override
            public final Authorization getAuthorization() {
                return null;
            }

            @Override
            public Configuration getConfiguration() {
                return null;
            }
        };
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        String screenName = twitterBaseImpl.getScreenName();
        assertEquals("", screenName);
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        long id = twitterBaseImpl.getId();
        assertEquals(0, id);
    }

    // Add more test methods for other methods in TwitterBaseImpl...

}