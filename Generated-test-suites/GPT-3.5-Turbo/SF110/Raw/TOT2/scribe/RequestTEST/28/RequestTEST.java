
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import org.scribe.model.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testSendWithPUT() {
        Request request = new Request(Verb.PUT, "https://example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        Request request = new Request(Verb.GET, "https://invalidurl");
        request.send();
    }
}
