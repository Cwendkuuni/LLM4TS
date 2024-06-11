import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructor1() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals("Test message", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor2() {
        Exception cause = new NullPointerException();
        TwitterException ex = new TwitterException(cause);
        assertEquals(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor3() throws HttpResponse {
        HttpResponse res = new HttpResponseImpl(null, null, null, 420, null);
        TwitterException ex = new TwitterException("Test message", res);
        assertEquals("Test message", ex.getMessage());
        assertEquals(420, ex.getStatusCode());
        assertNotNull(ex.getRateLimitStatus());
        assertNotNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor4() {
        Exception cause = new NullPointerException();
        TwitterException ex = new TwitterException("Test message", cause);
        assertEquals("Test message", ex.getMessage());
        assertEquals(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor5() {
        Exception cause = new NullPointerException();
        TwitterException ex = new TwitterException("Test message", cause, 500);
        assertEquals("Test message", ex.getMessage());
        assertEquals(cause, ex.getCause());
        assertEquals(500, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals(-1, ex.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException ex = new TwitterException("Test message");
        assertNull(ex.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException ex = new TwitterException("Test message");
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException ex = new TwitterException("Test message");
        ex.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException ex1 = new TwitterException("Test message");
        assertFalse(ex1.isCausedByNetworkIssue());

        TwitterException ex2 = new TwitterException(new IOException());
        assertTrue(ex2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException ex1 = new TwitterException("Test message");
        assertFalse(ex1.exceededRateLimitation());

        HttpResponse res = new HttpResponseImpl(null, null, null, 400, null);
        TwitterException ex2 = new TwitterException("Test message", res);
        assertTrue(ex2.exceededRateLimitation());

        HttpResponse res2 = new HttpResponseImpl(null, null, null, 420, null);
        TwitterException ex3 = new TwitterException("Test message", res2);
        assertTrue(ex3.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException ex1 = new TwitterException("Test message");
        assertFalse(ex1.resourceNotFound());

        HttpResponse res = new HttpResponseImpl(null, null, null, 404, null);
        TwitterException ex2 = new TwitterException("Test message", res);
        assertTrue(ex2.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals("", ex.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("Test message");
        TwitterException ex2 = new TwitterException("Test message");
        assertEquals(ex1, ex2);
    }

    @Test
    public void testHashCode() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals(ex.hashCode(), ex.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException ex = new TwitterException("Test message");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", ex.toString());
    }

}