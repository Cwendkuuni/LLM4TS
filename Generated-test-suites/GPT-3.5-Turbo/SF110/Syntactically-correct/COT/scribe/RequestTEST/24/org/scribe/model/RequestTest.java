import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testSend() {
        // Test case for send()
        // Test the functionality of the send method
        // This method should create a connection and send a request
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() {
        // Test case for createConnection()
        // Test the functionality of createConnection method
        // This method should create an HttpURLConnection object and set URL properties
        Request request = new Request(Verb.GET, "https://www.example.com");
        try {
            request.createConnection();
        } catch (Exception e) {
            fail("Exception thrown while creating connection");
        }
        assertNotNull(request.getConnection());
    }
    
    @Test
    public void testDoSend() {
        // Test case for doSend()
        // Test the functionality of the doSend method
        // This method should set request method, add headers, and handle request body for PUT and POST methods
        Request request = new Request(Verb.POST, "https://www.example.com");
        try {
            request.createConnection();
        } catch (Exception e) {
            fail("Exception thrown while creating connection");
        }
        try {
            Response response = request.doSend();
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception thrown while sending request");
        }
    }
    
    @Test
    public void testAddHeaders() {
        // Test case for addHeaders()
        // Test the functionality of the addHeaders method
        // This method should set request headers in the HttpURLConnection object
        Request request = new Request(Verb.GET, "https://www.example.com");
        try {
            request.createConnection();
        } catch (Exception e) {
            fail("Exception thrown while creating connection");
        }
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer myToken");
        assertEquals("application/json", request.getConnection().getRequestProperty("Content-Type"));
        assertEquals("Bearer myToken", request.getConnection().getRequestProperty("Authorization"));
    }
    
    @Test
    public void testAddBody() {
        // Test case for addBody()
        // Test the functionality of the addBody method
        // This method should set request body in the HttpURLConnection object
        Request request = new Request(Verb.POST, "https://www.example.com");
        try {
            request.createConnection();
        } catch (Exception e) {
            fail("Exception thrown while creating connection");
        }
        byte[] content = "test content".getBytes();
        try {
            request.addBody(request.getConnection(), content);
        } catch (Exception e) {
            fail("Exception thrown while adding body");
        }
        assertEquals(String.valueOf(content.length), request.getConnection().getRequestProperty("Content-Length"));
        assertNotNull(request.getConnection().getOutputStream());
    }
    
    // More test cases can be added for other methods
    
}