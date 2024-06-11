import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    private static final String consumerKey = "your_consumer_key";
    private static final String consumerSecret = "your_consumer_secret";
    private static final String accessToken = "your_access_token";
    private static final String accessTokenSecret = "your_access_token_secret";

    private TwitterBaseImpl twitter;

    @Before
    public void setUp() {
        Configuration conf = new ConfigurationBuilder()
            .setDebugEnabled(true)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret)
            .build();

        Authorization auth = AuthorizationFactory.getInstance(conf);

        twitter = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        assertEquals("screenName", twitter.getScreenName());
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        assertEquals(12345L, twitter.getId());
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // implementation
            }
            
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // implementation
            }
        };

        twitter.addRateLimitStatusListener(listener);
        assertTrue(twitter.getRateLimitStatusListeners().contains(listener));
    }

    @Test
    public void testOnRateLimitStatus() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // implementation
            }
            
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // implementation
            }
        };

        twitter.onRateLimitStatus(listener::onRateLimitStatus);
        assertTrue(twitter.getRateLimitStatusListeners().contains(listener));
    }

    @Test
    public void testOnRateLimitReached() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // implementation
            }
            
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // implementation
            }
        };

        twitter.onRateLimitReached(listener::onRateLimitReached);
        assertTrue(twitter.getRateLimitStatusListeners().contains(listener));
    }
}