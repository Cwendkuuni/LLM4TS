import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructorWithString() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals("Test message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithException() {
        Exception exception = new Exception("Test exception");
        TwitterException twitterException = new TwitterException(exception);
        assertEquals(exception, twitterException.getCause());
    }
    
    // Add more test cases for other methods
    
}