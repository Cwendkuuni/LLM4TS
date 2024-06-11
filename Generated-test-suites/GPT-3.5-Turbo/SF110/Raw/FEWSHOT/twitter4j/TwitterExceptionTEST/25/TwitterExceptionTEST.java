
package twitter4j;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterExceptionTest {

    private TwitterException twitterException;

    @Before
    public void setUp() {
        twitterException = new TwitterException("Test Message");
        twitterException.setStackTrace(new StackTraceElement[]{});
    }

    @Test
    public void testTwitterExceptionConstructor() {
        assertEquals("Test Message", twitterException.getMessage());
        assertNull(twitterException.getCause());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithCause() {
        Exception cause = new Exception("Nested Exception");
        TwitterException twitterExceptionWithCause = new TwitterException("Test Message", cause);

        assertEquals("Test Message", twitterExceptionWithCause.getMessage());
        assertEquals(cause, twitterExceptionWithCause.getCause());
        assertEquals(-1, twitterExceptionWithCause.getStatusCode());
        assertNull(twitterExceptionWithCause.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionConstructorWithMessageAndHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(420);
        httpResponse.setResponseHeader("Retry-After", "60");

        TwitterException twitterExceptionWithHttpResponse = new TwitterException("Test Error Message", httpResponse);

        assertEquals("Test Error Message\n420:The number of requests you have made exceeds the quota afforded by your assigned rate limit.", twitterExceptionWithHttpResponse.getMessage());
        assertNull(twitterExceptionWithHttpResponse.getCause());
        assertEquals(420, twitterExceptionWithHttpResponse.getStatusCode());
        assertNull(twitterExceptionWithHttpResponse.getRateLimitStatus());
        assertEquals(60, twitterExceptionWithHttpResponse.getRetryAfter());
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
    public void testGetRetryAfterException() {
        twitterException.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        assertFalse(twitterException.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        assertFalse(twitterException.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        assertNotNull(twitterException.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test Message");
        TwitterException twitterException2 = new TwitterException("Test Message");

        assertTrue(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        int hashCode = twitterException.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        String toString = twitterException.toString();
        assertNotNull(toString);
    }
}
