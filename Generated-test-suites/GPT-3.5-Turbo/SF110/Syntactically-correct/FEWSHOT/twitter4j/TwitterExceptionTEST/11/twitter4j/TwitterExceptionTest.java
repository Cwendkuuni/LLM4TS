package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TwitterExceptionTest {
    @Test
    public void testConstructorWithMessage() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("Test message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(420);
        httpResponse.setResponseHeader("Retry-After", "60");
        
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test message", cause, 500);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        assertEquals(200, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(100, 200, 300, 400);
        TwitterException exception = new TwitterException("Test message");
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(100, 200, 300, 400);
        TwitterException exception = new TwitterException("Test message");
        exception.featureSpecificRateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test message");
        exception.retryAfter = 60;
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.isCausedByNetworkIssue());
        
        exception = new TwitterException(new IOException("Network error"));
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.exceededRateLimitation());
        
        exception = new TwitterException("Test message", new HttpResponseImpl());
        assertFalse(exception.exceededRateLimitation());
        
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(100, 200, 300, 400);
        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(400);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        exception.rateLimitStatus = rateLimitStatus;
        assertTrue(exception.exceededRateLimitation());
        
        httpResponse.setStatusCode(420);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(404);
        TwitterException exception = new TwitterException("Test message", httpResponse);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("", exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        RateLimitStatus rateLimitStatus1 = new RateLimitStatusJSONImpl(100, 200, 300, 400);
        RateLimitStatus rateLimitStatus2 = new RateLimitStatusJSONImpl(200, 300, 400, 500);
        
        TwitterException exception1 = new TwitterException("Test message");
        exception1.statusCode = 200;
        exception1.retryAfter = 60;
        exception1.rateLimitStatus = rateLimitStatus1;
        
        TwitterException exception2 = new TwitterException("Test message");
        exception2.statusCode = 200;
        exception2.retryAfter = 60;
        exception2.rateLimitStatus = rateLimitStatus1;
        
        TwitterException exception3 = new TwitterException("Test message");
        exception3.statusCode = 500;
        exception3.retryAfter = 60;
        exception3.rateLimitStatus = rateLimitStatus1;
        
        TwitterException exception4 = new TwitterException("Test message");
        exception4.statusCode = 200;
        exception4.retryAfter = 120;
        exception4.rateLimitStatus = rateLimitStatus1;
        
        TwitterException exception5 = new TwitterException("Test message");
        exception5.statusCode = 200;
        exception5.retryAfter = 60;
        exception5.rateLimitStatus = rateLimitStatus2;
        
        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
        assertFalse(exception1.equals(exception4));
        assertFalse(exception1.equals(exception5));
    }
    
    @Test
    public void testHashCode() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(100, 200, 300, 400);
        
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        exception.retryAfter = 60;
        exception.rateLimitStatus = rateLimitStatus;
        
        int expectedHashCode = 31 * (31 * (31 * 0 + exception.statusCode) + exception.retryAfter) + ((exception.rateLimitStatus != null) ? exception.rateLimitStatus.hashCode() : 0);
        assertEquals(expectedHashCode, exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        exception.retryAfter = 60;
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl(100, 200, 300, 400);
        exception.rateLimitStatus = rateLimitStatus;
        
        String expectedToString = "TwitterException{exceptionCode=[], statusCode=200, retryAfter=60, rateLimitStatus=" + rateLimitStatus + ", version=Twitter4J.getVersion()}";
        assertEquals(expectedToString, exception.toString());
    }
}