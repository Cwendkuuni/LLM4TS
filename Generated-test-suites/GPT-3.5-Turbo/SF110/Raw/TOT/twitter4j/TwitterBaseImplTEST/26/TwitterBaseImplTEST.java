import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName_success() throws TwitterException, IllegalStateException {
        Configuration configuration = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization authorization = new OAuthAuthorization(configuration);
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(configuration, authorization);

        assertEquals("Expected screen name is not returned", "test_screen_name", twitterBase.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_noAuth() throws TwitterException, IllegalStateException {
        Configuration configuration = new ConfigurationBuilder()
                .build();
        Authorization authorization = NullAuthorization.getInstance();
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(configuration, authorization);

        twitterBase.getScreenName();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_invalidAuth() throws TwitterException, IllegalStateException {
        Configuration configuration = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization authorization = new NullAuthorization();
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(configuration, authorization);

        twitterBase.getScreenName();
    }

    @Test
    public void testGetId_success() throws TwitterException, IllegalStateException {
        Configuration configuration = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization authorization = new OAuthAuthorization(configuration);
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(configuration, authorization);

        assertEquals("Expected id is not returned", 1234567890L, twitterBase.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetId_noAuth() throws TwitterException, IllegalStateException {
        Configuration configuration = new ConfigurationBuilder()
                .build();
        Authorization authorization = NullAuthorization.getInstance();
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(configuration, authorization);

        twitterBase.getId();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetId_invalidAuth() throws TwitterException, IllegalStateException {
        Configuration configuration = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .build();
        Authorization authorization = new NullAuthorization();
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(configuration, authorization);

        twitterBase.getId();
    }

    // Add more test cases for the remaining methods

}