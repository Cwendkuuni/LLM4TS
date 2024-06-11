import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import twitter4j.internal.http.HttpResponse;

public class TwitterExceptionTest {
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test");
        assertEquals(-1, exception.getStatusCode());
        
        HttpResponse response = new HttpResponse(null);
        TwitterException exceptionWithResponse = new TwitterException("Test", response);
        assertEquals(0, exceptionWithResponse.getStatusCode());

        response.setStatusCode(200);
        assertEquals(200, exceptionWithResponse.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test");
        assertNull(exception.getRateLimitStatus());
        
        HttpResponse response = new HttpResponse(null);
        TwitterException exceptionWithResponse = new TwitterException("Test", response);
        assertNull(exceptionWithResponse.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        
        HttpResponse response = new HttpResponse(null);
        TwitterException exceptionWithResponse = new TwitterException("Test", response);
        assertNull(exceptionWithResponse.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test");
        boolean exceptionThrown = false;
        try {
            exception.getRetryAfter();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        HttpResponse response = new HttpResponse(null);
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        TwitterException exceptionWithResponse = new TwitterException("Test", response);
        assertEquals(60, exceptionWithResponse.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test");
        exception.setNested();
        assertFalse(exception.isCausedByNetworkIssue());
        
        TwitterException nestedException = new TwitterException(new IOException());
        assertTrue(nestedException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test");
        assertFalse(exception.exceededRateLimitation());
        
        HttpResponse response = new HttpResponse(null);
        response.setStatusCode(400);
        TwitterException exceptionWithResponse = new TwitterException("Test", response);
        assertTrue(exceptionWithResponse.exceededRateLimitation());
        
        response.setStatusCode(420);
        assertFalse(exceptionWithResponse.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test");
        assertFalse(exception.resourceNotFound());
        
        HttpResponse response = new HttpResponse(null);
        response.setStatusCode(404);
        TwitterException exceptionWithResponse = new TwitterException("Test", response);
        assertTrue(exceptionWithResponse.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test");
        assertEquals("", exception.getExceptionCode());
        
        TwitterException filteredException = new TwitterException("Test", null, 200);
        assertEquals("ExceptionDiagnosis", filteredException.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test");
        TwitterException exception2 = new TwitterException("Test");
        assertTrue(exception1.equals(exception2));
        
        HttpResponse response = new HttpResponse(null);
        TwitterException exceptionWithResponse1 = new TwitterException("Test", response);
        TwitterException exceptionWithResponse2 = new TwitterException("Test", response);
        assertTrue(exceptionWithResponse1.equals(exceptionWithResponse2));
        
        TwitterException exceptionWithDifferentRateLimitStatus = new TwitterException("Test", response);
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        exceptionWithDifferentRateLimitStatus.setRateLimitStatus(rateLimitStatus);
        assertFalse(exceptionWithResponse1.equals(exceptionWithDifferentRateLimitStatus));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test");
        TwitterException exception2 = new TwitterException("Test");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        HttpResponse response = new HttpResponse(null);
        TwitterException exceptionWithResponse1 = new TwitterException("Test", response);
        TwitterException exceptionWithResponse2 = new TwitterException("Test", response);
        assertEquals(exceptionWithResponse1.hashCode(), exceptionWithResponse2.hashCode());
        
        TwitterException exceptionWithDifferentRateLimitStatus = new TwitterException("Test", response);
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        exceptionWithDifferentRateLimitStatus.setRateLimitStatus(rateLimitStatus);
        assertNotEquals(exceptionWithResponse1.hashCode(), exceptionWithDifferentRateLimitStatus.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception1 = new TwitterException("Test");
        assertTrue(exception1.toString().contains("statusCode=-1"));
        
        HttpResponse response = new HttpResponse(null);
        TwitterException exceptionWithResponse = new TwitterException("Test", response);
        assertTrue(exceptionWithResponse.toString().contains("statusCode=0"));
    }
    
}