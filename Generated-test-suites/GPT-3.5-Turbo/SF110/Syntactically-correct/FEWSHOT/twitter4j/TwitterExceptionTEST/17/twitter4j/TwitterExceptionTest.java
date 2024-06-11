package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

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
        Exception cause = new RuntimeException("Cause exception");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause.toString(), exception.getCause().toString());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMsgAndResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        response.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test message", response);
        assertEquals("200:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.\nTest message", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMsgAndCause() {
        Exception cause = new RuntimeException("Cause exception");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause.toString(), exception.getCause().toString());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new RuntimeException("Cause exception");
        TwitterException exception = new TwitterException("Test message", cause, 500);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause.toString(), exception.getCause().toString());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(200, 100, 50, 80);
        TwitterException exception = new TwitterException("Test message");
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl(200, 100, 50, 80);
        TwitterException exception = new TwitterException("Test message");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 420;
        exception.retryAfter = 60;
        assertEquals(60, exception.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterNotExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException();
        TwitterException exception = new TwitterException(ioException);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testIsCausedByNetworkIssueFalse() {
        RuntimeException runtimeException = new RuntimeException();
        TwitterException exception = new TwitterException(runtimeException);
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 400;
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationNotExceeded() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 404;
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testResourceNotFoundFalse() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message");
        exception1.statusCode = 200;
        exception1.retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(200, 100, 50, 80);
        exception1.rateLimitStatus = rateLimitStatus;
        
        TwitterException exception2 = new TwitterException("Test message");
        exception2.statusCode = 200;
        exception2.retryAfter = 60;
        exception2.rateLimitStatus = rateLimitStatus;
        
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        exception.retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(200, 100, 50, 80);
        exception.rateLimitStatus = rateLimitStatus;
        
        int expectedHashCode = 31 * 200 + 31 * 60 + rateLimitStatus.hashCode();
        assertEquals(expectedHashCode, exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        exception.retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(200, 100, 50, 80);
        exception.rateLimitStatus = rateLimitStatus;
        
        String expectedToString = "TwitterException{exceptionCode=[], statusCode=200, retryAfter=60, rateLimitStatus=" + rateLimitStatus.toString() + ", version=Version}";
        assertEquals(expectedToString, exception.toString());
    }
}