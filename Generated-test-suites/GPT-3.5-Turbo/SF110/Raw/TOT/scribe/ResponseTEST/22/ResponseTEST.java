import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.scribe.model.*;

public class ResponseTest {
    private HttpURLConnection connection;
    private Response response;
    
    @Before
    public void setUp() throws IOException {
        URL url = new URL("http://www.example.com");
        connection = (HttpURLConnection) url.openConnection();
        response = new Response(connection);
    }
    
    @Test
    public void testGetCode_WhenConnectionIsSuccessful_ReturnsResponseCode() {
        int expectedCode = 200;
        assertEquals(expectedCode, response.getCode());
    }
    
    @Test
    public void testGetCode_WhenConnectionFails_Returns404() {
        HttpURLConnection invalidConnection;
        Response invalidResponse;
        try {
            URL invalidUrl = new URL("http://www.invalidurl.com");
            invalidConnection = (HttpURLConnection) invalidUrl.openConnection();
            invalidResponse = new Response(invalidConnection);
            int expectedCode = 404;
            assertEquals(expectedCode, invalidResponse.getCode());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetHeaders_ReturnsNonEmptyMap() {
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertFalse(headers.isEmpty());
    }
    
    @Test
    public void testGetHeader_WhenHeaderExists_ReturnsHeaderValue() {
        String expectedHeader = "application/json";
        String headerValue = response.getHeader("Content-Type");
        assertEquals(expectedHeader, headerValue);
    }
    
    @Test
    public void testGetHeader_WhenHeaderDoesNotExist_ReturnsNull() {
        String headerValue = response.getHeader("Invalid-Header");
        assertNull(headerValue);
    }
    
    @Test
    public void testGetBody_WhenBodyIsNotParsed_ReturnsParsedBody() {
        String expectedBody = "Sample response body";
        String body = response.getBody();
        assertEquals(expectedBody, body);
    }
    
    @Test
    public void testGetBody_WhenBodyIsParsed_ReturnsParsedBody() {
        String responseBody = "Sample response body";
        InputStream inputStream = new ByteArrayInputStream(responseBody.getBytes());
        Response parsedResponse = Mockito.spy(response);
        Mockito.doReturn(inputStream).when(parsedResponse).getStream();
        String expectedBody = "Sample response body";
        String body = parsedResponse.getBody();
        assertEquals(expectedBody, body);
    }
    
    @Test
    public void testGetStream_ReturnsResponseStream() {
        assertNotNull(response.getStream());
    }
    
    @After
    public void tearDown() {
        connection.disconnect();
    }
}