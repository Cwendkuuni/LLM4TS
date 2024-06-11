import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        String msg = "Test Message";
        TwitterException exception = new TwitterException(msg);

        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);

        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        String msg = "Test Message";
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");

        TwitterException exception = new TwitterException(msg, res);

        assertEquals(msg + "\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testTwitterExceptionStringException() {
        String msg = "Test Message";
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(msg, cause);

        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        String msg = "Test Message";
        Exception cause = new Exception("Test Cause");
        int statusCode = 404;
        TwitterException exception = new TwitterException(msg, cause, statusCode);

        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("Test");
        exception.rateLimitStatus = rateLimitStatus;

        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("Test");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;

        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test");
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Test");
        TwitterException exception = new TwitterException(ioException);

        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test", new HttpResponse());
        assertTrue(exception1.exceededRateLimitation());

        TwitterException exception2 = new TwitterException("Test", new HttpResponse());
        exception2.rateLimitStatus = null;
        assertFalse(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test", new HttpResponse());
        exception.statusCode = 404;

        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test");

        assertEquals("d144440f", exception.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test");
        exception1.statusCode = 404;

        TwitterException exception2 = new TwitterException("Test");
        exception2.statusCode = 404;

        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test");
        exception.statusCode = 404;

        assertEquals(12100105, exception.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test");

        assertEquals("TwitterException{exceptionCode=[d144440f], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=}", exception.toString());
    }
}

