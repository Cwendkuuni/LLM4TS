import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructor1() {
        TwitterException ex = new TwitterException("Test message");
        assertNotNull(ex);
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor2() {
        Exception cause = new Exception();
        TwitterException ex = new TwitterException(cause);
        assertNotNull(ex);
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }
    
    // Additional test cases for each public method can be added here
    
}