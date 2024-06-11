import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        Configuration conf = mock(Configuration.class);
        Authorization auth = mock(Authorization.class);
        twitterBase = new TwitterBaseImpl(conf, auth) {
            @Override
            User fillInIDAndScreenName(HttpParameter[] parameters) throws TwitterException {
                return new UserJSONImpl(http.get(conf.getRestBaseURL() + "account/verify_credentials.json", parameters, auth, this));
            }
        };
    }

    @Test
    public void testGetScreenName() throws TwitterException {
        // Test when authorization is not enabled
        try {
            twitterBase.getScreenName();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }

        // TODO: Add more test cases for scenarios when screenName is null
    }

    @Test
    public void testGetId() throws TwitterException {
        // Test when authorization is not enabled
        try {
            twitterBase.getId();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }

        // TODO: Add more test cases for scenarios when ID is 0
    }

    // Add more test methods for each public method in TwitterBaseImpl

}