import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testGetStatusCode() {
        TwitterException twitterException = new TwitterException("Test");
        assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test");
        assertNull(twitterException.getRateLimitStatus());
    }

    @Test
    public void testGetFeatureSpecificRateLimitStatus() {
        TwitterException twitterException = new TwitterException("Test");
        assertNull(twitterException.getFeatureSpecificRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        TwitterException twitterException = new TwitterException("Test");
        twitterException.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        TwitterException twitterException = new TwitterException("Test", new IOException());
        assertTrue(twitterException.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException twitterException1 = new TwitterException("Test", new Exception(), 400);
        assertTrue(twitterException1.exceededRateLimitation());

        TwitterException twitterException2 = new TwitterException("Test", new Exception(), 420);
        assertTrue(twitterException2.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException twitterException = new TwitterException("Test", new IOException(), 404);
        assertTrue(twitterException.resourceNotFound());
    }

    @Test
    public void testEquals() {
        TwitterException twitterException1 = new TwitterException("Test");
        TwitterException twitterException2 = new TwitterException("Test");
        assertTrue(twitterException1.equals(twitterException2));
    }

    @Test
    public void testHashCode() {
        TwitterException twitterException = new TwitterException("Test");
        assertEquals(0, twitterException.hashCode());
    }
}