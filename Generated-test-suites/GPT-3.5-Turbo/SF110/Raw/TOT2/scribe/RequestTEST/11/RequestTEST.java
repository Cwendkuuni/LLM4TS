import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNotNull(request.send());
    }

    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "http://example.com");
        try {
            request.createConnection();
            assertNotNull(request.connection);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    // Add more test cases for other public methods

}