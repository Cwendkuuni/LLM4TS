
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterException() {
        String msg = "Test message";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        String msg = "Test message";
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        String msg = "Test message";
        HttpResponse res = new HttpResponseImpl(null, null);
        TwitterException exception = new TwitterException(msg, res);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithExceptionAndStatusCode() {
        String msg = "Test message";
        Exception cause = new Exception("Test cause");
        int statusCode = 200;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 200;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("");
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        int retryAfter = 60;
        int statusCode = 420;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        assertEquals(retryAfter, exception.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        int statusCode = 200;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        Exception cause = new IOException();
        TwitterException exception = new TwitterException("");
        exception.initCause(cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testIsCausedByNetworkIssueWhenCauseIsNull() {
        TwitterException exception = new TwitterException("");
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitationWithRateLimitStatus() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.rateLimitStatus = rateLimitStatus;
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithoutRateLimitStatus() {
        int statusCode = 400;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithStatusCode420() {
        int statusCode = 420;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        int statusCode = 404;
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("");
        exception.statusCode = 400;
        assertEquals(Integer.toHexString(exception.hashCode()), exception.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("");
        exception1.statusCode = 200;
        exception1.retryAfter = 60;
        TwitterException exception2 = new TwitterException("");
        exception2.statusCode = 200;
        exception2.retryAfter = 60;
        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testNotEquals() {
        TwitterException exception1 = new TwitterException("");
        exception1.statusCode = 200;
        exception1.retryAfter = 60;
        TwitterException exception2 = new TwitterException("");
        exception2.statusCode = 200;
        exception2.retryAfter = 30;
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("");
        exception.statusCode = 200;
        exception.retryAfter = 60;
        int expectedHashCode = 31 * (31 * 200 + 60);
        assertEquals(expectedHashCode, exception.hashCode());
    }

    @Test
    public void testToString() {
        int statusCode = 200;
        int retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        String version = Version.getVersion();
        TwitterException exception = new TwitterException("");
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals("TwitterException{statusCode=" + statusCode + ", retryAfter=" + retryAfter + ", rateLimitStatus=" + rateLimitStatus + ", version=" + version + '}', exception.toString());
    }
}
