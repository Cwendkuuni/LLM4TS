package twitter4j;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();

        Authorization auth = new OAuthAuthorization(conf);

        twitterBase = new TwitterBaseImpl(conf, auth) {
        };
    }

    @Test
    public void testGetScreenName() throws TwitterException {
        String screenName = twitterBase.getScreenName();
        assertNotNull(screenName);
        assertEquals("testScreenName", screenName);
    }

    @Test
    public void testGetId() throws TwitterException {
        long id = twitterBase.getId();
        assertEquals(1234567890L, id);
    }

    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {
            }

            public void onRateLimitReached(RateLimitStatusEvent event) {
            }
        };

        twitterBase.addRateLimitStatusListener(listener);

        assertTrue(twitterBase.rateLimitStatusListeners.contains(listener));
    }

}