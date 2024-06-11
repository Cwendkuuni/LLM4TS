import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {
    
    @Test
    public void testTwitterExceptionMsg() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals("Test message", twitterException.getMessage());
    }
    
    @Test
    public void testTwitterExceptionCause() {
        Exception cause = new Exception("Test cause");
        TwitterException twitterException = new TwitterException(cause);
        assertEquals(cause, twitterException.getCause());
    }
    
    @Test
    public void testTwitterExceptionMsgRes() {
        HttpResponse res = new HttpResponseImpl();
        TwitterException twitterException = new TwitterException("Test message", res);
        assertEquals("Test message", twitterException.getMessage());
        assertEquals(res, twitterException.getResponse());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals(-1, twitterException.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNull(twitterException.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test message");
        assertThrows(IllegalStateException.class, () -> twitterException.getRetryAfter());
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        IOException ioException = new IOException();
        TwitterException twitterException = new TwitterException("Test message", ioException);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException = new TwitterException("Test message");
        assertFalse(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test message");
        assertFalse(twitterException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNotNull(twitterException.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test message");
        TwitterException twitterException2 = new TwitterException("Test message");
        assertTrue(twitterException1.equals(twitterException2));
    }
    
    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals(31 * (31 * 31 * -1), twitterException.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test message");
        assertNotNull(twitterException.toString());
    }
}