import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    private TwitterException twitterException;

    @Before
    public void setUp() {
        twitterException = new TwitterException("Test Exception");
    }

    @Test
    public void testConstructorWithMsg() {
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.isCausedByNetworkIssue());
        assertFalse(twitterException.exceededRateLimitation());
        assertFalse(twitterException.resourceNotFound());
        assertEquals("", twitterException.getExceptionCode());
    }

    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException ex = new TwitterException(cause);
        assertSame(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertEquals("", ex.getExceptionCode());
    }

    @Test
    public void testConstructorWithMsgAndHttpResponse() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.addResponseHeader("Retry-After", "60");

        TwitterException ex = new TwitterException("Test Exception", res);
        assertEquals("Test Exception", ex.getMessage());
        assertEquals(420, ex.getStatusCode());
        assertEquals(60, ex.getRetryAfter());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertEquals("", ex.getExceptionCode());
    }

    @Test
    public void testConstructorWithMsgAndCause() {
        Exception cause = new Exception("Test Cause");
        TwitterException ex = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", ex.getMessage());
        assertSame(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertFalse(ex.resourceNotFound());
        assertEquals("", ex.getExceptionCode());
    }

    @Test
    public void testConstructorWithMsgCauseAndStatusCode() {
        Exception cause = new Exception("Test Cause");
        TwitterException ex = new TwitterException("Test Exception", cause, 404);
        assertEquals("Test Exception", ex.getMessage());
        assertSame(cause, ex.getCause());
        assertEquals(404, ex.getStatusCode());
        assertNull(ex.getRateLimitStatus());
        assertNull(ex.getFeatureSpecificRateLimitStatus());
        assertFalse(ex.isCausedByNetworkIssue());
        assertFalse(ex.exceededRateLimitation());
        assertTrue(ex.resourceNotFound());
        assertEquals("", ex.getExceptionCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeNot420() {
        TwitterException ex = new TwitterException("Test Exception");
        ex.getRetryAfter();
    }

    @Test
    public void testEqualsMethod() {
        TwitterException ex1 = new TwitterException("Test Exception");
        TwitterException ex2 = new TwitterException("Test Exception");
        assertEquals(ex1, ex2);
    }

    @Test
    public void testEqualsMethodWithDifferentRetryAfter() {
        HttpResponse res = new HttpResponse();
        res.setStatusCode(420);
        res.addResponseHeader("Retry-After", "60");
        TwitterException ex1 = new TwitterException("Test Exception", res);
        TwitterException ex2 = new TwitterException("Test Exception", res);
        assertEquals(ex1, ex2);
        assertEquals(60, ex2.getRetryAfter());
    }

    @Test
    public void testHashCodeMethod() {
        TwitterException ex1 = new TwitterException("Test Exception");
        TwitterException ex2 = new TwitterException("Test Exception");
        assertEquals(ex1.hashCode(), ex2.hashCode());
    }

    @Test
    public void testToStringMethod() {
        TwitterException ex = new TwitterException("Test Exception");
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=version}";
        assertEquals(expected, ex.toString());
    }

    @Test
    public void testSetNestedMethod() {
        TwitterException ex = new TwitterException("Test Exception");
        assertFalse(ex.nested);
        ex.setNested();
        assertTrue(ex.nested);
    }
}

