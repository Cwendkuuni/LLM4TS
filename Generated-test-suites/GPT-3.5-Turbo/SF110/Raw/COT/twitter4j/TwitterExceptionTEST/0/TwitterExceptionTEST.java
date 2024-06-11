
import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "Test Exception";
        TwitterException twitterException = new TwitterException(message);

        assertEquals(message, twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertNull(twitterException.getExceptionCode());
    }

    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException twitterException = new TwitterException(cause);

        assertEquals(cause, twitterException.getCause());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertNull(twitterException.getExceptionCode());
    }

    @Test
    public void testConstructorWithMessageAndResponse() {
        String message = "Test Exception";
        HttpResponse res = new HttpResponse(404, "Not Found");
        TwitterException twitterException = new TwitterException(message, res);

        assertEquals(message + "\n" + TwitterException.getCause(res), twitterException.getMessage());
        assertEquals(404, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertTrue(twitterException.resourceNotFound());
        assertNotNull(twitterException.getExceptionCode());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String message = "Test Exception";
        Exception cause = new Exception("Test Cause");
        TwitterException twitterException = new TwitterException(message, cause);

        assertEquals(message, twitterException.getMessage());
        assertEquals(cause, twitterException.getCause());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertNull(twitterException.getExceptionCode());
    }

    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        String message = "Test Exception";
        Exception cause = new Exception("Test Cause");
        int statusCode = 400;
        TwitterException twitterException = new TwitterException(message, cause, statusCode);

        assertEquals(message, twitterException.getMessage());
        assertEquals(cause, twitterException.getCause());
        assertEquals(statusCode, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertTrue(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertNull(twitterException.getExceptionCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException twitterException = new TwitterException("Test Exception");
        twitterException.getRetryAfter();
    }

    @Test
    public void testGetRetryAfterWhenStatusCodeIs420() {
        HttpResponse res = new HttpResponse(420, "Rate Limit Exceeded");
        res.setResponseHeader("Retry-After", "60");
        TwitterException twitterException = new TwitterException("Test Exception", res);

        assertEquals(60, twitterException.getRetryAfter());
    }

    @Test
    public void testEqualsWhenSameObject() {
        TwitterException twitterException = new TwitterException("Test Exception");

        assertTrue(twitterException.equals(twitterException));
    }

    @Test
    public void testEqualsWhenDifferentClass() {
        TwitterException twitterException = new TwitterException("Test Exception");
        Object obj = new Object();

        assertFalse(twitterException.equals(obj));
    }

    @Test
    public void testEqualsWhenDifferentRetryAfter() {
        TwitterException twitterException1 = new TwitterException("Test Exception");
        HttpResponse res = new HttpResponse(420, "Rate Limit Exceeded");
        res.setResponseHeader("Retry-After", "60");
        TwitterException twitterException2 = new TwitterException("Test Exception", res);

        assertFalse(twitterException1.equals(twitterException2));
    }

    @Test
    public void testEqualsWhenDifferentStatusCode() {
        TwitterException twitterException1 = new TwitterException("Test Exception");
        TwitterException twitterException2 = new TwitterException("Test Exception", new Exception(), 200);

        assertFalse(twitterException1.equals(twitterException2));
    }

    @Test
    public void testEqualsWhenDifferentRateLimitStatus() {
        TwitterException twitterException1 = new TwitterException("Test Exception");
        HttpResponse res = new HttpResponse(420, "Rate Limit Exceeded");
        res.setResponseHeader("Retry-After", "60");
        TwitterException twitterException2 = new TwitterException("Test Exception", res);

        assertFalse(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test Exception");

        assertNotNull(twitterException.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test Exception");

        assertNotNull(twitterException.toString());
    }
}

