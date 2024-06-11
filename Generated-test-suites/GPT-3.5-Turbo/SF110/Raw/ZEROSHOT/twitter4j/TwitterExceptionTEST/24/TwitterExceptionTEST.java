import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        String msg = "test message";
        TwitterException exception = new TwitterException(msg);

        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(cause);

        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        String msg = "test message";
        HttpResponse res = new HttpResponse();

        TwitterException exception = new TwitterException(msg, res);

        assertEquals(getCause(res) + "\n" + msg, exception.getMessage());
        assertEquals(res.getStatusCode(), exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringException() {
        String msg = "test message";
        Exception cause = new Exception();

        TwitterException exception = new TwitterException(msg, cause);

        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        String msg = "test message";
        Exception cause = new Exception();
        int statusCode = 200;

        TwitterException exception = new TwitterException(msg, cause, statusCode);

        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("");
        exception.getStatusCode();

        try {
            exception.getRetryAfter();
            fail("Exception not thrown");
        } catch (IllegalStateException e) {
            // Correct exception thrown
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException(cause);

        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("test", null);
        exception.getStatusCode();

        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("test", null);
        exception.getStatusCode();

        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("test", null);
        exception.getExceptionCode();
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("test", null);
        exception.setNested();

        assertTrue(exception.nested);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("test", null);
        TwitterException exception2 = new TwitterException("test", null);

        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("test", null);
        exception.getStatusCode();

        int hashCode = exception.hashCode();
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("test", null);
        exception.getStatusCode();

        String toString = exception.toString();
    }

    private String getCause(final HttpResponse res) {
        // implementation
    }
}