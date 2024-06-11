import org.junit.Test;
import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.io.IOException;

public class ResponseTest {

    @Test
    public void testResponseConstructor() {
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com"));
            Response response = new Response(connection);
            assertNotNull(response);
            assertEquals(200, response.getCode());
        } catch (IOException e) {
            fail("IOException thrown during test");
        }
    }

    @Test
    public void testParseBodyContents() {
        Response response = new Response(new HttpURLConnection(new URL("http://www.example.com")));
        assertEquals("", response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() {
        Response response = new Response(new HttpURLConnection(new URL("http://www.example.com")));
        assertEquals(1, response.getHeaders().size());
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(new HttpURLConnection(new URL("http://www.example.com")));
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        Response response = new Response(new HttpURLConnection(new URL("http://www.example.com")));
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(new HttpURLConnection(new URL("http://www.example.com")));
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(new HttpURLConnection(new URL("http://www.example.com")));
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(new HttpURLConnection(new URL("http://www.example.com")));
        assertNull(response.getHeader("NonExistentHeader"));
    }
}