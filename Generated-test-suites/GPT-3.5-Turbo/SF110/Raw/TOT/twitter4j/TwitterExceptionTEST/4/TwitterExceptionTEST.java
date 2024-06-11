import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    // Test case for TwitterException(String msg)
    @Test
    public void testTwitterExceptionMsg() {
        String message = "Test Exception";
        TwitterException exception = new TwitterException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Test case for TwitterException(Exception cause)
    @Test
    public void testTwitterExceptionCause() {
        IOException cause = new IOException("IOException Test");
        TwitterException exception = new TwitterException(cause);

        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Test case for TwitterException(String msg, HttpResponse res)
    @Test
    public void testTwitterExceptionMsgRes() {
        String message = "Test Exception";
        HttpResponse res = new HttpResponse();
        res.setStatusCode(200);
        res.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException(message, res);

        assertEquals(message + "\n200:There was no new data to return.", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Test case for TwitterException(String msg, Exception cause)
    @Test
    public void testTwitterExceptionMsgCause() {
        String message = "Test Exception";
        IOException cause = new IOException("IOException Test");
        TwitterException exception = new TwitterException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Test case for TwitterException(String msg, Exception cause, int statusCode)
    @Test
    public void testTwitterExceptionMsgCauseStatusCode() {
        String message = "Test Exception";
        IOException cause = new IOException("IOException Test");
        int statusCode = 400;
        TwitterException exception = new TwitterException(message, cause, statusCode);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    // Test case for getStatusCode()
    @Test
    public void testGetStatusCode() {
        int statusCode = 200;
        TwitterException exception = new TwitterException("Test Exception");
        exception.statusCode = statusCode;

        assertEquals(statusCode, exception.getStatusCode());
    }

    // Test case for getRateLimitStatus()
    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("Test Exception");
        exception.rateLimitStatus = rateLimitStatus;

        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    // Test case for getFeatureSpecificRateLimitStatus()
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("Test Exception");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;

        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    // Test case for getRetryAfter()
    @Test
    public void testGetRetryAfter() {
        int statusCode = 420;
        int retryAfter = 60;
        TwitterException exception = new TwitterException("Test Exception");
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;

        assertEquals(retryAfter, exception.getRetryAfter());
    }

    // Test case for isCausedByNetworkIssue()
    @Test
    public void testIsCausedByNetworkIssue() {
        IOException cause = new IOException("IOException Test");
        TwitterException exception = new TwitterException(cause);

        assertTrue(exception.isCausedByNetworkIssue());
    }

    // Test case for exceededRateLimitation()
    @Test
    public void testExceededRateLimitation() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("Test Exception");
        exception.statusCode = statusCode;
        exception.rateLimitStatus = rateLimitStatus;

        assertTrue(exception.exceededRateLimitation());
    }

    // Test case for resourceNotFound()
    @Test
    public void testResourceNotFound() {
        int statusCode = 404;
        TwitterException exception = new TwitterException("Test Exception");
        exception.statusCode = statusCode;

        assertTrue(exception.resourceNotFound());
    }

    // Test case for getExceptionCode()
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");

        assertNotNull(exception.getExceptionCode());
    }

    // Test case for equals()
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        exception1.statusCode = 200;
        exception1.retryAfter = 60;
        exception1.rateLimitStatus = new RateLimitStatus();

        TwitterException exception2 = new TwitterException("Test Exception");
        exception2.statusCode = 200;
        exception2.retryAfter = 60;
        exception2.rateLimitStatus = new RateLimitStatus();

        assertTrue(exception1.equals(exception2));
    }

    // Test case for hashCode()
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.statusCode = 200;
        exception.retryAfter = 60;
        exception.rateLimitStatus = new RateLimitStatus();

        assertNotNull(exception.hashCode());
    }

    // Test case for toString()
    @Test
    public void testToString() {
        int statusCode = 200;
        int retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        TwitterException exception = new TwitterException("Test Exception");
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        exception.rateLimitStatus = rateLimitStatus;

        assertNotNull(exception.toString());
    }
}