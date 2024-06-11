import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import twitter4j.*;

public class TwitterBaseImplTest {

    private Configuration confMock;
    private Authorization authMock;
    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        confMock = Mockito.mock(Configuration.class);
        authMock = Mockito.mock(Authorization.class);
        twitterBase = new TwitterBaseImpl(confMock, authMock) {};
    }

    @Test
    public void testGetScreenName() {
        // Typical use case
        Mockito.when(authMock.isEnabled()).thenReturn(true);
        assertEquals("TestScreenName", twitterBase.getScreenName());

        // Edge case: when auth is not enabled
        Mockito.when(authMock.isEnabled()).thenReturn(false);
        try {
            twitterBase.getScreenName();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }

    @Test
    public void testGetId() {
        // Typical use case
        Mockito.when(authMock.isEnabled()).thenReturn(true);
        assertEquals(12345L, twitterBase.getId());

        // Edge case: when auth is not enabled
        Mockito.when(authMock.isEnabled()).thenReturn(false);
        try {
            twitterBase.getId();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }

    // Add more test cases for other public methods as per the requirement

}