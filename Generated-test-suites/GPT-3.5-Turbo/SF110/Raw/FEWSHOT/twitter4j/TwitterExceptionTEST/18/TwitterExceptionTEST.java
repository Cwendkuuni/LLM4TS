package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class TwitterExceptionTest {

    // Test constructor TwitterException(String)
    @Test
    public void testTwitterExceptionString() {
        TwitterException exception = new TwitterException("Test Message");
        assertNotNull(exception);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("", exception.getExceptionCode());
    }

    // Test constructor TwitterException(Exception)
    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception("Test Exception");
        TwitterException exception = new TwitterException(cause);
        assertNotNull(exception);
        assertEquals(cause.getMessage(), exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("", exception.getExceptionCode());
    }

    // Test constructor TwitterException(String, HttpResponse)
    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test Message", res);
        assertNotNull(exception);
        assertEquals("Test Message\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("", exception.getExceptionCode());
        assertEquals(60, exception.getRetryAfter());
    }

    // Test constructor TwitterException(String, Exception)
    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new Exception("Test Exception");
        TwitterException exception = new TwitterException("Test Message", cause);
        assertNotNull(exception);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertEquals("", exception.getExceptionCode());
    }

    // Test constructor TwitterException(String, Exception, int)
    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new Exception("Test Exception");
        TwitterException exception = new TwitterException("Test Message", cause, 404);
        assertNotNull(exception);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertTrue(exception.resourceNotFound());
        assertEquals("", exception.getExceptionCode());
    }

    // Test getStatusCode()
    @Test
    public void testGetStatusCode() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        TwitterException exception = new TwitterException("Test Message", res);
        assertEquals(420, exception.getStatusCode());
    }

    // Test getRateLimitStatus()
    @Test
    public void testGetRateLimitStatus() {
        HttpResponse res = new HttpResponse();
        res.setResponseHeader("X-RateLimit-Limit", "1500");
        res.setResponseHeader("X-RateLimit-Remaining", "500");
        res.setResponseHeader("X-RateLimit-Reset", "1599856335");
        TwitterException exception = new TwitterException("Test Message", res);
        assertNotNull(exception.getRateLimitStatus());
        assertEquals(1500, exception.getRateLimitStatus().getLimit());
        assertEquals(500, exception.getRateLimitStatus().getRemaining());
        assertEquals(1599856335, exception.getRateLimitStatus().getResetTimeInSeconds());
    }

    // Test getFeatureSpecificRateLimitStatus()
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        HttpResponse res = new HttpResponse();
        res.setResponseHeader("X-FeatureRateLimit-Limit", "1000");
        res.setResponseHeader("X-FeatureRateLimit-Remaining", "700");
        res.setResponseHeader("X-FeatureRateLimit-Reset", "1599856335");
        TwitterException exception = new TwitterException("Test Message", res);
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(1000, exception.getFeatureSpecificRateLimitStatus().getLimit());
        assertEquals(700, exception.getFeatureSpecificRateLimitStatus().getRemaining());
        assertEquals(1599856335, exception.getFeatureSpecificRateLimitStatus().getResetTimeInSeconds());
    }

    // Test getRetryAfter()
    @Test
    public void testGetRetryAfter() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test Message", res);
        assertEquals(60, exception.getRetryAfter());
    }

    // Test isCausedByNetworkIssue()
    @Test
    public void testIsCausedByNetworkIssue() {
        Exception cause = new IOException("Test IO Exception");
        TwitterException exception = new TwitterException(cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    // Test exceededRateLimitation()
    @Test
    public void testExceededRateLimitation() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        TwitterException exception = new TwitterException("Test Message", res);
        assertTrue(exception.exceededRateLimitation());
    }

    // Test resourceNotFound()
    @Test
    public void testResourceNotFound() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        TwitterException exception = new TwitterException("Test Message", res);
        assertTrue(exception.resourceNotFound());
    }

    // Test getExceptionCode()
    @Test
    public void testGetExceptionCode() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(500);
        TwitterException exception = new TwitterException("Test Message", res);
        assertEquals("500:", exception.getExceptionCode());
    }

    // Test setNested()
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Message");
        exception.setNested();
        assertTrue(exception.nested);
    }

    // Test equals()
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message");
        TwitterException exception2 = new TwitterException("Test Message");
        TwitterException exception3 = new TwitterException("Different Message");
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
    }

    // Test hashCode()
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Message");
        int hashCode = 31 * 31 * exception.getStatusCode() + 31 * exception.getRetryAfter();
        assertEquals(hashCode, exception.hashCode());
    }

    // Test toString()
    @Test
    public void testToString() {
        HttpResponse res = new HttpResponse();
        res.setResponseHeader("X-FeatureRateLimit-Limit", "1000");
        res.setResponseHeader("X-FeatureRateLimit-Remaining", "700");
        res.setResponseHeader("X-FeatureRateLimit-Reset", "1599856335");
        TwitterException exception = new TwitterException("Test Message", res);
        String expected = "TwitterException{exceptionCode=[], statusCode=200, retryAfter=0, rateLimitStatus=RateLimitStatus{limit=1000, remaining=700, resetTimeInSeconds=1599856335}, version=4.0.7}";
        assertEquals(expected, exception.toString());
    }
}