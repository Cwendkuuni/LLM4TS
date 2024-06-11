import static org.junit.Assert.*;
import org.junit.Test;

public class RequestTest {
    
    @Test
    public void testSend() {
        // Typical use case
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertNotNull(response);
        
        // Edge cases
        // ...
        
        // Error scenarios
        // ...
    }
    
    @Test
    public void testAddHeader() {
        // Typical use case
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        
        // Edge cases
        // ...
        
        // Error scenarios
        // ...
    }
    
    // Include test cases for other public methods as well
    
}