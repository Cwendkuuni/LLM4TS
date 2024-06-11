package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import twitter4j.internal.http.HttpResponse;

import org.junit.Before;
import org.junit.Test;

public class TwitterExceptionTest {
    
    private HttpResponse mockedResponse;
    
    @Before
    public void setup() {
        mockedResponse = mock(HttpResponse.class);
    }

    @Test
    public void testConstructor() {
        TwitterException exception = new TwitterException("Test Message");
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testConstructorWithCause() {
        Exception cause = new Exception("Cause");
        TwitterException exception = new TwitterException(cause);
        
        assertEquals("Cause", exception.getCause().getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        verify(cause, times(1)).setNested();
    }
    
    @Test
    public void testConstructorWithResponse() {
        when(mockedResponse.getStatusCode()).thenReturn(420);
        when(mockedResponse.getResponseHeader("Retry-After")).thenReturn("5");
        when(RateLimitStatusJSONImpl.createFromResponseHeader(mockedResponse)).thenReturn(null);
        when(RateLimitStatusJSONImpl.createFeatureSpecificRateLimitStatusFromResponseHeader(mockedResponse)).thenReturn(null);
        
        TwitterException exception = new TwitterException("Test Message", mockedResponse);
        
        assertEquals("420:Test Message", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals(5, exception.getRetryAfter());
    }
    
    @Test
    public void testConstructorWithCauseAndStatusCode() {
        Exception cause = new Exception("Cause");
        TwitterException exception = new TwitterException("Test Message", cause, 200);
        
        assertEquals("Test Message", exception.getMessage());
        assertEquals(200, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
        assertEquals("ExceptionDiagnosis{exception=TwitterException{exceptionCode=[], statusCode=200, retryAfter=0, rateLimitStatus=null, version=null}}", exception.getExceptionCode());
        assertEquals("TwitterException{exceptionCode=[], statusCode=200, retryAfter=0, rateLimitStatus=null, version=null}", exception.toString());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = 200;
        
        assertEquals(200, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Message");
        exception.rateLimitStatus = null;
        
        assertNull(exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Message");
        exception.featureSpecificRateLimitStatus = null;
        
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = 420;
        exception.retryAfter = 5;
        
        assertEquals(5, exception.getRetryAfter());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testGetRetryAfterException() {
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = 200;
        
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Message");
        exception.initCause(new IOException());
        
        assertEquals(true, exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test Message1");
        exception1.statusCode = 200;
        exception1.rateLimitStatus = null;
        
        TwitterException exception2 = new TwitterException("Test Message2");
        exception2.statusCode = 400;
        exception2.rateLimitStatus = null;
        
        TwitterException exception3 = new TwitterException("Test Message3");
        exception3.statusCode = 420;
        exception3.rateLimitStatus = null;
        
        TwitterException exception4 = new TwitterException("Test Message4");
        exception4.statusCode = 420;
        exception4.rateLimitStatus = new RateLimitStatusJSONImpl();
        
        assertEquals(false, exception1.exceededRateLimitation());
        assertEquals(true, exception2.exceededRateLimitation());
        assertEquals(true, exception3.exceededRateLimitation());
        assertEquals(true, exception4.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception1 = new TwitterException("Test Message1");
        exception1.statusCode = 200;
        
        TwitterException exception2 = new TwitterException("Test Message2");
        exception2.statusCode = 404;
        
        assertEquals(false, exception1.resourceNotFound());
        assertEquals(true, exception2.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Message");
        
        assertEquals("ExceptionDiagnosis{exception=TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=null}}", exception.getExceptionCode());
    }
    
    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Message1");
        exception1.statusCode = 200;
        exception1.retryAfter = 5;
        
        TwitterException exception2 = new TwitterException("Test Message2");
        exception2.statusCode = 200;
        exception2.retryAfter = 5;
        exception2.rateLimitStatus = null;
        
        TwitterException exception3 = new TwitterException("Test Message3");
        exception3.statusCode = 200;
        exception3.retryAfter = 5;
        exception3.rateLimitStatus = new RateLimitStatusJSONImpl();
        
        TwitterException exception4 = new TwitterException("Test Message4");
        exception4.statusCode = 400;
        
        assertEquals(true, exception1.equals(exception2));
        assertEquals(false, exception1.equals(exception3));
        assertEquals(false, exception1.equals(exception4));
    }
    
    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = 200;
        exception.retryAfter = 5;
        exception.rateLimitStatus = null;
        
        assertEquals(6213, exception.hashCode());
    }
    
    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Message");
        exception.statusCode = 200;
        exception.retryAfter = 5;
        exception.rateLimitStatus = null;
        
        assertEquals("TwitterException{exceptionCode=[], statusCode=200, retryAfter=5, rateLimitStatus=null, version=null}", exception.toString());
    }

    @Test
    public void testGetCause() {
        HttpResponse mockedResponse = mock(HttpResponse.class);
        when(mockedResponse.getStatusCode()).thenReturn(400);
        when(mockedResponse.getResponseHeader("Retry-After")).thenReturn(null);
        
        assertEquals("400:The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.", TwitterException.getCause(mockedResponse));
    }
    
    @Test
    public void testGetCauseWithOtherStatus() {
        HttpResponse mockedResponse = mock(HttpResponse.class);
        when(mockedResponse.getStatusCode()).thenReturn(500);
        
        assertEquals("500:Something is broken.  Please post to the group so the Twitter team can investigate.", TwitterException.getCause(mockedResponse));
    }

}