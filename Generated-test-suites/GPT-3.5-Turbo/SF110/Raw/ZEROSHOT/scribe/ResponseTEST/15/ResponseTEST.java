import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    
    @Test
    public void testGetBody() {
        Response response = createResponse();
        String expected = "";
        String actual = response.getBody();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetStream() {
        Response response = createResponse();
        assertNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = createResponse();
        int expected = 404;
        int actual = response.getCode();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetHeaders() {
        Response response = createResponse();
        Map<String, String> expected = new HashMap<>();
        Map<String, String> actual = response.getHeaders();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetHeader() {
        Response response = createResponse();
        String expected = null;
        String actual = response.getHeader("TestHeader");
        assertEquals(expected, actual);
    }
    
    private Response createResponse() {
        HttpURLConnection connection = null;
        try {
            connection = new HttpURLConnection(new URL("http://www.example.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Response(connection);
    }
}