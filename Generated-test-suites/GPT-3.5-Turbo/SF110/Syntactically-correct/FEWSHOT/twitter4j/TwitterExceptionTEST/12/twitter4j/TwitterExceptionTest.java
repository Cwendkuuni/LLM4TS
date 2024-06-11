package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionConstructorWithMsg() {
        TwitterException te = new TwitterException("Test message");
        assertEquals("Test message", te.getMessage());
        assertEquals(-1, te.getStatusCode());
        assertNull(te.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithCause() {
        Exception cause = new Exception();
        TwitterException te = new TwitterException(cause);
        assertEquals(cause, te.getCause());
        assertEquals(-1, te.getStatusCode());
        assertNull(te.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithMsgAndRes() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.setResponseHeader("Retry-After", "60");
        TwitterException te = new TwitterException("Test message", res);
        assertEquals("Test message", te.getMessage());
        assertEquals(420, te.getStatusCode());
        assertEquals(60, te.getRetryAfter());
        assertNull(te.getRateLimitStatus());
        assertNull(te.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithMsgAndCause() {
        Exception cause = new Exception();
        TwitterException te = new TwitterException("Test message", cause);
        assertEquals("Test message", te.getMessage());
        assertEquals(cause, te.getCause());
        assertEquals(-1, te.getStatusCode());
        assertNull(te.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new Exception();
        TwitterException te = new TwitterException("Test message", cause, 400);
        assertEquals("Test message", te.getMessage());
        assertEquals(cause, te.getCause());
        assertEquals(400, te.getStatusCode());
        assertNull(te.getRateLimitStatus());
        assertNull(te.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException te = new TwitterException("Test message");
        assertEquals(-1, te.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException te = new TwitterException("Test message");
        assertNull(te.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException te = new TwitterException("Test message");
        assertNull(te.getFeatureSpecificRateLimitStatus());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeNot420() {
        TwitterException te = new TwitterException("Test message");
        te.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException te = new TwitterException("Test message");
        assertFalse(te.isCausedByNetworkIssue());
        te = new TwitterException(new IOException());
        assertTrue(te.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException te = new TwitterException("Test message");
        assertFalse(te.exceededRateLimitation());
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        te = new TwitterException("Test message", res);
        assertTrue(te.exceededRateLimitation());
        res.setStatusCode(420);
        te = new TwitterException("Test message", res);
        assertTrue(te.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException te = new TwitterException("Test message");
        assertFalse(te.resourceNotFound());
        HttpResponse res = new HttpResponse();
        res.setStatusCode(404);
        te = new TwitterException("Test message", res);
        assertTrue(te.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException te = new TwitterException("Test message");
        assertEquals("", te.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException te1 = new TwitterException("Test message");
        TwitterException te2 = new TwitterException("Test message");
        assertTrue(te1.equals(te1));
        assertFalse(te1.equals(null));
        assertFalse(te1.equals(new Object()));
        assertTrue(te1.equals(te2));
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        te1 = new TwitterException("Test message", res);
        te2 = new TwitterException("Test message", res);
        assertTrue(te1.equals(te2));
        te2 = new TwitterException("Different message", res);
        assertFalse(te1.equals(te2));
        te2 = new TwitterException("Test message", new HttpResponse());
        assertFalse(te1.equals(te2));
    }

    @Test
    public void testHashCode() {
        TwitterException te = new TwitterException("Test message");
        assertEquals(0, te.hashCode());
        HttpResponse res = new HttpResponse();
        res.setStatusCode(400);
        te = new TwitterException("Test message", res);
        assertEquals(400, te.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException te = new TwitterException("Test message");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}", te.toString());
    }

    @Test
    public void testSetNested() {
        TwitterException te = new TwitterException("Test message");
        assertFalse(te.nested);
        te.setNested();
        assertTrue(te.nested);
    }
}