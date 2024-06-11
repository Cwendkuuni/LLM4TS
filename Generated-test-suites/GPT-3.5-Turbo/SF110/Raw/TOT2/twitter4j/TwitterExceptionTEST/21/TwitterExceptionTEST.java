import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterExceptionTest {
    
    @Test
    public void testConstructorMsg(){
        TwitterException exception = new TwitterException("Test Exception Message");
        assertEquals("Test Exception Message", exception.getMessage());
    }
    
    @Test
    public void testConstructorCause(){
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    public void testConstructorMsgRes(){
        HttpResponse res = new HttpResponseImpl();
        TwitterException exception = new TwitterException("Test Exception Message", res);
        assertEquals("Test Exception Message", exception.getMessage());
    }
    
    @Test
    public void testConstructorMsgCause(){
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception Message", cause);
        assertEquals("Test Exception Message", exception.getMessage());
    }
    
    @Test
    public void testConstructorMsgCauseStatusCode(){
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception Message", cause, 404);
        assertEquals(404, exception.getStatusCode());
    }
    
    @Test
    public void testGetStatusCode(){
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals(-1, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus(){
        TwitterException exception = new TwitterException("Test Exception");
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus(){
        TwitterException exception = new TwitterException("Test Exception");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter(){
        TwitterException exception = new TwitterException("Test Exception", new Exception(), 420);
        assertEquals(420, exception.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue(){
        TwitterException exception = new TwitterException("Test Exception", new IOException());
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation(){
        TwitterException exception = new TwitterException("Test Exception", new Exception(), 420);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound(){
        TwitterException exception = new TwitterException("Test Exception", new Exception(), 404);
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode(){
        TwitterException exception = new TwitterException("Test Exception");
        assertNotNull(exception.getExceptionCode());
    }
    
    @Test
    public void testEquals(){
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertTrue(exception1.equals(exception2));
    }
    
    @Test
    public void testHashCode(){
        TwitterException exception = new TwitterException("Test Exception", new Exception(), 400);
        assertEquals(100000, exception.hashCode());
    }
    
    @Test
    public void testToString(){
        TwitterException exception = new TwitterException("Test Exception", new IOException());
        assertNotNull(exception.toString());
    }
    
    @Test
    public void testSetNested(){
        TwitterException exception = new TwitterException("Test Exception");
        exception.setNested();
        assertTrue(exception.nested);
    }
    
    @Test
    public void testGetCause(){
        HttpResponse res = new HttpResponseImpl();
        assertEquals("200:", TwitterException.getCause(res));
    }
}