import org.junit.*;
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
            void setFactory() {
                // Mock method for testing
            }
        };
    }

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        // Typical use case
        when(twitterBase.auth.isEnabled()).thenReturn(true);
        assertEquals("mockScreenName", twitterBase.getScreenName());

        // Edge case: auth is not enabled
        when(twitterBase.auth.isEnabled()).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> {
            twitterBase.getScreenName();
        });
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        // Typical use case
        when(twitterBase.auth.isEnabled()).thenReturn(true);
        assertEquals(12345L, twitterBase.getId());

        // Edge case: auth is not enabled
        when(twitterBase.auth.isEnabled()).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> {
            twitterBase.getId();
        });
    }

    // More test cases for other methods can be added here

}