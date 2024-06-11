import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.internal.http.HttpResponse;

import org.junit.Before;
import org.junit.Test;

public class TwitterExceptionTest {

    private TwitterException exception;

    @Before
    public void setUp() {
        exception = new TwitterException("Test message");
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertEquals(null, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertEquals(null, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        try {
            exception.getRetryAfter();
        } catch (IllegalStateException e) {
            assertEquals("Rate limitation is not exceeded", e.getMessage());
        }
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        assertEquals("expected", exception.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException otherException = new TwitterException("Test message");
        assertFalse(exception.equals(null));
        assertTrue(exception.equals(exception));
        assertTrue(exception.equals(otherException));
    }

    @Test
    public void testHashCode() {
        assertEquals(961, exception.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}";
        assertEquals(expected, exception.toString());
    }

    @Test
    public void testGetCause() {
        HttpResponse response = mock(HttpResponse.class);
        when(response.getStatusCode()).thenReturn(400);
        when(response.getResponseHeader("Retry-After")).thenReturn("60");
        String expected = "400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
        assertEquals(expected, TwitterException.getCause(response));
    }

    @Test
    public void testSetNested() {
        exception.setNested();
        assertTrue(exception.isNested());
    }

    private boolean isNested() {
        return exception.isNested();
    }

}