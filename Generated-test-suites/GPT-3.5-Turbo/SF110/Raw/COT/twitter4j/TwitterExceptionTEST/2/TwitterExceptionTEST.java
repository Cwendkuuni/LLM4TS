import static org.junit.Assert.*;
import org.junit.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMsg() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("Test message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMsgAndRes() {
        HttpResponse res = new HttpResponseImpl();
        TwitterException exception = new TwitterException("Test message", res);
        assertEquals("Test message\n" + TwitterException.getCause(res), exception.getMessage());
        assertEquals(res.getStatusCode(), exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMsgAndCause() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test message", cause, 400);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test message");
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test message");
        exception.setNested();
        // No assertions, just testing the method invocation
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message 1");
        TwitterException exception2 = new TwitterException("Test message 2");
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        assertNotNull(exception.toString());
    }

}