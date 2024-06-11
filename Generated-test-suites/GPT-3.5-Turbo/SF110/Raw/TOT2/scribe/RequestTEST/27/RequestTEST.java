import static org.junit.Assert.*;
import org.junit.Test;

public class RequestTest {

    @Test
    public void testSend() {
        // Test sending a request
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        
        assertNotNull(response);
        // Add more assertions if needed
    }

    @Test
    public void testAddHeader() {
        // Test adding a header to the request
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        
        assertTrue(request.getHeaders().containsKey("Content-Type"));
        // Add more assertions if needed
    }

    // Add more test methods for other public methods in Request class

}