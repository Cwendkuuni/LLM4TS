import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionWithMsg() {
        TwitterException twitterException = new TwitterException("Test message");
        assertEquals("Test message", twitterException.getMessage());
        assertEquals(-1, twitterException.getStatusCode());
    }

    @Test
    public void testTwitterExceptionWithCause() {
        Exception cause = new Exception("Test cause");
        TwitterException twitterException = new TwitterException(cause);
        assertEquals(cause, twitterException.getCause());
        assertEquals(-1, twitterException.getStatusCode());
    }

    // Add test cases for other public methods here...

    @Test
    public void testGetCauseWithStatusCode304() {
        HttpResponse res = new HttpResponseImpl(304);
        assertEquals("304:There was no new data to return.", TwitterException.getCause(res));
    }

    @Test
    public void testGetCauseWithUnknownStatusCode() {
        HttpResponse res = new HttpResponseImpl(999);
        assertEquals("999:", TwitterException.getCause(res));
    }
}