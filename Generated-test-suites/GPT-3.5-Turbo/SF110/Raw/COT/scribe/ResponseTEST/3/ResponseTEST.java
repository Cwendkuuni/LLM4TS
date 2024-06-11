
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class ResponseTest {

    private Response response;
    
    @Before
    public void setup() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        response = new Response(connection);
    }
    
    @Test
    public void testGetBody() {
        String expectedBody = ""; // Empty body in the Response constructor
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }
    
    @Test
    public void testGetStream() {
        InputStream expectedStream = response.getStream();
        assertNotNull(expectedStream);
    }
    
    @Test
    public void testGetCode() {
        int expectedCode = response.getCode();
        assertTrue(expectedCode >= 200 && expectedCode < 400);
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = response.getHeaders();
        assertNotNull(expectedHeaders);
    }
    
    @Test
    public void testGetHeader() {
        String headerName = "Content-Type";
        String expectedHeaderValue = response.getHeader(headerName);
        assertNotNull(expectedHeaderValue);
    }
    
    // Additional test cases for potential edge cases and exception handling scenarios
    
    @Test
    public void testParseBodyContents() {
        String expectedParsedBody = response.parseBodyContents();
        assertNotNull(expectedParsedBody);
    }
    
    @Test
    public void testParseHeaders() {
        HttpURLConnection conn = (HttpURLConnection) new URL("http://example.com").openConnection();
        Map<String, String> expectedParsedHeaders = response.parseHeaders(conn);
        assertNotNull(expectedParsedHeaders);
    }
    
    @Test
    public void testWasSuccessful() {
        boolean expectedSuccessStatus = response.wasSuccessful();
        assertTrue(expectedSuccessStatus);
    }
    
}
