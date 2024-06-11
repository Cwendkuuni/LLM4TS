import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {
    
    TwitterBaseImpl twitterBase;
    
    @Before
    public void setUp() {
        Configuration confMock = mock(Configuration.class);
        Authorization authMock = mock(Authorization.class);
        twitterBase = new TwitterBaseImpl(confMock, authMock) {
            @Override
            void setFactory() {
                // Do nothing for testing purpose
            }
        };
    }
    
    @Test
    public void testGetScreenName() throws TwitterException {
        // Typical use case
        assertEquals("TestUser", twitterBase.getScreenName());
        
        // Edge case: Auth not enabled
        Authorization authMock = mock(Authorization.class);
        when(authMock.isEnabled()).thenReturn(false);
        twitterBase.auth = authMock;
        try {
            twitterBase.getScreenName();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // Success
        }
    }
    
    @Test
    public void testGetId() throws TwitterException {
        // Typical use case
        assertEquals(12345, twitterBase.getId());
        
        // Edge case: Auth not enabled
        Authorization authMock = mock(Authorization.class);
        when(authMock.isEnabled()).thenReturn(false);
        twitterBase.auth = authMock;
        try {
            twitterBase.getId();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // Success
        }
    }
    
    // Additional test cases for other methods can be added here
    
}