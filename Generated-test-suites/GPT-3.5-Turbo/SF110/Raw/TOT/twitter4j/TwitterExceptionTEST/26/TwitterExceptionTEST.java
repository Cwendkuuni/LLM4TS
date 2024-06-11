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
    public void testConstructorWithMessage() {
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithCause() {
        Exception exception = new Exception("Test Cause");
        twitterException = new TwitterException(exception);
        assertEquals("Test Cause", twitterException.getCause().getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(420);
        httpResponse.addResponseHeader("Retry-After", "20");

        twitterException = new TwitterException("Test Exception", httpResponse);
        assertEquals("Test Exception\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", twitterException.getMessage());
        assertEquals(420, twitterException.getStatusCode());
        assertEquals(20, twitterException.getRetryAfter());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        Exception exception = new Exception("Test Cause");

        twitterException = new TwitterException("Test Exception", exception);
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals(exception, twitterException.getCause());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        Exception exception = new Exception("Test Cause");

        twitterException = new TwitterException("Test Exception", exception, 400);
        assertEquals("Test Exception", twitterException.getMessage());
        assertEquals(exception, twitterException.getCause());
        assertEquals(400, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(-1, twitterException.getStatusCode());
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
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        twitterException.getRetryAfter();
    }

    @Test
    public void testGetRetryAfterWhenStatusCodeIs420() {
        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(420);
        httpResponse.addResponseHeader("Retry-After", "20");

        twitterException = new TwitterException("Test Exception", httpResponse);
        assertEquals(20, twitterException.getRetryAfter());
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());

        Exception exception = new IOException("Test Network Issue");
        twitterException = new TwitterException("Test Exception", exception);

        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        assertFalse(twitterException.exceededRateLimitation());

        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(420);
        twitterException = new TwitterException("Test Exception", httpResponse);

        assertTrue(twitterException.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        assertFalse(twitterException.resourceNotFound());

        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(404);
        twitterException = new TwitterException("Test Exception", httpResponse);

        assertTrue(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        assertEquals("88484", twitterException.getExceptionCode());

        twitterException = new TwitterException("Test Exception twitter4j", new TwitterException("Test Cause twitter4j"));
        assertEquals("8848488484", twitterException.getExceptionCode());

        twitterException = new TwitterException("Test Exception twitter4j http", new HttpResponseImpl());
        assertEquals("884848486481", twitterException.getExceptionCode());
    }

    @Test
    public void testEquals() {
        assertTrue(twitterException.equals(twitterException));

        TwitterException twitterException2 = new TwitterException("Test Exception");
        assertTrue(twitterException.equals(twitterException2));
        assertTrue(twitterException2.equals(twitterException));

        twitterException2 = new TwitterException("Test Exception", new HttpResponseImpl());
        assertFalse(twitterException.equals(twitterException2));
        assertFalse(twitterException2.equals(twitterException));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException2 = new TwitterException("Test Exception");
        assertEquals(twitterException.hashCode(), twitterException2.hashCode());

        twitterException2 = new TwitterException("Test Exception", new HttpResponseImpl());
        assertNotEquals(twitterException.hashCode(), twitterException2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("TwitterException{exceptionCode=[88484], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=}", twitterException.toString());

        HttpResponse httpResponse = new HttpResponseImpl();
        httpResponse.setStatusCode(420);
        twitterException = new TwitterException("Test Exception", httpResponse);
        assertEquals("TwitterException{statusCode=420, retryAfter=0, rateLimitStatus=null, version=}", twitterException.toString());
    }
}