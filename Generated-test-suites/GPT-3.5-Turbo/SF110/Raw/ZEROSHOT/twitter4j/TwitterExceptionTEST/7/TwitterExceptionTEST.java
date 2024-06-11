import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionConstructor() {
        TwitterException twitterException = new TwitterException("Test");
        assertEquals("Test", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithException() {
        Exception exception = new Exception("Test Exception");
        TwitterException twitterException = new TwitterException(exception);
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertTrue(exception instanceof TwitterException);
        assertTrue(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithHttpResponse() {
        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(200);
        httpResponse.setResponseHeader("Retry-After", "60");
        TwitterException twitterException = new TwitterException("Test", httpResponse);
        assertEquals("Test", twitterException.getMessage());
        assertEquals(200, twitterException.getStatusCode());
        assertEquals(60, twitterException.getRetryAfter());
        assertNotNull(twitterException.getRateLimitStatus());
        assertNotNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithExceptionAndStatusCode() {
        Exception exception = new Exception("Test Exception");
        TwitterException twitterException = new TwitterException("Test", exception, 400);
        assertEquals("Test", twitterException.getMessage());
        assertEquals(400, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertTrue(exception instanceof TwitterException);
        assertTrue(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test");
        assertNotNull(twitterException.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException twitterException = new TwitterException("Test");
        twitterException.setNested();
        assertTrue(twitterException.equals(twitterException));
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test");
        TwitterException twitterException2 = new TwitterException("Test");
        assertTrue(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test");
        assertNotNull(twitterException.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test");
        assertNotNull(twitterException.toString());
    }
}