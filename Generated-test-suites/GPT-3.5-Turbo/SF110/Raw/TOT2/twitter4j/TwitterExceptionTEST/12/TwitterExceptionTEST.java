import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test Exception Message");
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Test IOException");
        TwitterException exception = new TwitterException(ioException);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception Message");
        TwitterException exception2 = new TwitterException("Another Test Exception Message");
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals(961, exception.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception Message");
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=0.0.16}";
        assertEquals(expected, exception.toString());
    }

}