package twitter4j;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new IOException("Network Issue");
        TwitterException exception = new TwitterException(cause);
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse mockResponse = new HttpResponseMock(420, null);
        TwitterException exception = new TwitterException("Test Message", mockResponse);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertTrue(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new IOException("Network Issue");
        TwitterException exception = new TwitterException("Test Message", cause);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new IOException("Network Issue");
        TwitterException exception = new TwitterException("Test Message", cause, 500);
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(-1, exception.getRetryAfter());
        assertFalse(exception.isCausedByNetworkIssue());
        assertFalse(exception.exceededRateLimitation());
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Message");
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Message");
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test Message");
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.isCausedByNetworkIssue());

        exception = new TwitterException(new IOException("Network Issue"));
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.exceededRateLimitation());

        HttpResponse mockResponse = new HttpResponseMock(400, null);
        exception = new TwitterException("Test Message", mockResponse);
        assertTrue(exception.exceededRateLimitation());

        mockResponse = new HttpResponseMock(420, null);
        exception = new TwitterException("Test Message", mockResponse);
        assertTrue(exception.exceededRateLimitation());

        mockResponse = new HttpResponseMock(200, null);
        exception = new TwitterException("Test Message", mockResponse);
        assertFalse(exception.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.resourceNotFound());

        HttpResponse mockResponse = new HttpResponseMock(404, null);
        exception = new TwitterException("Test Message", mockResponse);
        assertTrue(exception.resourceNotFound());

        mockResponse = new HttpResponseMock(200, null);
        exception = new TwitterException("Test Message", mockResponse);
        assertFalse(exception.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("", exception.getExceptionCode());
    }

    @Test
    public void testSetNested() {
        TwitterException exception = new TwitterException("Test Message");
        assertFalse(exception.equals(null));
        assertFalse(exception.equals(new TwitterException("Test Message")));
        assertFalse(exception.equals(new TwitterException(new IOException("Network Issue"))));
    }

    @Test
    public void testEquals() {
        TwitterException exception = new TwitterException("Test Message");
        assertTrue(exception.equals(exception));

        HttpResponse mockResponse = new HttpResponseMock(404, null);
        TwitterException exception1 = new TwitterException("Test Message", mockResponse);
        assertTrue(exception.equals(exception1));
        assertTrue(exception1.equals(exception));

        TwitterException exception2 = new TwitterException("Different Message", mockResponse);
        assertFalse(exception.equals(exception2));
        assertFalse(exception2.equals(exception));

        TwitterException exception3 = new TwitterException("Test Message", new IOException("Network Issue"));
        assertFalse(exception.equals(exception3));
        assertFalse(exception3.equals(exception));

        TwitterException exception4 = new TwitterException("Test Message", new IOException("Different Issue"));
        assertFalse(exception.equals(exception4));
        assertFalse(exception4.equals(exception));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals(-548646121, exception.hashCode());

        HttpResponse mockResponse = new HttpResponseMock(404, null);
        TwitterException exception1 = new TwitterException("Test Message", mockResponse);
        assertEquals(-548646091, exception.hashCode());
        assertEquals(-548646091, exception1.hashCode());

        TwitterException exception2 = new TwitterException("Different Message", mockResponse);
        assertNotEquals(exception.hashCode(), exception2.hashCode());

        TwitterException exception3 = new TwitterException("Test Message", new IOException("Network Issue"));
        assertNotEquals(exception.hashCode(), exception3.hashCode());

        TwitterException exception4 = new TwitterException("Test Message", new IOException("Different Issue"));
        assertNotEquals(exception.hashCode(), exception4.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Message");
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=twitter4j-4.0.7}", exception.toString());

        HttpResponse mockResponse = new HttpResponseMock(404, null);
        TwitterException exception1 = new TwitterException("Test Message", mockResponse);
        assertEquals("TwitterException{exceptionCode=[], statusCode=404, retryAfter=0, rateLimitStatus=null, version=twitter4j-4.0.7}", exception1.toString());

        mockResponse = new HttpResponseMock(420, null);
        TwitterException exception2 = new TwitterException("Test Message", mockResponse);
        assertEquals("TwitterException{exceptionCode=[], statusCode=420, retryAfter=0, rateLimitStatus=null, version=twitter4j-4.0.7}", exception2.toString());

        TwitterException exception3 = new TwitterException("Test Message", new IOException("Network Issue"));
        assertEquals("TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=twitter4j-4.0.7}", exception3.toString());

        TwitterException exception4 = new TwitterException("Test Message", new IOException("Network Issue"), 500);
        assertEquals("TwitterException{exceptionCode=[], statusCode=500, retryAfter=0, rateLimitStatus=null, version=twitter4j-4.0.7}", exception4.toString());
    }
}

class HttpResponseMock implements HttpResponse {
    private int statusCode;
    private String responseHeader;

    HttpResponseMock(int statusCode, String responseHeader) {
        this.statusCode = statusCode;
        this.responseHeader = responseHeader;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getResponseHeader(String name) {
        return responseHeader;
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
    public void disconnect() throws IOException {
    }
}