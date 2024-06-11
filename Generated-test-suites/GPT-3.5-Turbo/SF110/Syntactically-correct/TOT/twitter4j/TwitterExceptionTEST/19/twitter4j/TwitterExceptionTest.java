import org.junit.Assert;
import org.junit.Test;

public class TwitterExceptionTest {
  
  @Test
  public void testTwitterExceptionConstructorWithString() {
    String message = "Test message";
    TwitterException exception = new TwitterException(message);
    Assert.assertEquals(message, exception.getMessage());
    Assert.assertEquals(-1, exception.getStatusCode());
    Assert.assertNull(exception.getRateLimitStatus());
    Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
  }
  
  @Test
  public void testTwitterExceptionConstructorWithException() {
    Exception cause = new Exception("Test exception");
    TwitterException exception = new TwitterException(cause);
    Assert.assertEquals(cause.toString(), exception.getCause().toString());
    Assert.assertEquals(-1, exception.getStatusCode());
    Assert.assertNull(exception.getRateLimitStatus());
    Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
  }
  
  @Test
  public void testTwitterExceptionConstructorWithMessageAndHttpResponse() {
    String message = "Test message";
    HttpResponse res = new HttpResponseImpl();
    res.setStatusCode(420);
    res.setResponseHeader("Retry-After", "60");
    TwitterException exception = new TwitterException(message, res);
    Assert.assertEquals(getCause(res) + "\n" + message, exception.getMessage());
    Assert.assertEquals(420, exception.getStatusCode());
    Assert.assertNull(exception.getRateLimitStatus());
    Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
    Assert.assertEquals(60, exception.getRetryAfter());
  }
  
  @Test
  public void testTwitterExceptionConstructorWithMessageAndException() {
    String message = "Test message";
    Exception cause = new Exception("Test exception");
    TwitterException exception = new TwitterException(message, cause);
    Assert.assertEquals(message, exception.getMessage());
    Assert.assertEquals(cause.toString(), exception.getCause().toString());
    Assert.assertEquals(-1, exception.getStatusCode());
    Assert.assertNull(exception.getRateLimitStatus());
    Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
  }
  
  @Test
  public void testTwitterExceptionConstructorWithMessageExceptionAndStatusCode() {
    String message = "Test message";
    Exception cause = new Exception("Test exception");
    int statusCode = 404;
    TwitterException exception = new TwitterException(message, cause, statusCode);
    Assert.assertEquals(message, exception.getMessage());
    Assert.assertEquals(cause.toString(), exception.getCause().toString());
    Assert.assertEquals(statusCode, exception.getStatusCode());
    Assert.assertNull(exception.getRateLimitStatus());
    Assert.assertNull(exception.getFeatureSpecificRateLimitStatus());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetRetryAfterWithNonExceededRateLimitation() {
    TwitterException exception = new TwitterException("Test message");
    exception.getRetryAfter();
  }
  
  @Test
  public void testIsCausedByNetworkIssue() {
    TwitterException exception = new TwitterException(new IOException("Network issue"));
    Assert.assertTrue(exception.isCausedByNetworkIssue());
  }
  
  @Test
  public void testExceededRateLimitation() {
    TwitterException exception1 = new TwitterException("Test message");
    Assert.assertFalse(exception1.exceededRateLimitation());
    
    HttpResponse res = new HttpResponseImpl();
    res.setStatusCode(400);
    TwitterException exception2 = new TwitterException("Test message", res);
    Assert.assertTrue(exception2.exceededRateLimitation());
    
    res.setStatusCode(420);
    TwitterException exception3 = new TwitterException("Test message", res);
    Assert.assertTrue(exception3.exceededRateLimitation());
  }
  
  @Test
  public void testResourceNotFound() {
    TwitterException exception1 = new TwitterException("Test message");
    Assert.assertFalse(exception1.resourceNotFound());
    
    HttpResponse res = new HttpResponseImpl();
    res.setStatusCode(404);
    TwitterException exception2 = new TwitterException("Test message", res);
    Assert.assertTrue(exception2.resourceNotFound());
  }
  
  @Test
  public void testGetExceptionCode() {
    TwitterException exception = new TwitterException("Test message");
    Assert.assertNotNull(exception.getExceptionCode());
  }
  
  @Test
  public void testEquals() {
    TwitterException exception1 = new TwitterException("Test message");
    Assert.assertEquals(exception1, exception1);
    
    TwitterException exception2 = new TwitterException("Test message");
    Assert.assertEquals(exception1, exception2);
    Assert.assertEquals(exception2, exception1);
    
    TwitterException exception3 = new TwitterException("Test message3");
    Assert.assertNotEquals(exception1, exception3);
    Assert.assertNotEquals(exception3, exception1);
  }
  
  @Test
  public void testHashCode() {
    TwitterException exception1 = new TwitterException("Test message");
    TwitterException exception2 = new TwitterException("Test message");
    
    Assert.assertEquals(exception1.hashCode(), exception2.hashCode());
  }
  
  @Test
  public void testToString() {
    TwitterException exception = new TwitterException("Test message");
    Assert.assertNotNull(exception.toString());
  }
  
  private static class HttpResponseImpl implements HttpResponse {
    
    private int statusCode;
    private String responseHeader;
    
    public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
    }
    
    public void setResponseHeader(String responseHeader, String value) {
      this.responseHeader = value;
    }
    
    @Override
    public int getStatusCode() {
      return statusCode;
    }

    @Override
    public String getResponseHeader(String name) {
      if (name.equals("Retry-After")) {
        return responseHeader;
      }
      return null;
    }

    // other interface methods...

  }
}