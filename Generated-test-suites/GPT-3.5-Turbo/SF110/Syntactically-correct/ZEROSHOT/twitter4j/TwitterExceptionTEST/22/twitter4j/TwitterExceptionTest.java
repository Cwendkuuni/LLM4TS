package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        String msg = "Test exception message";
        TwitterException exception = new TwitterException(msg);

        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Test cause exception");
        TwitterException exception = new TwitterException(cause);

        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageAndResponse() {
        String msg = "Test exception message";
        HttpResponse response = new HttpResponseImpl();
        TwitterException exception = new TwitterException(msg, response);

        assertEquals(response.getStatusCode() + ":" + response.getStatusMessage() + "\n" + msg, exception.getMessage());
        assertEquals(response.getStatusCode(), exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        String msg = "Test exception message";
        Exception cause = new Exception("Test cause exception");
        TwitterException exception = new TwitterException(msg, cause);

        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        String msg = "Test exception message";
        Exception cause = new Exception("Test cause exception");
        int statusCode = 400;
        TwitterException exception = new TwitterException(msg, cause, statusCode);

        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test exception message");
        exception.rateLimitStatus = rateLimitStatus;

        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test exception message");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;

        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        response.addResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test exception message", response);

        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException("Test IO Exception");
        TwitterException exception = new TwitterException("Test exception message", ioException);

        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception1 = new TwitterException("Test exception message", new HttpResponseImpl());
        TwitterException exception2 = new TwitterException("Test exception message", new HttpResponseImpl());
        exception1.rateLimitStatus = null;
        exception2.rateLimitStatus = rateLimitStatus;
        exception2.statusCode = 400;

        assertFalse(exception1.exceededRateLimitation());
        assertTrue(exception2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test exception message", new HttpResponseImpl());
        exception.statusCode = 404;

        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test exception message");

        assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test exception message");
        TwitterException exception2 = new TwitterException("Test exception message");
        exception2.retryAfter = 60;

        assertTrue(exception1.equals(exception1));
        assertFalse(exception1.equals(null));
        assertFalse(exception1.equals(new Object()));
        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test exception message");
        exception.retryAfter = 60;
        exception.rateLimitStatus = rateLimitStatus;

        int expectedHashCode = 31 * (31 * (31 * 0 + 0) + 60) + rateLimitStatus.hashCode();

        assertEquals(expectedHashCode, exception.hashCode());
    }

    @Test
    public void testToString() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        String expectedToString = "TwitterException{" +
            "exceptionCode=[], " +
            "statusCode=400, " +
            "retryAfter=60, " +
            "rateLimitStatus=" + rateLimitStatus + ", " +
            "version=" + Version.getVersion() +
            "}";

        TwitterException exception = new TwitterException("Test exception message");
        exception.retryAfter = 60;
        exception.rateLimitStatus = rateLimitStatus;

        assertEquals(expectedToString, exception.toString());
    }
}