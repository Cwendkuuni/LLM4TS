import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

    private Configuration confMock;
    private Authorization authMock;
    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        confMock = mock(Configuration.class);
        authMock = mock(Authorization.class);
        twitterBase = new TwitterBaseImpl(confMock, authMock) {
            @Override
            void setFactory() {
                // do nothing for testing purposes
            }
        };
    }

    @Test
    public void testGetScreenName_WhenAuthIsNotEnabled_ThrowsIllegalStateException() throws TwitterException {
        when(authMock.isEnabled()).thenReturn(false);

        try {
            twitterBase.getScreenName();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Neither user ID/password combination nor OAuth consumer key/secret combination supplied", e.getMessage());
        }
    }

    @Test
    public void testGetScreenName_WhenScreenNameIsNullAndAuthIsBasicAuthorization_FillsInIDAndScreenName() throws TwitterException {
        when(authMock.isEnabled()).thenReturn(true);
        when(authMock instanceof BasicAuthorization).thenReturn(true);
        when(((BasicAuthorization) authMock).getUserId()).thenReturn("testUserId");

        String screenName = twitterBase.getScreenName();

        assertEquals("testUserId", screenName);
        verify(authMock, times(1)).isEnabled();
        verify(authMock, times(1)).getUserId();
        verify(authMock, times(0)).getOAuthConsumerKey();
    }

    @Test
    public void testGetScreenName_WhenScreenNameIsNullAndAuthIsOAuthAuthorization_CallsFillInIDAndScreenName() throws TwitterException {
        when(authMock.isEnabled()).thenReturn(true);
        when(authMock instanceof BasicAuthorization).thenReturn(false);

        twitterBase.getScreenName();

        verify(authMock, times(1)).isEnabled();
        verify(twitterBase, times(1)).fillInIDAndScreenName();
    }

    // more test methods for other methods in the TwitterBaseImpl class

}