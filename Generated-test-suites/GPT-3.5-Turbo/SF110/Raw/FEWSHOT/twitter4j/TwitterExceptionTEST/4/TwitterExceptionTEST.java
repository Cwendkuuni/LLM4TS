
package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        String msg = "Test Message";
        TwitterException exception = new TwitterException(msg);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionException() {
        String msg = "Test Message";
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.getCause() instanceof Exception);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        String msg = "Test Message";
        MockHttpResponse res = new MockHttpResponse();
        res.setStatusCode(200);
        TwitterException exception = new TwitterException(msg, res);
        assertEquals(msg, exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringException() {
        String msg = "Test Message";
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(msg, cause);
        assertEquals(msg, exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.getCause() instanceof Exception);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        String msg = "Test Message";
        Exception cause = new Exception("Test Cause");
        int statusCode = 200;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        assertEquals(msg, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.getCause() instanceof Exception);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 200;
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = statusCode;
        assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test Message");
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test Message");
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testGetRetryAfter() {
        int statusCode = 420;
        int retryAfter = 60;
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = statusCode;
        exception.retryAfter = retryAfter;
        assertEquals(retryAfter, exception.getRetryAfter());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWithInvalidStatusCode() {
        int statusCode = 200;
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = statusCode;
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        Exception cause = new IOException("Test IO Exception");
        TwitterException exception = new TwitterException("Test Message");
        exception.initCause(cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        int statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = statusCode;
        exception.rateLimitStatus = rateLimitStatus;
        assertTrue(exception.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithInvalidStatusCode() {
        int statusCode = 200;
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = statusCode;
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        int statusCode = 404;
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = statusCode;
        assertTrue(exception.resourceNotFound());
    }

    @Test
    public void testResourceNotFoundWithInvalidStatusCode() {
        int statusCode = 200;
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = statusCode;
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = 400;
        assertEquals("400:", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.nested);
        exception.setNested();
        assertTrue(exception.nested);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message");
        TwitterException exception2 = new TwitterException("Test Message");
        assertTrue(exception1.equals(exception2));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = 200;
        exception.retryAfter = 60;
        assertEquals(7720, exception.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = 200;
        exception.retryAfter = 60;
        assertEquals("TwitterException{statusCode=200, retryAfter=60, rateLimitStatus=null, version=null}", exception.toString());
    }

    private static class MockHttpResponse implements HttpResponse {
        private int statusCode;

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public int getStatusCode() {
            return statusCode;
        }

        @Override
        public String getResponseHeader(String name) {
            return null;
        }

        @Override
        public InputStream asStream() throws IOException {
            return null;
        }

        @Override
        public Reader asReader() throws IOException {
            return null;
        }

        @Override
        public String asString() throws IOException {
            return null;
        }

        @Override
        public void disconnect() {

        }
    }
}
