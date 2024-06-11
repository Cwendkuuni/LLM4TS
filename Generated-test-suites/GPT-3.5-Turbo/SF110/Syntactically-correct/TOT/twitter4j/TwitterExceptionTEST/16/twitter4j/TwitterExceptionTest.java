import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.HttpResponse;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusJSONImpl;
import java.io.IOException;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        // Typical Use Case
        String message = "Test Message";
        TwitterException exception = new TwitterException(message);
        assertEquals(message, exception.getMessage());

        // Edge Case
        String emptyMessage = "";
        TwitterException emptyException = new TwitterException(emptyMessage);
        assertEquals(emptyMessage, emptyException.getMessage());
    }

    @Test
    public void testConstructorWithCause() {
        // Typical Use Case
        Exception cause = new IOException("Test IOException");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());

        // Error Scenario
        Exception twitterExceptionCause = new TwitterException("Test TwitterException");
        TwitterException twitterException = new TwitterException(twitterExceptionCause);
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testConstructorWithMessageAndHttpResponse() {
        // Typical Use Case
        String message = "Test Message";
        HttpResponse httpResponse = new HttpResponseImpl();
        TwitterException exception = new TwitterException(message, httpResponse);
        assertTrue(exception.getMessage().contains(message));
        assertEquals(httpResponse, exception.getResponse());
        assertEquals(httpResponse.getStatusCode(), exception.getStatusCode());
        assertEquals(RateLimitStatusJSONImpl.createFromResponseHeader(httpResponse), exception.getRateLimitStatus());
        assertEquals(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(httpResponse), exception.getFeatureSpecificRateLimitStatus());

        // Error Scenario
        HttpResponse nullResponse = null;
        TwitterException nullResponseException = new TwitterException(message, nullResponse);
        assertTrue(nullResponseException.getMessage().contains(message));
        assertNull(nullResponseException.getResponse());
        assertEquals(-1, nullResponseException.getStatusCode());
        assertNull(nullResponseException.getRateLimitStatus());
        assertNull(nullResponseException.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        // Typical Use Case
        String message = "Test Message";
        Exception cause = new IOException("Test IOException");
        int statusCode = 500;
        TwitterException exception = new TwitterException(message, cause, statusCode);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(statusCode, exception.getStatusCode());

        // Error Scenario
        int negativeStatusCode = -100;
        TwitterException negativeStatusCodeException = new TwitterException(message, cause, negativeStatusCode);
        assertEquals(message, negativeStatusCodeException.getMessage());
        assertEquals(cause, negativeStatusCodeException.getCause());
        assertEquals(negativeStatusCode, negativeStatusCodeException.getStatusCode());
    }

    @Test
    public void testGetRetryAfter() {
        // Typical Use Case
        HttpResponse httpResponseWithRetryAfter = new HttpResponseImpl();
        httpResponseWithRetryAfter.setStatusCode(420);
        httpResponseWithRetryAfter.setResponseHeader("Retry-After", "60");
        TwitterException exceptionWithRetryAfter = new TwitterException("", httpResponseWithRetryAfter);
        assertEquals(60, exceptionWithRetryAfter.getRetryAfter());

        // Error Scenario
        HttpResponse httpResponseWithoutRetryAfter = new HttpResponseImpl();
        httpResponseWithoutRetryAfter.setStatusCode(200);
        TwitterException exceptionWithoutRetryAfter = new TwitterException("", httpResponseWithoutRetryAfter);
        try {
            exceptionWithoutRetryAfter.getRetryAfter();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testExceededRateLimitation() {
        // Typical Use Case
        HttpResponse httpResponseWithRateLimitStatus = new HttpResponseImpl();
        httpResponseWithRateLimitStatus.setStatusCode(400);
        httpResponseWithRateLimitStatus.setResponseHeader("RateLimitStatus", new RateLimitStatusJSONImpl().toString());
        TwitterException exceptionWithRateLimitStatus = new TwitterException("", httpResponseWithRateLimitStatus);
        assertTrue(exceptionWithRateLimitStatus.exceededRateLimitation());

        // Error Scenarios
        HttpResponse httpResponseWithoutRateLimitStatus = new HttpResponseImpl();
        httpResponseWithoutRateLimitStatus.setStatusCode(400);
        TwitterException exceptionWithoutRateLimitStatus = new TwitterException("", httpResponseWithoutRateLimitStatus);
        assertFalse(exceptionWithoutRateLimitStatus.exceededRateLimitation());

        HttpResponse httpResponseWithStatusCode200 = new HttpResponseImpl();
        httpResponseWithStatusCode200.setStatusCode(200);
        httpResponseWithStatusCode200.setResponseHeader("RateLimitStatus", new RateLimitStatusJSONImpl().toString());
        TwitterException exceptionWithStatusCode200 = new TwitterException("", httpResponseWithStatusCode200);
        assertFalse(exceptionWithStatusCode200.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        // Typical Use Case
        HttpResponse httpResponseNotFound = new HttpResponseImpl();
        httpResponseNotFound.setStatusCode(404);
        TwitterException exceptionNotFound = new TwitterException("", httpResponseNotFound);
        assertTrue(exceptionNotFound.resourceNotFound());

        // Error Scenario
        HttpResponse httpResponseNotNotFound = new HttpResponseImpl();
        httpResponseNotNotFound.setStatusCode(200);
        TwitterException exceptionNotNotFound = new TwitterException("", httpResponseNotNotFound);
        assertFalse(exceptionNotNotFound.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        // Typical Use Case
        TwitterException exception = new TwitterException("");
        // Since FILTER is hardcoded in the class, we can only test that getExceptionCode does not throw any exception.
        assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testEquals() {
        // Typical Use Case
        TwitterException exception1 = new TwitterException("");
        TwitterException exception2 = new TwitterException("");
        assertTrue(exception1.equals(exception2));

        // Error Scenario
        HttpResponse httpResponseWithRateLimitStatus = new HttpResponseImpl();
        httpResponseWithRateLimitStatus.setStatusCode(400);
        httpResponseWithRateLimitStatus.setResponseHeader("RateLimitStatus", new RateLimitStatusJSONImpl().toString());
        TwitterException exceptionWithRateLimitStatus1 = new TwitterException("", httpResponseWithRateLimitStatus);
        TwitterException exceptionWithRateLimitStatus2 = new TwitterException("", httpResponseWithRateLimitStatus);
        assertTrue(exceptionWithRateLimitStatus1.equals(exceptionWithRateLimitStatus2));

        HttpResponse httpResponseWithRateLimitStatusNull = new HttpResponseImpl();
        httpResponseWithRateLimitStatusNull.setStatusCode(400);
        httpResponseWithRateLimitStatusNull.setResponseHeader("RateLimitStatus", null);
        TwitterException exceptionWithRateLimitStatusNull1 = new TwitterException("", httpResponseWithRateLimitStatusNull);
        TwitterException exceptionWithRateLimitStatusNull2 = new TwitterException("", httpResponseWithRateLimitStatusNull);
        assertFalse(exceptionWithRateLimitStatusNull1.equals(exceptionWithRateLimitStatusNull2));
    }

    @Test
    public void testHashCode() {
        // Typical Use Case
        TwitterException exception1 = new TwitterException("");
        TwitterException exception2 = new TwitterException("");
        assertEquals(exception1.hashCode(), exception2.hashCode());

        // Error Scenario
        HttpResponse httpResponseWithRateLimitStatus = new HttpResponseImpl();
        httpResponseWithRateLimitStatus.setStatusCode(400);
        httpResponseWithRateLimitStatus.setResponseHeader("RateLimitStatus", new RateLimitStatusJSONImpl().toString());
        TwitterException exceptionWithRateLimitStatus1 = new TwitterException("", httpResponseWithRateLimitStatus);
        TwitterException exceptionWithRateLimitStatus2 = new TwitterException("", httpResponseWithRateLimitStatus);
        assertEquals(exceptionWithRateLimitStatus1.hashCode(), exceptionWithRateLimitStatus2.hashCode());

        HttpResponse httpResponseWithRateLimitStatusNull = new HttpResponseImpl();
        httpResponseWithRateLimitStatusNull.setStatusCode(400);
        httpResponseWithRateLimitStatusNull.setResponseHeader("RateLimitStatus", null);
        TwitterException exceptionWithRateLimitStatusNull1 = new TwitterException("", httpResponseWithRateLimitStatusNull);
        TwitterException exceptionWithRateLimitStatusNull2 = new TwitterException("", httpResponseWithRateLimitStatusNull);
        assertNotEquals(exceptionWithRateLimitStatusNull1.hashCode(), exceptionWithRateLimitStatusNull2.hashCode());
    }

    @Test
    public void testToString() {
        // Typical Use Case
        TwitterException exception = new TwitterException("");
        // Since toString() contains dynamic values, we can only test that it does not throw any exception.
        assertNotNull(exception.toString());
    }

    private static class HttpResponseImpl implements HttpResponse {
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