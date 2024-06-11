package twitter4j;

import org.junit.Before;
import org.junit.Test;
import twitter4j.auth.Authorization;
import twitter4j.conf.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TwitterBaseImplTest {
    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        Configuration conf = new Configuration();
        Authorization auth = new NullAuthorization();
        twitterBase = new TwitterBaseImpl(conf, auth) {};
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        assertNull(twitterBase.getScreenName());
        twitterBase.auth = new BasicAuthorization("user", "password");
        assertNull(twitterBase.getScreenName());
        twitterBase.auth = new OAuthAuthorization(twitterBase.conf);
        assertNotNull(twitterBase.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        assertEquals(0L, twitterBase.getId());
        twitterBase.auth = new BasicAuthorization("user", "password");
        assertEquals(0L, twitterBase.getId());
        twitterBase.auth = new OAuthAuthorization(twitterBase.conf);
        assertEquals(0L, twitterBase.getId());
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        User user = twitterBase.fillInIDAndScreenName();
        assertNull(user);
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // test listener
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // test listener
            }
        };
        twitterBase.addRateLimitStatusListener(listener);
    }

    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
            @Override
            public void accept(RateLimitStatusEvent rateLimitStatusEvent) {
                // test consumer
            }
        };
        twitterBase.onRateLimitStatus(action);
    }

    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
            @Override
            public void accept(RateLimitStatusEvent rateLimitStatusEvent) {
                // test consumer
            }
        };
        twitterBase.onRateLimitReached(action);
    }
}