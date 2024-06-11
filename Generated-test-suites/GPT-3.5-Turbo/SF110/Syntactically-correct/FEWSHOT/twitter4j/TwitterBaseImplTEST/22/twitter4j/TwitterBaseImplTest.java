package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterBaseImplTest {
    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();
        
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {};
        
        assertEquals("screenName", twitter.getScreenName());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameNoAuth() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().build();
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, null) {};
        twitter.getScreenName();
    }
    
    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();
        
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {};
        
        assertEquals(1234L, twitter.getId());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetIdNoAuth() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().build();
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, null) {};
        twitter.getId();
    }
    
    @Test
    public void testAddRateLimitStatusListener() {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {};
        
        RateLimitStatusListener listener = new RateLimitStatusListener() {
            public void onRateLimitStatus(RateLimitStatusEvent event) {}
            public void onRateLimitReached(RateLimitStatusEvent event) {}
        };
        
        twitter.addRateLimitStatusListener(listener);
        assertEquals(1, twitter.rateLimitStatusListeners.size());
    }
}