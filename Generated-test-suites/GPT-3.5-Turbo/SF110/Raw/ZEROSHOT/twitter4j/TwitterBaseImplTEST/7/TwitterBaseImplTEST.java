import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter4j.auth.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    
    private TwitterBaseImpl twitter;
    
    @Before
    public void setUp() {
        ConfigurationBuilder confBuilder = new ConfigurationBuilder();
        confBuilder.setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret");
        
        Configuration conf = confBuilder.build();
        Authorization auth = new OAuthAuthorization(conf);
        
        twitter = new TwitterBaseImpl(conf, auth) {};
    }

    @After
    public void tearDown() {
        twitter = null;
    }
    
    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        String screenName = twitter.getScreenName();
        
        assertNotNull(screenName);
        assertNotEquals("", screenName);
    }
    
    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        long id = twitter.getId();
        
        assertNotEquals(0L, id);
    }
    
    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        User user = twitter.fillInIDAndScreenName();
        
        assertNotNull(user);
        assertNotNull(user.getScreenName());
        assertNotEquals("", user.getScreenName());
        assertNotEquals(0L, user.getId());
    }
    
    @Test
    public void testAddRateLimitStatusListener() {
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                // do nothing
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                // do nothing
            }
        };
        
        twitter.addRateLimitStatusListener(listener);
        
        int expectedSize = twitter.rateLimitStatusListeners.size() + 1;
        assertEquals(expectedSize, twitter.rateLimitStatusListeners.size());
    }
    
    @Test
    public void testOnRateLimitStatus() {
        Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
            @Override
            public void accept(RateLimitStatusEvent event) {
                // do nothing
            }
        };
        
        twitter.onRateLimitStatus(action);
        
        int expectedSize = twitter.rateLimitStatusListeners.size() + 1;
        assertEquals(expectedSize, twitter.rateLimitStatusListeners.size());
    }
    
    @Test
    public void testOnRateLimitReached() {
        Consumer<RateLimitStatusEvent> action = new Consumer<RateLimitStatusEvent>() {
            @Override
            public void accept(RateLimitStatusEvent event) {
                // do nothing
            }
        };
        
        twitter.onRateLimitReached(action);
        
        int expectedSize = twitter.rateLimitStatusListeners.size() + 1;
        assertEquals(expectedSize, twitter.rateLimitStatusListeners.size());
    }
    
    @Test
    public void testHttpResponseReceived() {
        HttpResponseEvent event = new HttpResponseEvent(null, null);
        twitter.httpResponseReceived(event);
        
        // Assert that the method does not throw an exception
    }
}