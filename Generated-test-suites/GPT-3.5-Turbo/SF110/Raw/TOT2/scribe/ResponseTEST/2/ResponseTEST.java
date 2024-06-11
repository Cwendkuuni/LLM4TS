import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    
    @Test
    public void testGetBody() {
        Response response = new Response(mockConnection(200));
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(mockConnection(200));
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(mockConnection(200));
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(mockConnection(200));
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(mockConnection(200));
        assertEquals("value", response.getHeader("key"));
    }
    
    private HttpURLConnection mockConnection(int responseCode) {
        HttpURLConnection connection = new HttpURLConnection(null) {
            @Override
            public int getResponseCode() throws IOException {
                return responseCode;
            }
            
            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headers = new HashMap<>();
                headers.put("key", List.of("value"));
                return headers;
            }
            
            @Override
            public InputStream getInputStream() {
                return null;
            }
            
            @Override
            public InputStream getErrorStream() {
                return null;
            }
        };
        
        return connection;
    }
}