import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        TwitterException exception = new TwitterException("Testing constructor with message");
        assertNotNull(exception);
        assertEquals("Testing constructor with message", exception.getMessage());
    }

    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Testing cause");
        TwitterException exception = new TwitterException(cause);
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndHttpResponse() {
        HttpResponseMock response = new HttpResponseMock(400);
        response.setResponseHeader("Retry-After", "60");
        TwitterException exception = new TwitterException("Testing constructor with message and HttpResponse", response);
        assertNotNull(exception);
        assertEquals("Testing constructor with message and HttpResponse\n400:The request was invalid. " +
                "An accompanying error message will explain why. This is the status code will be returned during rate limiting.",
                exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        Exception cause = new Exception("Testing cause");
        TwitterException exception = new TwitterException("Testing constructor with message and cause", cause);
        assertNotNull(exception);
        assertEquals("Testing constructor with message and cause", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        Exception cause = new Exception("Testing cause");
        TwitterException exception = new TwitterException("Testing constructor with message, cause, and status code", cause, 500);
        assertNotNull(exception);
        assertEquals("Testing constructor with message, cause, and status code", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Testing getStatusCode");
        exception.statusCode = 200;
        assertEquals(200, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Testing getRateLimitStatus");
        assertNull(exception.getRateLimitStatus());
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        exception.rateLimitStatus = rateLimitStatus;
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Testing getFeatureSpecificRateLimitStatus");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenStatusCodeIsNot420() {
        TwitterException exception = new TwitterException("Testing getRetryAfter");
        exception.getStatusCode(); // Set status code to a value other than 420
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Testing isCausedByNetworkIssue");
        assertFalse(exception.isCausedByNetworkIssue());
        exception = new TwitterException(new IOException("Testing network issue"));
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Testing exceededRateLimitation");
        // statusCode = 400 and rateLimitStatus = null
        assertFalse(exception.exceededRateLimitation());
        exception.statusCode = 400;
        exception.rateLimitStatus = new RateLimitStatus();
        assertTrue(exception.exceededRateLimitation());

        exception.statusCode = 420;
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Testing resourceNotFound");
        assertFalse(exception.resourceNotFound());
        exception.statusCode = 404;
        assertTrue(exception.resourceNotFound());
    }

    private static class HttpResponseMock implements HttpResponse {
        private int statusCode;
        private String responseHeader;

        public HttpResponseMock(int statusCode) {
            this.statusCode = statusCode;
        }

        public void setResponseHeader(String name, String value) {
            this.responseHeader = name + ": " + value;
        }

        @Override
        public int getStatusCode() {
            return statusCode;
        }

        @Override
        public String getResponseHeader(String name) {
            return responseHeader;
        }

        // Other methods from HttpResponse interface are not needed for testing purposes
    }
}