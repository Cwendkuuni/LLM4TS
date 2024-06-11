import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.auth.*;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBase;

    @Before
    public void setup() {
        Configuration conf = new ConfigurationBuilder()
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret")
                .build();
        Authorization auth = new OAuthAuthorization(conf);
        twitterBase = new TwitterBaseImpl(conf, auth);
    }

    @Test
    public void testGetScreenName_WithValidCredentials_ReturnsScreenName() throws TwitterException, IllegalStateException {
        String expectedScreenName = "testScreenName";
        twitterBase.screenName = expectedScreenName;

        String actualScreenName = twitterBase.getScreenName();

        assertEquals(expectedScreenName, actualScreenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_WithNoAuthorization_ThrowsIllegalStateException() throws TwitterException, IllegalStateException {
        twitterBase.auth = NullAuthorization.getInstance();

        twitterBase.getScreenName();
    }

    @Test
    public void testGetId_WithValidCredentials_ReturnsId() throws TwitterException, IllegalStateException {
        long expectedId = 12345L;
        twitterBase.id = expectedId;

        long actualId = twitterBase.getId();

        assertEquals(expectedId, actualId);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetId_WithNoAuthorization_ThrowsIllegalStateException() throws TwitterException, IllegalStateException {
        twitterBase.auth = NullAuthorization.getInstance();

        twitterBase.getId();
    }

    // ... More test cases for other methods ...

}