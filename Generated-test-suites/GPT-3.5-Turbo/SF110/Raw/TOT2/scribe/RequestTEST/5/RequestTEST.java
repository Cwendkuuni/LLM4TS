import static org.junit.Assert.*;
import org.junit.Test;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        try {
            request.createConnection();
            assertNotNull(request.getConnection());
        } catch (IOException e) {
            fail("Exception thrown during createConnection");
        }
    }

    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        HttpURLConnection connection = new HttpURLConnection();
        request.addHeader("key", "value");
        assertEquals("value", connection.getRequestProperty("key"));
    }

    @Test
    public void testAddBody() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        HttpURLConnection connection = new HttpURLConnection();
        byte[] content = "test".getBytes();
        try {
            request.addBody(connection, content);
            assertEquals("4", connection.getRequestProperty("Content-Length"));
        } catch (IOException e) {
            fail("Exception thrown during addBody");
        }
    }

    // Add more test cases for other methods in the Request class

}