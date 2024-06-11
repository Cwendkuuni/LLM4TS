import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithStringMessage() {
        TwitterException exception = new TwitterException("Test Exception");
        
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionWithExceptionCause() {
        TwitterException cause = new TwitterException("Cause Exception");
        TwitterException exception = new TwitterException(cause);
        
        assertEquals("Cause Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(404);
        response.setResponseHeader("Retry-After", "15");
        
        TwitterException exception = new TwitterException("Test Exception", response);
        
        assertEquals("Test Exception\n404:The URI requested is invalid or the resource requested, such as a user, does not exists.", 
                     exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(15, exception.getRetryAfter());
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testTwitterExceptionWithExceptionCauseAndStatusCode() {
        TwitterException cause = new TwitterException("Cause Exception");
        TwitterException exception = new TwitterException("Test Exception", cause, 500);
        
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Test Exception");
        
        exception.getRetryAfter();
    }
    
    @Test
    public void testExceededRateLimitationWithRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.setRateLimitStatus(new RateLimitStatusImpl(0, 60));
        
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testExceededRateLimitationWithStatusCode420() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.setStatusCode(420);
        
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testExceededRateLimitationWithoutRateLimitStatusAndStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        
        assertFalse(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFoundWithStatusCode404() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.setStatusCode(404);
        
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testResourceNotFoundWithoutStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        
        assertNotNull(exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.setNested();
        
        assertTrue(exception.isNested());
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
        
        assertNotNull(exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        
        assertNotNull(exception.toString());
    }
}