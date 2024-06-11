import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    private TwitterException twitterException;

    @Before
    public void setUp() {
        twitterException = new TwitterException("Test Exception");
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfterNotExceeded() {
        twitterException.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        assertFalse(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitationWithNullRateLimitStatus() {
        assertFalse(twitterException.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithRateLimitStatus() {
        TwitterException twitterExceptionWithRateLimitStatus = new TwitterException("Test Exception");
        twitterExceptionWithRateLimitStatus.rateLimitStatus = new RateLimitStatusJSONImpl();
        assertTrue(twitterExceptionWithRateLimitStatus.exceededRateLimitation());
    }

    @Test
    public void testExceededRateLimitationWithStatusCode420() {
        TwitterException twitterExceptionWithStatusCode420 = new TwitterException("Test Exception");
        twitterExceptionWithStatusCode420.statusCode = 420;
        assertTrue(twitterExceptionWithStatusCode420.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        assertFalse(twitterException.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        assertNotNull(twitterException.getExceptionCode());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(twitterException.equals(twitterException));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(twitterException.equals("Test String"));
    }

    @Test
    public void testEqualsWithDifferentRetryAfter() {
        TwitterException twitterExceptionWithDifferentRetryAfter = new TwitterException("Test Exception");
        twitterExceptionWithDifferentRetryAfter.retryAfter = 10;
        assertFalse(twitterException.equals(twitterExceptionWithDifferentRetryAfter));
    }

    @Test
    public void testEqualsWithDifferentStatusCode() {
        TwitterException twitterExceptionWithDifferentStatusCode = new TwitterException("Test Exception");
        twitterExceptionWithDifferentStatusCode.statusCode = 200;
        assertFalse(twitterException.equals(twitterExceptionWithDifferentStatusCode));
    }

    @Test
    public void testEqualsWithDifferentRateLimitStatus() {
        TwitterException twitterExceptionWithDifferentRateLimitStatus = new TwitterException("Test Exception");
        twitterExceptionWithDifferentRateLimitStatus.rateLimitStatus = new RateLimitStatusJSONImpl();
        assertFalse(twitterException.equals(twitterExceptionWithDifferentRateLimitStatus));
    }

    @Test
    public void testEqualsWithSameAttributes() {
        TwitterException twitterExceptionWithSameAttributes = new TwitterException("Test Exception");
        assertTrue(twitterException.equals(twitterExceptionWithSameAttributes));
    }

    @Test
    public void testHashCode() {
        assertNotNull(twitterException.hashCode());
    }

    @Test
    public void testToString() {
        assertNotNull(twitterException.toString());
    }
}