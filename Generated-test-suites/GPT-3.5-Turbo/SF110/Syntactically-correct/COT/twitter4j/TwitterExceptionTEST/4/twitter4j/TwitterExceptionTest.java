import org.junit.Assert;
import org.junit.Test;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionConstructorWithMessage() {
        String msg = "Test message";
        TwitterException exception = new TwitterException(msg);
        
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionConstructorWithCause() {
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(cause);
        
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionConstructorWithMessageAndResponse() {
        String msg = "Test message";
        HttpResponse res = new HttpResponseImpl();
        TwitterException exception = new TwitterException(msg, res);
        
        Assert.assertEquals(getCause(res) + "\n" + msg, exception.getMessage());
        Assert.assertEquals(res.getStatusCode(), exception.getStatusCode());
        Assert.assertNotNull(exception.getRateLimitStatus());
        Assert.assertNotNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionConstructorWithMessageAndCause() {
        String msg = "Test message";
        Exception cause = new Exception("Test cause");
        TwitterException exception = new TwitterException(msg, cause);
        
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(-1, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testTwitterExceptionConstructorWithMessageCauseAndStatusCode() {
        String msg = "Test message";
        Exception cause = new Exception("Test cause");
        int statusCode = 500;
        TwitterException exception = new TwitterException(msg, cause, statusCode);
        
        Assert.assertEquals(msg, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
        Assert.assertEquals(statusCode, exception.getStatusCode());
        Assert.assertNull(exception.getRateLimitStatus());
        Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test message");
        int statusCode = 500;
        exception.statusCode = statusCode;
        
        Assert.assertEquals(statusCode, exception.getStatusCode());
    }
    
    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        RateLimitStatus rateLimitStatus = new RateLimitStatusJSONImpl();
        exception.rateLimitStatus = rateLimitStatus;
        
        Assert.assertEquals(rateLimitStatus, exception.getRateLimitStatus());
    }
    
    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test message");
        RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatusJSONImpl();
        exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
        
        Assert.assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
    }
    
    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 420;
        int retryAfter = 10;
        exception.retryAfter = retryAfter;
        
        Assert.assertEquals(retryAfter, exception.getRetryAfter());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterWhenNotExceedingRateLimitation() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 200;
        
        exception.getRetryAfter();
    }
    
    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test message");
        IOException cause = new IOException("Test cause");
        exception.initCause(cause);
        
        Assert.assertTrue(exception.isCausedByNetworkIssue());
    }
    
    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test message");
        exception1.statusCode = 400;
        exception1.rateLimitStatus = new RateLimitStatusJSONImpl();
        
        TwitterException exception2 = new TwitterException("Test message");
        exception2.statusCode = 420;
        
        Assert.assertTrue(exception1.exceededRateLimitation());
        Assert.assertTrue(exception2.exceededRateLimitation());
    }
    
    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test message");
        exception.statusCode = 404;
        
        Assert.assertTrue(exception.resourceNotFound());
    }
    
    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test message");
        String exceptionCode = "Exception Code";
        ExceptionDiagnosis diagnosis = new ExceptionDiagnosis(exception, TwitterException.FILTER);
        diagnosis.exceptionCode = exceptionCode;
        
        Assert.assertEquals(exceptionCode, exception.getExceptionCode());
    }
    
    private static String getCause(HttpResponse res) {
        // Method implementation for testing purposes.
        return null;
    }
}

