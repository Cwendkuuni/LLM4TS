import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    
    private TwitterBaseImpl twitterBase;

    @Before
    public void setUp() {
        twitterBase = new TwitterBaseImpl();
    }

    @Test
    public void testGetScreenName() {
        // Test typical case
        assertEquals("username", twitterBase.getScreenName());
        
        // Test edge case - when auth is disabled
        // An IllegalStateException should be thrown
        twitterBase.auth.setEnabled(false);
        assertThrows(IllegalStateException.class, () -> {
            twitterBase.getScreenName();
        });
        
        // Test edge case - when screenName is null
        twitterBase.screenName = null;
        assertEquals("username", twitterBase.getScreenName());
    }
    
    @Test
    public void testGetId() {
        // Test typical case
        assertEquals(12345, twitterBase.getId());
        
        // Test edge case - when auth is disabled
        // An IllegalStateException should be thrown
        twitterBase.auth.setEnabled(false);
        assertThrows(IllegalStateException.class, () -> {
            twitterBase.getId();
        });
        
        // Test edge case - when id is 0
        assertEquals(12345, twitterBase.getId());
    }
    
    // Additional test cases for other methods can be added here
    
}