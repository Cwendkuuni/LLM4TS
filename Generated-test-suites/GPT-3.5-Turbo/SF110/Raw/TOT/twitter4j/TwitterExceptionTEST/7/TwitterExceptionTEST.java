import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        // Create a TwitterException with a message
        TwitterException exception = new TwitterException("Test exception message");
        
        // Verify that the message is correctly set
        assertEquals("Test exception message", exception.getMessage());
    }
    
    @Test
    public void testConstructorWithCause() {
        // Create a new IOException for the cause
        IOException cause = new IOException();
        
        // Create a TwitterException with a cause
        TwitterException exception = new TwitterException(cause);
        
        // Verify that the cause is correctly set
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    public void testConstructorWithMessageAndResponse() {
        // Create a mock HttpResponse with status code 400
        HttpResponse response = new MockHttpResponse(400);
        
        // Create a TwitterException with a message and response
        TwitterException exception = new TwitterException("Test exception message", response);
        
        // Verify that the message and response are correctly set
        assertEquals("Test exception message\n400:Bad Request", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        // Create a new IOException for the cause
        IOException cause = new IOException();
        
        // Create a TwitterException with a message and cause
        TwitterException exception = new TwitterException("Test exception message", cause);
        
        // Verify that the message and cause are correctly set
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    public void testConstructorWithMessageCauseAndStatusCode() {
        // Create a new IOException for the cause
        IOException cause = new IOException();
        
        // Create a TwitterException with a message, cause, and status code
        TwitterException exception = new TwitterException("Test exception message", cause, 500);
        
        // Verify that the message, cause, and status code are correctly set
        assertEquals("Test exception message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        // Create a mock RateLimitStatus object
        RateLimitStatus rateLimitStatus = new MockRateLimitStatus();
        
        // Create a TwitterException and set the rate limit status
        TwitterException exception = new TwitterException("Test exception");
        exception.rateLimitStatus = rateLimitStatus;
        
        // Verify that the correct rate limit status is returned
        assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        // Create a mock RateLimitStatus object
        RateLimitStatus featureRateLimitStatus = new MockRateLimitStatus();
        
        // Create a TwitterException and set the feature specific rate limit status
        TwitterException exception = new TwitterException("Test exception");
        exception.featureSpecificRateLimitStatus = featureRateLimitStatus;
        
        // Verify that the correct feature specific rate limit status is returned
        assertEquals(featureRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        // Create a TwitterException with a status code of 420
        TwitterException exception = new TwitterException("Test exception");
        exception.statusCode = 420;
        exception.retryAfter = 60;
        
        // Verify that the correct retry after value is returned
        assertEquals(60, exception.getRetryAfter());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWithoutExceededRateLimitation() {
        // Create a TwitterException with a status code other than 420
        TwitterException exception = new TwitterException("Test exception");
        exception.statusCode = 400;
        
        // Verify that an IllegalStateException is thrown
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        // Create a TwitterException with an IOException as the cause
        TwitterException exception = new TwitterException(new IOException());
        
        // Verify that isCausedByNetworkIssue returns true
        assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        // Create a TwitterException with a status code of 400 and a rate limit status
        TwitterException exception1 = new TwitterException("Test exception");
        exception1.statusCode = 400;
        exception1.rateLimitStatus = new MockRateLimitStatus();
        
        // Verify that exceededRateLimitation returns true
        assertTrue(exception1.exceededRateLimitation());
        
        // Create a TwitterException with a status code of 420
        TwitterException exception2 = new TwitterException("Test exception");
        exception2.statusCode = 420;
        
        // Verify that exceededRateLimitation returns true
        assertTrue(exception2.exceededRateLimitation());
        
        // Create a TwitterException with a status code other than 400 or 420
        TwitterException exception3 = new TwitterException("Test exception");
        exception3.statusCode = 200;
        
        // Verify that exceededRateLimitation returns false
        assertFalse(exception3.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        // Create a TwitterException with a status code of 404
        TwitterException exception = new TwitterException("Test exception");
        exception.statusCode = 404;
        
        // Verify that resourceNotFound returns true
        assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        // Create a mock ExceptionDiagnosis object
        ExceptionDiagnosis diagnosis = new MockExceptionDiagnosis();
        
        // Create a TwitterException and set the exception diagnosis
        TwitterException exception = new TwitterException("Test exception");
        TwitterException.FILTER = new String[] { "twitter4j" };
        exception.setNested();
        
        // Verify that the correct exception code is returned
        assertEquals(diagnosis.asHexString(), exception.getExceptionCode());
    }

}