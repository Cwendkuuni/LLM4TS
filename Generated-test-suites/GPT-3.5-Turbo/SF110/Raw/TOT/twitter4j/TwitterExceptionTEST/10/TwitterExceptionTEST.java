
import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithString() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNotNull(exception);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithException() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMessageAndResponse() {
        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        
        TwitterException exception = new TwitterException("Test Exception", response);
        assertNotNull(exception);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNotNull(exception.getRateLimitStatus());
        assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMessageAndException() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertNotNull(exception);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMessageExceptionAndStatusCode() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception", cause, 500);
        assertNotNull(exception);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals(-1, exception.getStatusCode());
        exception = new TwitterException("Test Exception", null, 500);
        assertEquals(500, exception.getStatusCode());
        exception = new TwitterException("Test Exception", null);
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNull(exception.getRateLimitStatus());
        exception = new TwitterException("Test Exception", null, 500);
        assertNull(exception.getRateLimitStatus());
        exception = new TwitterException("Test Exception", null);
        assertNull(exception.getRateLimitStatus());

        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("Test Exception", response);
        assertNotNull(exception.getRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test Exception");
        exception.getRetryAfter();

        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        response.setResponseHeader("Retry-After", "60");
        exception = new TwitterException("Test Exception", response);
        assertEquals(60, exception.getRetryAfter());

        response.setStatusCode(400);
        exception = new TwitterException("Test Exception", response);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.isCausedByNetworkIssue());

        Exception cause = new IOException();
        exception = new TwitterException("Test Exception", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.exceededRateLimitation());

        HttpResponse response = new HttpResponseImpl();
        exception = new TwitterException("Test Exception", response);
        assertFalse(exception.exceededRateLimitation());

        response.setStatusCode(400);
        exception = new TwitterException("Test Exception", response);
        assertTrue(exception.exceededRateLimitation());

        exception = new TwitterException("Test Exception", null, 420);
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Exception");
        assertFalse(exception.resourceNotFound());

        exception = new TwitterException("Test Exception", null, 404);
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNotEquals("", exception.getExceptionCode());
        assertNotNull(exception.getExceptionCode());

        Exception cause = new IOException();
        exception = new TwitterException("Test Exception", cause);
        assertNotEquals("", exception.getExceptionCode());
        assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertTrue(exception1.equals(exception2));

        HttpResponse response = new HttpResponseImpl();
        TwitterException exception3 = new TwitterException("Test Exception", response);
        assertFalse(exception1.equals(exception3));
    }

    @Test
    public void testHashCode() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertEquals(exception1.hashCode(), exception2.hashCode());

        HttpResponse response = new HttpResponseImpl();
        response.setStatusCode(420);
        TwitterException exception3 = new TwitterException("Test Exception", response);
        assertNotEquals(exception1.hashCode(), exception3.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        String toString = exception.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("TwitterException"));
    }

    private class HttpResponseImpl implements HttpResponse {
        private int statusCode;
        private String responseHeader;

        @Override
        public int getStatusCode() {
            return statusCode;
        }

        @Override
        public String getResponseHeader(String name) {
            return responseHeader;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public void setResponseHeader(String name, String value) {
            this.responseHeader = value;
        }
    }
}
