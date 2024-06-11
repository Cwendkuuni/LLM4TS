import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    // Test cases for TwitterException(String msg)

    @Test
    public void testTwitterExceptionWithMessage() {
        String message = "This is a test exception message";
        TwitterException exception = new TwitterException(message);
        assertEquals(message, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Test cases for TwitterException(Exception cause)

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getCause() instanceof TwitterException);
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Test cases for TwitterException(String msg, HttpResponse res)

    @Test
    public void testTwitterExceptionWithResponse() {
        String message = "This is a test exception message";
        HttpResponse res = new HttpResponseBuilder()
                .setStatusCode(420)
                .setResponseHeader("Retry-After", "60")
                .build();
        TwitterException exception = new TwitterException(message, res);
        assertEquals(message + "\n" + "420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Test cases for TwitterException(String msg, Exception cause)

    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        String message = "This is a test exception message";
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    // Test cases for TwitterException(String msg, Exception cause, int statusCode)

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        String message = "This is a test exception message";
        Exception cause = new Exception("Test cause");
        int statusCode = 500;
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
        String message = "This is a test exception message";
        int statusCode = 200;
        TwitterException exception = new TwitterException(message);
        assertEquals(-1, exception.getStatusCode());
        exception = new TwitterException(message, statusCode);
        assertEquals(statusCode, exception.getStatusCode());
    }

    // Test cases for getRateLimitStatus()

    @Test
    public void testGetRateLimitStatus() {
        String message = "This is a test exception message";
        HttpResponse res = new HttpResponseBuilder()
                .setStatusCode(200)
                .setResponseHeader("X-Rate-Limit-Limit", "100")
                .setResponseHeader("X-Rate-Limit-Remaining", "50")
                .setResponseHeader("X-Rate-Limit-Reset", "1577836800")
                .build();
        TwitterException exception = new TwitterException(message, res);
        assertNull(exception.getRateLimitStatus());
        res = new HttpResponseBuilder()
                .setStatusCode(200)
                .setResponseHeader("X-Rate-Limit-Limit", "100")
                .setResponseHeader("X-Rate-Limit-Remaining", "50")
                .setResponseHeader("X-Rate-Limit-Reset", "1577836800")
                .setResponseHeader("X-Rate-Limit-Reset-Date", "2020-01-01T00:00:00Z")
                .build();
        exception = new TwitterException(message, res);
        assertNotNull(exception.getRateLimitStatus());
        assertEquals(100, exception.getRateLimitStatus().getLimit());
        assertEquals(50, exception.getRateLimitStatus().getRemaining());
        assertEquals(1577836800, exception.getRateLimitStatus().getResetTimeInSeconds());
        assertEquals("2020-01-01T00:00:00Z", exception.getRateLimitStatus().getResetTime());
    }

    // Test cases for getFeatureSpecificRateLimitStatus()

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        String message = "This is a test exception message";
        HttpResponse res = new HttpResponseBuilder()
                .setStatusCode(200)
                .setResponseHeader("X-Feature-Rate-Limit-Limit", "100")
                .setResponseHeader("X-Feature-Rate-Limit-Remaining", "50")
                .setResponseHeader("X-Feature-Rate-Limit-Reset", "1577836800")
                .build();
        TwitterException exception = new TwitterException(message, res);
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        res = new HttpResponseBuilder()
                .setStatusCode(200)
                .setResponseHeader("X-Feature-Rate-Limit-Limit", "100")
                .setResponseHeader("X-Feature-Rate-Limit-Remaining", "50")
                .setResponseHeader("X-Feature-Rate-Limit-Reset", "1577836800")
                .setResponseHeader("X-Feature-Rate-Limit-Reset-Date", "2020-01-01T00:00:00Z")
                .build();
        exception = new TwitterException(message, res);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(100, exception.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals(50, exception.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals(1577836800, exception.getFeatureSpecificRateLimitStatus().getResetTimeInSeconds());
        assertEquals("2020-01-01T00:00:00Z", exception.getFeatureSpecificRateLimitStatus().getResetTime());
    }

    // Test case for getRetryAfter()

    @Test
    public void testGetRetryAfter() {
        String message = "This is a test exception message";
        HttpResponse res = new HttpResponseBuilder()
                .setStatusCode(420)
                .setResponseHeader("Retry-After", "60")
                .build();
        TwitterException exception = new TwitterException(message, res);
        assertEquals(60, exception.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterNotExceededRateLimitation() {
        String message = "This is a test exception message";
        HttpResponse res = new HttpResponseBuilder()
                .setStatusCode(200)
                .build();
        TwitterException exception = new TwitterException(message, res);
        exception.getRetryAfter();
    }

    // Test case for isCausedByNetworkIssue()

    @Test
    public void testIsCausedByNetworkIssue() {
        String message = "This is a test exception message";
        Exception cause = new IOException("Network issue");
        TwitterException exception = new TwitterException(message, cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testIsCausedByNetworkIssueFalse() {
        String message = "This is a test exception message";
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(message, cause);
        assertFalse(exception.isCausedByNetworkIssue());
    }

    // Test case for exceededRateLimitation()

    @Test
    public void testExceededRateLimitation() {
        String message = "This is a test exception message";
        TwitterException exception = new TwitterException(message);
        assertFalse(exception.exceededRateLimitation());
        exception = new TwitterException(message, new HttpResponse(400));
        assertFalse(exception.exceededRateLimitation());
        exception = new TwitterException(message, new HttpResponse(420));
        assertTrue(exception.exceededRateLimitation());
    }

    // Test case for resourceNotFound()

    @Test
    public void testResourceNotFound() {
        String message = "This is a test exception message";
        TwitterException exception = new TwitterException(message);
        assertFalse(exception.resourceNotFound());
        exception = new TwitterException(message, new HttpResponse(404));
        assertTrue(exception.resourceNotFound());
    }

    // Test case for getExceptionCode()

    @Test
    public void testGetExceptionCode() {
        String message = "This is a test exception message";
        TwitterException exception = new TwitterException(message);
        assertNotNull(exception.getExceptionCode());
    }

    // Test case for equals()

    @Test
    public void testEquals() {
        String message = "This is a test exception message";
        TwitterException exception1 = new TwitterException(message);
        TwitterException exception2 = new TwitterException(message);
        assertTrue(exception1.equals(exception2));
        exception1 = new TwitterException(message, new HttpResponse(400));
        assertFalse(exception1.equals(exception2));
    }

    // Test case for hashCode()

    @Test
    public void testHashCode() {
        String message = "This is a test exception message";
        TwitterException exception1 = new TwitterException(message);
        TwitterException exception2 = new TwitterException(message);
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }

    // Test case for toString()

    @Test
    public void testToString() {
        String message = "This is a test exception message";
        TwitterException exception = new TwitterException(message);
        assertNotNull(exception.toString());
    }

}