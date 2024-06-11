import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithString() {
        String msg = "Test Error Message";
        TwitterException twitterException = new TwitterException(msg);

        assertNotNull(twitterException);
        assertEquals(msg, twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.nested);
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testConstructorWithException() {
        Exception ex = new Exception("Test Cause");
        TwitterException twitterException = new TwitterException(ex);

        assertNotNull(twitterException);
        assertEquals(ex.getMessage(), twitterException.getCause().getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.nested);
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testConstructorWithStringAndHttpResponse() {
        String msg = "Test Error Message";
        HttpResponse httpResponse = new HttpResponseImpl();

        TwitterException twitterException = new TwitterException(msg, httpResponse);

        assertNotNull(twitterException);
        assertEquals(httpResponse.getStatusCode(), twitterException.getStatusCode());
        assertNotNull(twitterException.getRateLimitStatus());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.nested);
    }

    @Test
    public void testConstructorWithStringAndException() {
        String msg = "Test Error Message";
        Exception ex = new Exception("Test Cause");

        TwitterException twitterException = new TwitterException(msg, ex);

        assertNotNull(twitterException);
        assertEquals(ex.getMessage(), twitterException.getCause().getMessage());
        assertEquals(-1, twitterException.getStatusCode());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.nested);
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testConstructorWithStringExceptionAndStatusCode() {
        String msg = "Test Error Message";
        Exception ex = new Exception("Test Cause");
        int statusCode = 404;

        TwitterException twitterException = new TwitterException(msg, ex, statusCode);

        assertNotNull(twitterException);
        assertEquals(ex.getMessage(), twitterException.getCause().getMessage());
        assertEquals(statusCode, twitterException.getStatusCode());
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
        assertFalse(twitterException.nested);
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 500;

        TwitterException twitterException = new TwitterException("Test", null, statusCode);

        assertEquals(statusCode, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test");

        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test");

        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test");

        twitterException.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        Exception ex = new IOException("Network Issue");

        TwitterException twitterException = new TwitterException(ex);

        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException1 = new TwitterException("Test");
        TwitterException twitterException2 = new TwitterException("Test", null, 400);
        twitterException2.rateLimitStatus = new RateLimitStatusJSONImpl();

        assertFalse(twitterException1.exceededRateLimitation());
        assertTrue(twitterException2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test", null, 404);

        assertTrue(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException twitterException = new TwitterException("Test");

        assertNotNull(twitterException.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test");
        TwitterException twitterException2 = new TwitterException("Test");
        twitterException2.statusCode = 500;

        assertTrue(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test");

        assertNotNull(twitterException.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException twitterException = new TwitterException("Test");

        assertNotNull(twitterException.toString());
    }

    private static class HttpResponseImpl implements HttpResponse {

        @Override
        public int getStatusCode() {
            return 200;
        }

        @Override
        public RateLimitStatus getRateLimitStatus() {
            return null;
        }

        @Override
        public RateLimitStatus getFeatureSpecificRateLimitStatus() {
            return null;
        }

        @Override
        public String getResponseHeader(String name) {
            return null;
        }

        @Override
        public void disconnect() throws IOException {

        }

        @Override
        public InputStream asStream() throws TwitterException {
            return null;
        }

        @Override
        public String asString() throws TwitterException {
            return null;
        }

        @Override
        public String getResponseURL() {
            return null;
        }

        @Override
        public void setResponseHeader(String name, String value) {

        }
    }
}