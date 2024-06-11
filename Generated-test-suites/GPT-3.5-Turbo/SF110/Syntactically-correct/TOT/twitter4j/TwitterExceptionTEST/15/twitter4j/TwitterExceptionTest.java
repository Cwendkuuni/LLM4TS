import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import twitter4j.internal.http.HttpResponse;

import java.io.IOException;


public class TwitterExceptionTest {

    private TwitterException exception;
    private String msg = "Test message";
    private Exception cause = new Exception("Test Exception");
    private int statusCode = 200;
    private int retryAfter = 30;
    private RateLimitStatus rateLimitStatus = new RateLimitStatus();
    private RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();

    @Before
    public void setUp() {
        exception = new TwitterException(msg);
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        exception.rateLimitStatus = rateLimitStatus;
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
    }

    @Test
    public void testTwitterExceptionWithMessage() {
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        TwitterException twitterException = new TwitterException(cause);
        assertEquals(cause, twitterException.getCause());
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionWithMessageAndResponse() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(statusCode);
        response.setResponseHeader("Retry-After", Integer.toString(retryAfter));
        TwitterException twitterException = new TwitterException(msg, response);
        assertEquals(msg + "\n" + twitterException.getCause(), twitterException.getMessage());
        assertEquals(statusCode, twitterException.getStatusCode());
        assertEquals(retryAfter, twitterException.getRetryAfter());
        assertEquals(rateLimitStatus, twitterException.getRateLimitStatus());
        assertEquals(featureSpecificRateLimitStatus, twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionWithMessageAndCauseAndStatusCode() {
        TwitterException twitterException = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, twitterException.getMessage());
        assertEquals(cause, twitterException.getCause());
        assertEquals(statusCode, twitterException.getStatusCode());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(exception.isCausedByNetworkIssue());
        exception.initCause(new IOException());
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        assertFalse(exception.exceededRateLimitation());
        exception.statusCode = 400;
        assertTrue(exception.exceededRateLimitation());
        exception.rateLimitStatus = new RateLimitStatus();
        assertTrue(exception.exceededRateLimitation());
        exception.statusCode = 420;
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        assertFalse(exception.resourceNotFound());
        exception.statusCode = 404;
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        assertFalse(exception.nested);
        exception.setNested();
        assertTrue(exception.nested);
    }

    @Test
    public void testEquals() {
        assertTrue(exception.equals(exception));
        assertFalse(exception.equals(null));
        assertFalse(exception.equals(new Object()));
        
        TwitterException otherException = new TwitterException(msg);
        assertFalse(exception.equals(otherException));
        assertFalse(otherException.equals(exception));
        
        otherException.rateLimitStatus = rateLimitStatus;
        assertFalse(exception.equals(otherException));
        assertFalse(otherException.equals(exception));
       
        otherException.statusCode = statusCode;
        assertFalse(exception.equals(otherException));
        assertFalse(otherException.equals(exception));
        
        otherException.retryAfter = retryAfter;
        assertFalse(exception.equals(otherException));
        assertFalse(otherException.equals(exception));
        
        otherException.rateLimitStatus = null;
        assertTrue(exception.equals(otherException));
        assertTrue(otherException.equals(exception));
    }

    @Test
    public void testHashCode() {
        assertNotNull(exception.hashCode());
    }

    @Test
    public void testToString() {
        assertNotNull(exception.toString());
    }
}