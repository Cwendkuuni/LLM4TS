import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testParseBodyContents() {
        // Test case for parseBodyContents method
        // Add test case here
    }

    @Test
    public void testParseHeaders() {
        // Test case for parseHeaders method
        // Add test case here
    }

    @Test
    public void testWasSuccessful() {
        // Test case for wasSuccessful method
        // Add test case here
    }

    @Test
    public void testGetBody() {
        // Test case for getBody method
        // Add test case here
    }

    @Test
    public void testGetStream() {
        // Test case for getStream method
        // Add test case here
    }

    @Test
    public void testGetCode() {
        // Test case for getCode method
        // Add test case here
    }

    @Test
    public void testGetHeaders() {
        // Test case for getHeaders method
        // Add test case here
    }

    @Test
    public void testGetHeader() {
        // Test case for getHeader method
        // Add test case here
    }

    @Test
    public void testExceptionHandling() {
        try {
            HttpURLConnection connection = null;
            Response response = new Response(connection);
            fail("Exception not thrown for null connection");
        } catch (IOException e) {
            // Exception handling test case
            assertTrue(true);
        }
    }

    @Test
    public void testEdgeCases() {
        // Test edge cases here
    }
}