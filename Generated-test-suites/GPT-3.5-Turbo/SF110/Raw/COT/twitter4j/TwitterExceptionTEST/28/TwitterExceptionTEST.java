import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.*;

public class TwitterExceptionTest {
  
  @Test
  public void testTwitterExceptionWithMsg() {
    String msg = "Test message";
    TwitterException exception = new TwitterException(msg);
    assertEquals(msg, exception.getMessage());
    assertEquals(-1, exception.getStatusCode());
    assertNull(exception.getRateLimitStatus());
    assertNull(exception.getFeatureSpecificRateLimitStatus());
  }
  
  @Test
  public void testTwitterExceptionWithCause() {
    Exception cause = new Exception();
    TwitterException exception = new TwitterException(cause);
    assertEquals(cause, exception.getCause());
    assertEquals(-1, exception.getStatusCode());
    assertNull(exception.getRateLimitStatus());
    assertNull(exception.getFeatureSpecificRateLimitStatus());
    assertTrue(exception.isCausedByNetworkIssue());
  }
  
  @Test
  public void testTwitterExceptionWithMsgAndRes() {
    String msg = "Test message";
    HttpResponse res = new HttpResponse();
    res.setStatusCode(420);
    res.setResponseHeader("Retry-After", "60");
    
    TwitterException exception = new TwitterException(msg, res);
    assertEquals(getCause(res) + "\n" + msg, exception.getMessage());
    assertEquals(420, exception.getStatusCode());
    assertNotNull(exception.getRateLimitStatus());
    assertEquals(60, exception.getRetryAfter());
  }
  
  @Test
  public void testTwitterExceptionWithMsgAndCause() {
    String msg = "Test message";
    Exception cause = new Exception();
    TwitterException exception = new TwitterException(msg, cause);
    assertEquals(msg, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertEquals(-1, exception.getStatusCode());
    assertNull(exception.getRateLimitStatus());
    assertNull(exception.getFeatureSpecificRateLimitStatus());
  }
  
  @Test
  public void testTwitterExceptionWithMsgCauseAndStatusCode() {
    String msg = "Test message";
    Exception cause = new Exception();
    int statusCode = 500;
    
    TwitterException exception = new TwitterException(msg, cause, statusCode);
    assertEquals(msg, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertEquals(statusCode, exception.getStatusCode());
    assertNull(exception.getRateLimitStatus());
    assertNull(exception.getFeatureSpecificRateLimitStatus());
  }
  
  @Test
  public void testGetRateLimitStatus() {
    TwitterException exception = new TwitterException("Test");
    assertNull(exception.getRateLimitStatus());
    
    RateLimitStatus rateLimitStatus = new RateLimitStatus();
    exception.rateLimitStatus = rateLimitStatus;
    assertEquals(rateLimitStatus, exception.getRateLimitStatus());
  }
  
  @Test
  public void testGetFeatureSpecificRateLimitStatus() {
    TwitterException exception = new TwitterException("Test");
    assertNull(exception.getFeatureSpecificRateLimitStatus());
    
    RateLimitStatus featureSpecificRateLimitStatus = new RateLimitStatus();
    exception.featureSpecificRateLimitStatus = featureSpecificRateLimitStatus;
    assertEquals(featureSpecificRateLimitStatus, exception.getFeatureSpecificRateLimitStatus());
  }
  
  @Test
  public void testGetRetryAfter() {
    TwitterException exception = new TwitterException("Test");
    exception.statusCode = 200;
    
    try {
      exception.getRetryAfter();
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Exception thrown as expected
    }
    
    exception.statusCode = 420;
    exception.retryAfter = 60;
    assertEquals(60, exception.getRetryAfter());
  }
  
  @Test
  public void testIsCausedByNetworkIssue() {
    TwitterException exception = new TwitterException("Test");
    assertFalse(exception.isCausedByNetworkIssue());
    
    exception = new TwitterException(new IOException());
    assertTrue(exception.isCausedByNetworkIssue());
  }
  
  @Test
  public void testExceededRateLimitation() {
    TwitterException exception = new TwitterException("Test");
    assertFalse(exception.exceededRateLimitation());
    
    exception.statusCode = 400;
    assertFalse(exception.exceededRateLimitation());
    
    exception.rateLimitStatus = new RateLimitStatus();
    assertTrue(exception.exceededRateLimitation());
    
    exception.statusCode = 420;
    assertTrue(exception.exceededRateLimitation());
  }
  
  @Test
  public void testResourceNotFound() {
    TwitterException exception = new TwitterException("Test");
    assertFalse(exception.resourceNotFound());
    
    exception.statusCode = 404;
    assertTrue(exception.resourceNotFound());
  }
  
  @Test
  public void testGetExceptionCode() {
    TwitterException exception = new TwitterException("Test");
    assertEquals("", exception.getExceptionCode());
    
    String expectedCode = new ExceptionDiagnosis(exception, TwitterException.FILTER).asHexString();
    assertEquals(expectedCode, exception.getExceptionCode());
  }
  
  @Test
  public void testEquals() {
    TwitterException exception1 = new TwitterException("Test");
    TwitterException exception2 = new TwitterException("Test");
    
    assertTrue(exception1.equals(exception1));
    assertTrue(exception1.equals(exception2));
    
    exception1.retryAfter = 1;
    assertFalse(exception1.equals(exception2));
    
    exception2.retryAfter = 1;
    assertTrue(exception1.equals(exception2));
  }
  
  @Test
  public void testHashCode() {
    TwitterException exception = new TwitterException("Test");
    int expectedHashCode = 31 * exception.statusCode;
    
    assertEquals(expectedHashCode, exception.hashCode());
  }
  
  @Test
  public void testToString() {
    TwitterException exception = new TwitterException("Test");
    String expectedToString = "TwitterException{statusCode=-1, retryAfter=0, rateLimitStatus=null, version=unknown}";
    
    assertEquals(expectedToString, exception.toString());
    
    exception.nested = true;
    expectedToString = "TwitterException{exceptionCode=[], statusCode=-1, retryAfter=0, rateLimitStatus=null, version=unknown}";
    assertEquals(expectedToString, exception.toString());
  }
  
  private static String getCause(final HttpResponse res) {
    int statusCode = res.getStatusCode();
    String cause = null;
    switch (statusCode) {
      case 304:
        cause = "There was no new data to return.";
        break;
      case 400:
        cause = "The request was invalid. An accompanying error message will explain why. This is the status code returned during rate limiting.";
        break;
      case 401:
        cause = "Authentication credentials were missing or incorrect.";
        break;
      case 403:
        cause = "The request is understood, but it has been refused. An accompanying error message will explain why.";
        break;
      case 404:
        cause = "The URI requested is invalid or the resource requested, such as a user, does not exist.";
        break;
      case 406:
        cause = "Returned by the Search API when an invalid format is specified in the request.";
        break;
      case 420:
        cause = "The number of requests you have made exceeds the quota afforded by your assigned rate limit.";
        break;
      case 500:
        cause = "Something is broken. Please post to the group so the Twitter team can investigate.";
        break;
      case 502:
        cause = "Twitter is down or being upgraded.";
        break;
      case 503:
        cause = "Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
        break;
      default:
        cause = "";
        break;
    }
    return statusCode + ":" + cause;
  }
}