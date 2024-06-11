import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.*;
import twitter4j.auth.Authorization;
import twitter4j.auth.NullAuthorization;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

    @Mock
    private Configuration configuration;

    @Mock
    private Authorization authorization;

    @Mock
    private HttpClient httpClient;

    @Mock
    private JSONImplFactory factory;

    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(configuration.getOAuthConsumerKey()).thenReturn("consumerKey");
        when(configuration.getOAuthConsumerSecret()).thenReturn("consumerSecret");
        when(configuration.isApplicationOnlyAuthEnabled()).thenReturn(false);
        twitterBase = new TwitterBaseImpl(configuration, authorization) {
            @Override
            void setFactory() {
                // Do nothing, mock implementation
            }
        };
        twitterBase.http = httpClient;
        twitterBase.factory = factory;
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        twitterBase.screenName = "testScreenName";
        assertEquals("testScreenName", twitterBase.getScreenName());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_notEnabled() throws TwitterException, IllegalStateException {
        when(authorization.isEnabled()).thenReturn(false);
        twitterBase.getScreenName();
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        twitterBase.id = 123;
        assertEquals(123, twitterBase.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetId_notEnabled() throws TwitterException, IllegalStateException {
        when(authorization.isEnabled()).thenReturn(false);
        twitterBase.getId();
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        HttpParameter[] parameters = new HttpParameter[0];
        User user = new UserJSONImpl(null, configuration);
        when(httpClient.get(anyString(), any(HttpParameter[].class), any(Authorization.class), any(HttpResponseListener.class))).thenReturn(null);
        when(factory.createUser(any())).thenReturn(user);

        User result = twitterBase.fillInIDAndScreenName(parameters);

        assertEquals(user, result);
        assertEquals(user.getScreenName(), twitterBase.screenName);
        assertEquals(user.getId(), twitterBase.id);
    }

    @Test
    public void testFillInIDAndScreenName_nullParameters() throws TwitterException {
        User user = new UserJSONImpl(null, configuration);
        when(httpClient.get(anyString(), isNull(HttpParameter[].class), any(Authorization.class), any(HttpResponseListener.class))).thenReturn(null);
        when(factory.createUser(any())).thenReturn(user);

        User result = twitterBase.fillInIDAndScreenName();

        assertEquals(user, result);
        assertEquals(user.getScreenName(), twitterBase.screenName);
        assertEquals(user.getId(), twitterBase.id);
    }

    // Add tests for other methods in the class...

}