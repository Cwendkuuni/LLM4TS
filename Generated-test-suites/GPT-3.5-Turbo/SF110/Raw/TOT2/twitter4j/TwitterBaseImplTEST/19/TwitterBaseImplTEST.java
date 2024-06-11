import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.*;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Authorization;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName_validAuth() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().setOAuthConsumerKey("key").setOAuthConsumerSecret("secret").build();
        Authorization auth = new OAuth2Authorization(conf);
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        assertEquals("Expected screen name is returned", "testUser", twitterBase.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_invalidAuth() throws TwitterException, IllegalStateException {
        Configuration conf = new ConfigurationBuilder().build();
        Authorization auth = new OAuth2Authorization(conf);
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth);
        twitterBase.getScreenName();
    }

    // Add more test cases for other methods as needed

}