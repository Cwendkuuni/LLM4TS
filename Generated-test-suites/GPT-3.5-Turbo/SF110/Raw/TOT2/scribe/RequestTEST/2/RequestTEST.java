import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.model.Response;

public class RequestTest {

    @Test
    public void testSend_Success() {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection_Success() {
        Request request = new Request(Verb.GET, "http://example.com");
        try {
            request.createConnection();
            HttpURLConnection connection = request.getConnection();
            assertNotNull(connection);
        } catch (IOException e) {
            fail("Exception thrown when creating connection");
        }
    }

    @Test
    public void testAddHeader_Success() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }

    // Add more test cases for other public methods

}