
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    @Test
    public void testConstructor1() {
        TwitterException exception = new TwitterException("Test");
        assertEquals("Test", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructor2() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructor3() {
        HttpResponse res = new HttpResponse(200, null);
        TwitterException exception = new TwitterException("Test", res);
        assertEquals("Test", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructor4() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test", cause);
        assertEquals("Test", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testConstructor5() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test", cause, 404);
        assertEquals("Test", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertTrue(exception.resourceNotFound());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test");
        exception.getRetryAfter();
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test1");
        TwitterException exception2 = new TwitterException("Test1");
        assertTrue(exception1.equals(exception2));
        
        exception1 = new TwitterException("Test1");
        exception2 = new TwitterException("Test2");
        assertFalse(exception1.equals(exception2));
        
        exception1 = new TwitterException("Test1");
        exception2 = new TwitterException("Test1");
        exception1.setNested();
        assertFalse(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test1");
        TwitterException exception2 = new TwitterException("Test1");
        assertEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test1");
        exception2 = new TwitterException("Test2");
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        
        exception1 = new TwitterException("Test1");
        exception2 = new TwitterException("Test1");
        exception1.setNested();
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", exception.toString());
    }
}
