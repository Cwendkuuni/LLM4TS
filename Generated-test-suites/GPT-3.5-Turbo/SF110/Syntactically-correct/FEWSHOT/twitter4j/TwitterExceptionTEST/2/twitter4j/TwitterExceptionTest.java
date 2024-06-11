import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructor1() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals("Test message", exception.getMessage());
    }
    
    @Test
    public void testConstructor2() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals("Test cause", exception.getCause().getMessage());
    }
    
    @Test
    public void testConstructor3() {
        HttpResponse res = new HttpResponseImpl();
        TwitterException exception = new TwitterException("Test message", res);
        assertEquals(200, res.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals("Test message", exception.getMessage());
    }
    
    @Test
    public void testConstructor4() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test message", cause);
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals("Test message", exception.getMessage());
        assertEquals("Test cause", exception.getCause().getMessage());
    }
    
    @Test
    public void testConstructor5() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException("Test message", cause, 500);
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals("Test message", exception.getMessage());
        assertEquals("Test cause", exception.getCause().getMessage());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test message");
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals(false, exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals(false, exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals(false, exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        assertEquals("0", exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test message");
        exception.setNested();
        assertEquals(true, exception.isNested());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        TwitterException exception3 = new TwitterException("Different message");
        
        assertEquals(true, exception1.equals(exception2));
        assertEquals(false, exception1.equals(exception3));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test message");
        TwitterException exception2 = new TwitterException("Test message");
        
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test message");
        String expected = "TwitterException{exceptionCode=[0], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expected, exception.toString());
    }
}