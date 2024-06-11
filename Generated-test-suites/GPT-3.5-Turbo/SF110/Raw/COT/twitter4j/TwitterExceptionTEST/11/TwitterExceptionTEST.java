
import static org.junit.Assert.*;
import org.junit.Test;

public class TwitterExceptionTest {

  @Test
  public void testTwitterExceptionString() {
    TwitterException twitterException = new TwitterException("Test Message");
    assertEquals("Test Message", twitterException.getMessage());
    assertEquals(-1, twitterException.getStatusCode());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
    assertEquals("000000", twitterException.getExceptionCode());
  }

  @Test
  public void testTwitterExceptionException() {
    Exception cause = new Exception("Test Cause");
    TwitterException twitterException = new TwitterException(cause);
    assertEquals(cause, twitterException.getCause());
    assertEquals(-1, twitterException.getStatusCode());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
    assertEquals("000000", twitterException.getExceptionCode());
  }

  @Test
  public void testTwitterExceptionStringHttpResponse() {
    HttpResponse res = new HttpResponse();
    res.setStatusCode(200);
    TwitterException twitterException = new TwitterException("Test Message", res);
    assertEquals("Test Message", twitterException.getMessage());
    assertEquals(200, twitterException.getStatusCode());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
    assertEquals("000000", twitterException.getExceptionCode());
  }

  @Test
  public void testTwitterExceptionStringException() {
    Exception cause = new Exception("Test Cause");
    TwitterException twitterException = new TwitterException("Test Message", cause);
    assertEquals("Test Message", twitterException.getMessage());
    assertEquals(cause, twitterException.getCause());
    assertEquals(-1, twitterException.getStatusCode());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
    assertEquals("000000", twitterException.getExceptionCode());
  }

  @Test
  public void testTwitterExceptionStringExceptionInt() {
    Exception cause = new Exception("Test Cause");
    TwitterException twitterException = new TwitterException("Test Message", cause, 200);
    assertEquals("Test Message", twitterException.getMessage());
    assertEquals(cause, twitterException.getCause());
    assertEquals(200, twitterException.getStatusCode());
    assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    assertFalse(twitterException.isCausedByNetworkIssue());
    assertFalse(twitterException.exceededRateLimitation());
    assertFalse(twitterException.resourceNotFound());
    assertEquals("000000", twitterException.getExceptionCode());
  }

  // Add test cases for other public methods...

}
