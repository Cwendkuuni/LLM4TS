import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import twitter4j.http.HttpResponse;

public class TwitterExceptionTest {

    private TwitterException twitterException;
    private HttpResponse httpResponse;

    @Before
    public void setUp() {
        twitterException = new TwitterException("Test");
        httpResponse = new HttpResponse();
    }

    @Test
    public void testTwitterExceptionString() {
        assertEquals("Test", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.nested);
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        httpResponse.setStatusCode(420);
        httpResponse.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Test", httpResponse);
        assertEquals("Test", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test", cause);
        assertEquals("Test", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new Exception();
        TwitterException exception = new TwitterException("Test", cause, 500);
        assertEquals("Test", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.nested);
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(-1, twitterException.getStatusCode());
        httpResponse.setStatusCode(200);
        TwitterException exception = new TwitterException("Test", httpResponse);
        assertEquals(200, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        twitterException.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());
        Exception cause = new Exception(new IOException());
        TwitterException exception = new TwitterException(cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        assertFalse(twitterException.exceededRateLimitation());
        httpResponse.setStatusCode(400);
        TwitterException exception = new TwitterException("Test", httpResponse);
        assertTrue(exception.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        assertFalse(twitterException.resourceNotFound());
        httpResponse.setStatusCode(404);
        TwitterException exception = new TwitterException("Test", httpResponse);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        String exceptionCode = twitterException.getExceptionCode();
        assertNotNull(exceptionCode);
    }

    @Test
    public void testSetNested() {
        assertFalse(twitterException.nested);
        twitterException.setNested();
        assertTrue(twitterException.nested);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test");
        TwitterException exception2 = new TwitterException("Test");
        assertTrue(exception1.equals(exception2));

        exception1 = new TwitterException("Test1");
        exception2 = new TwitterException("Test2");
        assertFalse(exception1.equals(exception2));

        exception1 = new TwitterException("Test");
        exception2 = new TwitterException("Test");
        exception1.setNested();
        assertFalse(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test");
        TwitterException exception2 = new TwitterException("Test");
        assertEquals(exception1.hashCode(), exception2.hashCode());

        exception1.setNested();
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
    }

    @Test
    public void testToString() {
        assertNotNull(twitterException.toString());
    }

    @Test
    public void testGetCause() {
        String cause = TwitterException.getCause(new HttpResponse(400, "Bad Request"));
        assertEquals("400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", cause);
    }
}