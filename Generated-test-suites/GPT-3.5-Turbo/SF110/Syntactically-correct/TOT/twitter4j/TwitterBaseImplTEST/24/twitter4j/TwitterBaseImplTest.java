import org.junit.Assert;
import org.junit.Test;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        // Typical use case
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth) {};
        String screenName = twitterBaseImpl.getScreenName();
        
        Assert.assertNotNull(screenName);
        
        // Edge case when auth is not enabled
        auth.setEnabled(false);
        try {
            screenName = twitterBaseImpl.getScreenName();
            Assert.fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception expected, do nothing
        }
        
        // Edge case when BasicAuthorization is used and screenName contains "@"
        auth = new BasicAuthorization("userId", "password");
        twitterBaseImpl = new TwitterBaseImpl(conf, auth) {};
        screenName = twitterBaseImpl.getScreenName();

        Assert.assertNull(screenName);
        
        // Error scenario when screenName is null after filling in ID and screen name
        // (should never happen with valid configuration)
        auth = new OAuthAuthorization(conf);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth) {};
        twitterBaseImpl.setFactory(null);
        screenName = twitterBaseImpl.getScreenName();

        Assert.assertNull(screenName);
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        // Typical use case
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitterBaseImpl = new TwitterBaseImpl(conf, auth) {};
        long id = twitterBaseImpl.getId();
        
        Assert.assertEquals(0L, id);
        
        // Edge case when auth is not enabled
        auth.setEnabled(false);
        try {
            id = twitterBaseImpl.getId();
            Assert.fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception expected, do nothing
        }
        
        // Error scenario when id is 0 after filling in ID and screen name
        // (should never happen with valid configuration)
        auth = new OAuthAuthorization(conf);
        twitterBaseImpl = new TwitterBaseImpl(conf, auth) {};
        twitterBaseImpl.setFactory(null);
        id = twitterBaseImpl.getId();

        Assert.assertEquals(0L, id);
    }

    // TODO: Add test cases for remaining methods

}