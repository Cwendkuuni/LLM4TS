import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.auth.Authorization;
import twitter4j.auth.NullAuthorization;
import twitter4j.conf.Configuration;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterBaseImplTest {

    @Mock
    Configuration mockConfig;

    @Mock
    Authorization mockAuth;

    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        twitterBase = new TwitterBaseImpl(mockConfig, mockAuth);
    }

    @Test
    public void testGetScreenName_WhenAuthIsEnabled_ReturnsScreenName() throws TwitterException, IllegalStateException {
        // Mocking
        BasicAuthorization mockBasicAuth = mock(BasicAuthorization.class);
        when(mockAuth.isEnabled()).thenReturn(true);
        when(mockAuth instanceof BasicAuthorization).thenReturn(true);
        when(mockAuth instanceof OAuthAuthorization).thenReturn(false);
        when((BasicAuthorization) mockAuth).thenReturn(mockBasicAuth);
        when(mockBasicAuth.getUserId()).thenReturn("TestUser");

        // Test
        String screenName = twitterBase.getScreenName();

        // Verify
        verify(mockAuth).isEnabled();
        verify(mockAuth, atLeastOnce()).getUserId();
        assertEquals("TestUser", screenName);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetScreenName_WhenAuthIsNotEnabled_ThrowsIllegalStateException() throws TwitterException, IllegalStateException {
        // Mocking
        when(mockAuth.isEnabled()).thenReturn(false);

        // Test
        twitterBase.getScreenName();

        // Verify
        verify(mockAuth).isEnabled();
    }

    @Test
    public void testGetId_WhenAuthIsEnabled_ReturnsId() throws TwitterException, IllegalStateException {
        // Mocking
        BasicAuthorization mockBasicAuth = mock(BasicAuthorization.class);
        when(mockAuth.isEnabled()).thenReturn(true);
        when(mockAuth instanceof BasicAuthorization).thenReturn(true);
        when(mockAuth instanceof OAuthAuthorization).thenReturn(false);
        when((BasicAuthorization) mockAuth).thenReturn(mockBasicAuth);
        when(mockBasicAuth.getUserId()).thenReturn("TestUser");
        
        // Test
        long id = twitterBase.getId();
        
        // Verify
        verify(mockAuth).isEnabled();
        verify(mockAuth, atLeastOnce()).getUserId();
        assertEquals(0L, id);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetId_WhenAuthIsNotEnabled_ThrowsIllegalStateException() throws TwitterException, IllegalStateException {
        // Mocking
        when(mockAuth.isEnabled()).thenReturn(false);
        
        // Test
        twitterBase.getId();
        
        // Verify
        verify(mockAuth).isEnabled();
    }
    
    @Test
    public void testSetOAuthConsumer_WhenConsumerKeyAndConsumerSecretNotNull_SetsOAuthConsumer() {
        // Test
        twitterBase.setOAuthConsumer("ConsumerKey", "ConsumerSecret");
        
        // Verify
        verify(mockAuth).setOAuthConsumer("ConsumerKey", "ConsumerSecret");
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumer_WhenConsumerKeyNull_ThrowsNullPointerException() {
        // Test
        twitterBase.setOAuthConsumer(null, "ConsumerSecret");
        
        // Verify
        // Exception expected
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetOAuthConsumer_WhenConsumerSecretNull_ThrowsNullPointerException() {
        // Test
        twitterBase.setOAuthConsumer("ConsumerKey", null);
        
        // Verify
        // Exception expected
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetOAuthConsumer_WhenConsumerKeySecretPairAlreadySet_ThrowsIllegalStateException() {
        // Mocking
        BasicAuthorization mockBasicAuth = mock(BasicAuthorization.class);
        when(mockAuth instanceof NullAuthorization).thenReturn(false);
        when(mockAuth instanceof BasicAuthorization).thenReturn(true);
        when(mockAuth instanceof OAuthAuthorization).thenReturn(false);
        when(mockAuth instanceof OAuth2Authorization).thenReturn(false);
        when((BasicAuthorization) mockAuth).thenReturn(mockBasicAuth);
        
        // Test
        twitterBase.setOAuthConsumer("ConsumerKey", "ConsumerSecret");
        
        // Verify
        // Exception expected
    }

    // ... Additional test cases for each method of the TwitterBaseImpl class ...

}