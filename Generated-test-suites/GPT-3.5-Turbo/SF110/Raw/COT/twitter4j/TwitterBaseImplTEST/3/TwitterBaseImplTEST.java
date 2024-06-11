import org.junit.*;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        // Test case with enabled auth and screenName already set
        Configuration conf = new ConfigurationBuilder().setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {};
        
        try {
            String screenName = twitter.getScreenName();
            assertEquals("Screen name should match", "testuser", screenName);
        } catch (TwitterException | IllegalStateException e) {
            fail("An exception should not be thrown");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenNameNoAuth() throws TwitterException, IllegalStateException {
        // Test case with no enabled auth
        Configuration conf = new ConfigurationBuilder().setDebugEnabled(true).build();
        Authorization auth = NullAuthorization.getInstance();
        TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth) {};
        
        twitter.getScreenName(); // Should throw an IllegalStateException
    }
  
    // Add more test cases for other methods in the class
  
}