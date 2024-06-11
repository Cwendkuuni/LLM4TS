import org.junit.*;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        int statusCode = exception.getStatusCode();
        assertEquals(-1, statusCode);
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        RateLimitStatus rateLimitStatus = exception.getRateLimitStatus();
        assertNull(rateLimitStatus);
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        RateLimitStatus featureSpecificRateLimitStatus = exception.getFeatureSpecificRateLimitStatus();
        assertNull(featureSpecificRateLimitStatus);
    }

    @Test
    public void testGetRetryAfter() {
        TwitterException exception = new TwitterException("Test Exception");
        assertThrows(IllegalStateException.class, () -> {
            exception.getRetryAfter();
        });
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException exception = new TwitterException("Test Exception");
        boolean isCausedByNetworkIssue = exception.isCausedByNetworkIssue();
        assertFalse(isCausedByNetworkIssue);
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test Exception");
        boolean exceededRateLimitation1 = exception1.exceededRateLimitation();
        assertFalse(exceededRateLimitation1);

        TwitterException exception2 = new TwitterException("Test Exception");
        exception2.statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        exception2.rateLimitStatus = rateLimitStatus;
        boolean exceededRateLimitation2 = exception2.exceededRateLimitation();
        assertTrue(exceededRateLimitation2);

        TwitterException exception3 = new TwitterException("Test Exception");
        exception3.statusCode = 420;
        boolean exceededRateLimitation3 = exception3.exceededRateLimitation();
        assertTrue(exceededRateLimitation3);
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception = new TwitterException("Test Exception");
        boolean resourceNotFound = exception.resourceNotFound();
        assertFalse(resourceNotFound);

        TwitterException exception2 = new TwitterException("Test Exception");
        exception2.statusCode = 404;
        boolean resourceNotFound2 = exception2.resourceNotFound();
        assertTrue(resourceNotFound2);
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        String exceptionCode = exception.getExceptionCode();
        assertNotNull(exceptionCode);
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = exception1;
        assertTrue(exception1.equals(exception2));

        TwitterException exception3 = new TwitterException("Test Exception");
        TwitterException exception4 = new TwitterException("Test Exception");
        exception4.statusCode = 400;
        RateLimitStatus rateLimitStatus = new RateLimitStatus();
        exception4.rateLimitStatus = rateLimitStatus;
        assertFalse(exception3.equals(exception4));

        TwitterException exception5 = new TwitterException("Test Exception");
        TwitterException exception6 = new TwitterException("Test Exception");
        exception6.retryAfter = 1;
        assertFalse(exception5.equals(exception6));

        TwitterException exception7 = new TwitterException("Test Exception");
        TwitterException exception8 = new TwitterException("Test Exception");
        exception8.rateLimitStatus = new RateLimitStatus();
        assertFalse(exception7.equals(exception8));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Exception");
        int hashCode = exception.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        String str = exception.toString();
        assertNotNull(str);
    }

}