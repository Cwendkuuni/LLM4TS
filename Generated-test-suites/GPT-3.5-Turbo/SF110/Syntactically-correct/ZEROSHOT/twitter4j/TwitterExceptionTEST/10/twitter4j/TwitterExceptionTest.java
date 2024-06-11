import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    private TwitterException exception;
    
    @Before
    public void setUp() {
        exception = new TwitterException("Test Message");
    }

    @Test
    public void testConstructor1() {
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructor2() {
        Exception cause = new Exception("Test Cause");
        exception = new TwitterException(cause);
        
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructor3() {
        HttpResponse res = new HttpResponse(200, "Test Response", null, null);
        exception = new TwitterException("Test Message", res);
        
        assertEquals("Test Response\nTest Message", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructor4() {
        Exception cause = new Exception("Test Cause");
        exception = new TwitterException("Test Message", cause);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructor5() {
        Exception cause = new Exception("Test Cause");
        exception = new TwitterException("Test Message", cause, 404);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        assertFalse(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        assertFalse(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        assertEquals("736973746572346A", exception.getExceptionCode());
    }
    
    @Test
    public void testSetNested() {
        assertFalse(exception.nested);
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testEquals() {
        TwitterException exception2 = new TwitterException("Test Message");
        assertTrue(exception.equals(exception2));
        
        exception2 = new TwitterException("Different Message");
        assertFalse(exception.equals(exception2));
    }
    
    @Test
    public void testHashCode() {
        int expected = -1;
        if (exception.getRateLimitStatus() != null) {
            expected += exception.getRateLimitStatus().hashCode();
        }
        assertEquals(expected, exception.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "TwitterException{exceptionCode=[736973746572346A], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=" + Version.getVersion() + "}";
        assertEquals(expected, exception.toString());
    }
    
    @Test
    public void testGetCause() {
        HttpResponse res = new HttpResponse(404, "Not Found", null, null);
        assertEquals("404:The URI requested is invalid or the resource requested, such as a user, does not exists.", TwitterException.getCause(res));
        
        res = new HttpResponse(420, "Rate Limit Exceeded", null, null);
        assertEquals("420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", TwitterException.getCause(res));
        
        res = new HttpResponse(500, "Internal Server Error", null, null);
        assertEquals("500:Something is broken.  Please post to the group so the Twitter team can investigate.", TwitterException.getCause(res));
    }
}