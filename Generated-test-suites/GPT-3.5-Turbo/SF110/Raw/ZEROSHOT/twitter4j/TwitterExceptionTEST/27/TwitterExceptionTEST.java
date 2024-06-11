import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructor1() {
        TwitterException ex = new TwitterException("Test");
        assertEquals("Test", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor2() {
        Exception cause = new Exception("Test");
        TwitterException ex = new TwitterException(cause);
        assertEquals("Test", ex.getCause().getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor3() {
        HttpResponse res = new HttpResponseImpl(null);
        TwitterException ex = new TwitterException("Test", res);
        assertEquals("null\nTest", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor4() {
        Exception cause = new Exception("Test");
        TwitterException ex = new TwitterException("Test", cause);
        assertEquals("Test", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructor5() {
        Exception cause = new Exception("Test");
        TwitterException ex = new TwitterException("Test", cause, 200);
        assertEquals("Test", ex.getMessage());
        assertEquals(200, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException ex = new TwitterException("Test");
        assertEquals(-1, ex.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException ex = new TwitterException("Test");
        assertNull(ex.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException ex = new TwitterException("Test");
        assertNull(ex.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException ex = new TwitterException("Test");
        try {
            ex.getRetryAfter();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException ex = new TwitterException("Test");
        assertFalse(ex.isCausedByNetworkIssue());
        Exception cause = new IOException();
        ex = new TwitterException("Test", cause);
        assertTrue(ex.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException ex = new TwitterException("Test");
        assertFalse(ex.exceededRateLimitation());
        ex = new TwitterException("Test", null, 400);
        assertTrue(ex.exceededRateLimitation());
        HttpResponse res = new HttpResponseImpl(null);
        ex = new TwitterException("Test", res);
        assertTrue(ex.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException ex = new TwitterException("Test");
        assertFalse(ex.resourceNotFound());
        ex = new TwitterException("Test", null, 404);
        assertTrue(ex.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException ex = new TwitterException("Test");
        assertEquals("", ex.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException ex = new TwitterException("Test");
        assertFalse(ex.equals(ex));
        ex.setNested();
        assertTrue(ex.equals(ex));
    }

    @Test
    public void testEquals() {
        TwitterException ex1 = new TwitterException("Test");
        TwitterException ex2 = new TwitterException("Test");
        TwitterException ex3 = new TwitterException("Different Test");
        assertFalse(ex1.equals(null));
        assertTrue(ex1.equals(ex1));
        assertTrue(ex1.equals(ex2));
        assertFalse(ex1.equals(ex3));
    }

    @Test
    public void testHashCode() {
        TwitterException ex = new TwitterException("Test");
        assertEquals(0, ex.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException ex = new TwitterException("Test");
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expected, ex.toString());
    }
}