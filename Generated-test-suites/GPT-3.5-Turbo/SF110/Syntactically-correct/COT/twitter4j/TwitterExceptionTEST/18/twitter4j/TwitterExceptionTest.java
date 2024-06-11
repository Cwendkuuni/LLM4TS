import org.junit.Test;
import twitter4j.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMessage() {
        TwitterException e = new TwitterException("Test Exception");
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getCause());
        assertEquals(-1, e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Test Exception");
        TwitterException e = new TwitterException(cause);
        assertEquals(cause, e.getCause());
        assertEquals(-1, e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageAndResponse() {
        HttpResponse res = new HttpResponseImpl();
        res.setStatusCode(200);
        res.setResponseHeader("Retry-After", "10");

        TwitterException e = new TwitterException("Test Exception", res);
        assertEquals("Test Exception", e.getMessage());
        assertNull(e.getCause());
        assertEquals(200, e.getStatusCode());
        assertEquals(10, e.getRetryAfter());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCause() {
        Exception cause = new Exception("Test Exception");
        TwitterException e = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", e.getMessage());
        assertEquals(cause, e.getCause());
        assertEquals(-1, e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertFalse(e.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Test Exception");
        TwitterException e = new TwitterException("Test Exception", cause, 404);
        assertEquals("Test Exception", e.getMessage());
        assertEquals(cause, e.getCause());
        assertEquals(404, e.getStatusCode());
        assertNull(e.getRateLimitStatus());
        assertNull(e.getFeatureSpecificRateLimitStatus());
        assertFalse(e.isCausedByNetworkIssue());
        assertFalse(e.exceededRateLimitation());
        assertTrue(e.resourceNotFound());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException e = new TwitterException("Test Exception");
        assertEquals(-1, e.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException e = new TwitterException("Test Exception");
        assertNull(e.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException e = new TwitterException("Test Exception");
        assertNull(e.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException e = new TwitterException("Test Exception");
        try {
            e.getRetryAfter();
            fail();
        } catch (IllegalStateException ex) {
            assertEquals("Rate limitation is not exceeded", ex.getMessage());
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException e1 = new TwitterException("Test Exception");
        TwitterException e2 = new TwitterException(new IOException("Test IO Exception"));

        assertFalse(e1.isCausedByNetworkIssue());
        assertTrue(e2.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        HttpResponse res1 = new HttpResponseImpl();
        res1.setStatusCode(400);

        HttpResponse res2 = new HttpResponseImpl();
        res2.setStatusCode(420);

        TwitterException e1 = new TwitterException("Test Exception", res1);
        TwitterException e2 = new TwitterException("Test Exception", res2);

        assertFalse(e1.exceededRateLimitation());
        assertTrue(e2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException e = new TwitterException("Test Exception", new Exception(), 404);
        assertTrue(e.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException e = new TwitterException("Test Exception");
        String exceptionCode = e.getExceptionCode();
        assertNotNull(exceptionCode);
    }

    @Test
    public void testEquals() {
        TwitterException e1 = new TwitterException("Test Exception");
        TwitterException e2 = new TwitterException("Test Exception");
        TwitterException e3 = new TwitterException("Test Exception");

        e2.setNested();
        e3.setNested();

        assertTrue(e1.equals(e1));
        assertTrue(e1.equals(e2));
        assertFalse(e1.equals(e3));
        assertFalse(e1.equals(null));
        assertFalse(e1.equals("Test Exception"));

        e2.setRateLimitStatus(new RateLimitStatusJSONImpl());
        assertFalse(e1.equals(e2));

        e1.setRateLimitStatus(new RateLimitStatusJSONImpl());
        assertTrue(e1.equals(e2));

        e2.setRetryAfter(10);
        assertFalse(e1.equals(e2));

        e1.setRetryAfter(10);
        e1.setFeatureSpecificRateLimitStatus(new RateLimitStatusJSONImpl());
        assertTrue(e1.equals(e2));
    }

    @Test
    public void testHashCode() {
        TwitterException e1 = new TwitterException("Test Exception");
        TwitterException e2 = new TwitterException("Test Exception");

        assertEquals(e1.hashCode(), e1.hashCode());
        assertEquals(e1.hashCode(), e2.hashCode());

        e1.setRetryAfter(10);
        assertNotEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException e = new TwitterException("Test Exception");
        String exceptionString = e.toString();
        assertNotNull(exceptionString);
    }

}