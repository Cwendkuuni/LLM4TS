import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;
import java.net.*;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class ResponseTest {

    private Response response;
    
    @Before
    public void setUp() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://www.example.com").openConnection();
        this.response = new Response(connection);
    }
    
    @Test
    public void testGetBody_WhenBodyIsNotNull_ReturnsBody() {
        String body = "Example Body";
        this.response.setBody(body);
        assertEquals(body, this.response.getBody());
    }
    
    @Test
    public void testGetBody_WhenBodyIsNull_ReturnsParsedBodyContents() {
        this.response.setBody(null);
        String parsedBody = this.response.parseBodyContents();
        assertEquals(parsedBody, this.response.getBody());
    }
    
    @Test
    public void testGetStream_ReturnsInputStream() {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        this.response.setStream(stream);
        assertEquals(stream, this.response.getStream());
    }
    
    @Test
    public void testGetCode_ReturnsCode() {
        int code = 200;
        this.response.setCode(code);
        assertEquals(code, this.response.getCode());
    }
    
    @Test
    public void testGetHeaders_ReturnsHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        this.response.setHeaders(headers);
        assertEquals(headers, this.response.getHeaders());
    }
    
    @Test
    public void testGetHeader_WhenNameExists_ReturnsHeaderValue() {
        String name = "Content-Type";
        String value = "text/plain";
        Map<String, String> headers = new HashMap<>();
        headers.put(name, value);
        this.response.setHeaders(headers);
        assertEquals(value, this.response.getHeader(name));
    }
    
    @Test
    public void testGetHeader_WhenNameDoesNotExist_ReturnsNull() {
        String name = "Nonexistent-Header";
        assertNull(this.response.getHeader(name));
    }
}