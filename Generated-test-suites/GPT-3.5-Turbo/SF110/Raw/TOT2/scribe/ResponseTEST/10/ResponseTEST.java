import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    
    @Test
    public void testGetBody_ReturnsBodyString() {
        Response response = new Response(getMockConnection());
        assertEquals("test body", response.getBody());
    }
    
    @Test
    public void testGetStream_ReturnsStreamObject() {
        Response response = new Response(getMockConnection());
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode_ReturnsResponseCode() {
        Response response = new Response(getMockConnection());
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders_ReturnsHeadersMap() {
        Response response = new Response(getMockConnection());
        Map<String, String> headers = response.getHeaders();
        assertEquals("value", headers.get("key"));
    }
    
    @Test
    public void testGetHeader_ReturnsHeaderValue() {
        Response response = new Response(getMockConnection());
        assertEquals("value", response.getHeader("key"));
    }
    
    private HttpURLConnection getMockConnection() {
        return new HttpURLConnection(new URL("http://example.com")) {
            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }

            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headerFields = new HashMap<>();
                headerFields.put("key", List.of("value"));
                return headerFields;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("test body".getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return new ByteArrayInputStream("error body".getBytes());
            }
        };
    }
}