package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TwitterExceptionTest {
    @Test
    public void testConstructorWithMsg() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNotEquals("", exception.getExceptionCode());
        assertEquals("TwitterException{exceptionCode=[" + exception.getExceptionCode() + "], statusCode=-1, retryAfter=-1, rateLimitStatus=null, version=" + Version.getVersion() + "}", exception.toString());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNotEquals("", exception.getExceptionCode());
        assertEquals("TwitterException{exceptionCode=[" + exception.getExceptionCode() + "], statusCode=-1, retryAfter=-1, rateLimitStatus=null, version=" + Version.getVersion() + "}", exception.toString());
    }
    
    @Test
    public void testConstructorWithMsgAndRes() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(420);
        res.setHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test Exception", res);
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.\nTest Exception", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(60, exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNotEquals("", exception.getExceptionCode());
        assertEquals("TwitterException{exceptionCode=[" + exception.getExceptionCode() + "], statusCode=420, retryAfter=60, rateLimitStatus=null, version=" + Version.getVersion() + "}", exception.toString());
    }
    
    @Test
    public void testConstructorWithMsgAndCause() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNotEquals("", exception.getExceptionCode());
        assertEquals("TwitterException{exceptionCode=[" + exception.getExceptionCode() + "], statusCode=-1, retryAfter=-1, rateLimitStatus=null, version=" + Version.getVersion() + "}", exception.toString());
    }
    
    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test Exception", cause, 500);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNotEquals("", exception.getExceptionCode());
        assertEquals("TwitterException{exceptionCode=[" + exception.getExceptionCode() + "], statusCode=500, retryAfter=-1, rateLimitStatus=null, version=" + Version.getVersion() + "}", exception.toString());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeNot420() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.getRetryAfter();
    }
    
    @Test
    public void testGetRetryAfterWhenStatusCodeIs420() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.getStatusCode() = 420;
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNotEquals("", exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals(31 * (31 * (31 * 17 + -1) + -1) + 0, exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("TwitterException{exceptionCode=[" + exception.getExceptionCode() + "], statusCode=-1, retryAfter=-1, rateLimitStatus=null, version=" + Version.getVersion() + "}", exception.toString());
    }
    
    @Test
    public void testGetCause() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(400);
        TwitterException exception = new TwitterException("Test Exception", res);
        assertEquals("The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", exception.getExceptionCode());
    }
}