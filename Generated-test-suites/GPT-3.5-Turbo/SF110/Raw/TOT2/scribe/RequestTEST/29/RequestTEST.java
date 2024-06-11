import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testAddHeader() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    // Add similar test cases for other public methods

    @Test
    public void testAddPayload_String() {
        // Edge case: Empty payload
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addPayload("");
        assertEquals("", request.getPayload());
    }

    @Test
    public void testAddBodyParameter() {
        // Error scenario: Duplicate key in body params
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addBodyParameter("key", "value1");
        request.addBodyParameter("key", "value2");
        assertEquals("value2", request.getBodyParams().get("key"));
    }

    // Add similar test cases for other identified edge cases and exceptions

}