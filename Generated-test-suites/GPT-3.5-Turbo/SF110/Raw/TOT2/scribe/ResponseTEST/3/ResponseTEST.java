import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(createMockConnection());
        assertEquals("Body content should match", "Test body content", response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(createMockConnection());
        assertNotNull("Stream should not be null", response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(createMockConnection());
        assertEquals("Response code should match", 200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(createMockConnection());
        Map<String, String> headers = response.getHeaders();
        assertNotNull("Headers should not be null", headers);
        assertTrue("Headers should contain key 'Content-Type'", headers.containsKey("Content-Type"));
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(createMockConnection());
        assertEquals("Header value should match", "application/json", response.getHeader("Content-Type"));
    }

    private HttpURLConnection createMockConnection() {
        //Create mock HttpURLConnection for testing
        return null;
    }
}