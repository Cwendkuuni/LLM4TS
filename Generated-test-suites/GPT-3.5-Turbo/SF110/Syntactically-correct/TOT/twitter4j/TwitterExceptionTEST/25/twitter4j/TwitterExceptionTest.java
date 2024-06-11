
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMsg() {
        String errorMsg = "This is an error message";
        TwitterException exception = new TwitterException(errorMsg);
        
        assertEquals(errorMsg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }

    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("This is a cause exception");
        TwitterException exception = new TwitterException(cause);

        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }

    @Test
    public void testConstructorWithMsgAndRes() {
        String errorMsg = "This is an error message";
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(404);
        TwitterException exception = new TwitterException(errorMsg, httpResponse);

        assertEquals("404:The URI requested is invalid or the resource requested," +
                " such as a user, does not exists.\nThis is an error message", exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertTrue(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }

    @Test
    public void testConstructorWithMsgAndCause() {
        String errorMsg = "This is an error message";
        Exception cause = new Exception("This is a cause exception");
        TwitterException exception = new TwitterException(errorMsg, cause);

        assertEquals(errorMsg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }

    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        String errorMsg = "This is an error message";
        Exception cause = new Exception("This is a cause exception");
        int statusCode = 500;
        TwitterException exception = new TwitterException(errorMsg, cause, statusCode);

        assertEquals(errorMsg, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
        assertNull(exception.getExceptionCode());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Rate limitation exceeded");
        exception.getStatusCode = 420;
        exception.retryAfter = 60;

        assertEquals(60, exception.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenRateLimitationNotExceeded() {
        TwitterException exception = new TwitterException("Rate limitation not exceeded");

        exception.getRetryAfter();
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Error 1");
        exception1.getStatusCode = 500;
        TwitterException exception2 = new TwitterException("Error 2");
        exception2.getStatusCode = 500;
        TwitterException exception3 = new TwitterException("Error 3");
        exception3.getStatusCode = 200;
        TwitterException exception4 = new TwitterException("Error 4");
        exception4.getStatusCode = 500;

        assertTrue(exception1.equals(exception2));
        assertFalse(exception1.equals(exception3));
        assertFalse(exception1.equals(exception4));
    }
  
    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Error", null, 500);
        exception1.retryAfter = 60;
        TwitterException exception2 = new TwitterException("Error", null, 500);
        exception2.retryAfter = 60;
        
        assertEquals(exception1.hashCode(), exception2.hashCode());
    }
  
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Error");
        String expected = "TwitterException{exceptionCode=[], " +
                "statusCode=-1, retryAfter=0, rateLimitStatus=null, version=0.0.0}";

        assertEquals(expected, exception.toString());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception1 = new TwitterException("Error");
        TwitterException exception2 = new TwitterException("Error", new IOException());

        assertFalse(exception1.isCausedByNetworkIssue());
        assertTrue(exception2.isCausedByNetworkIssue());
    }
}

