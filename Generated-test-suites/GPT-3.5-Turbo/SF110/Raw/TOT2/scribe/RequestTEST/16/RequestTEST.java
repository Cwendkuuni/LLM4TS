import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertNotNull(request.send());
    }

    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertDoesNotThrow(() -> request.createConnection());
    }

    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertNotNull(request.getHeaders());
    }

    @Test
    public void testAddBody() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        assertDoesNotThrow(() -> request.addBody(request.connection, request.getByteBodyContents()));
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertTrue(request.getHeaders().containsValue("application/json"));
    }

    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "https://www.example.com?key1=value1&key2=value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("value1", queryStringParams.get("key1"));
        assertEquals("value2", queryStringParams.get("key2"));
    }

    // Add more test cases for other methods and edge cases

}