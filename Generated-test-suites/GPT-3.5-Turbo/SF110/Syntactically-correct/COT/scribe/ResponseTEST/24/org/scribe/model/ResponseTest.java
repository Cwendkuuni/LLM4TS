
import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testConstructor() throws IOException {
        HttpURLConnection connection = null; // create a mock HttpURLConnection object
        Response response = new Response(connection);
        assertNotNull(response);
    }

    @Test
    public void testGetBody() throws IOException {
        HttpURLConnection connection = null; // create a mock HttpURLConnection object
        Response response = new Response(connection);
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof String);
    }

    @Test
    public void testGetStream() throws IOException {
        HttpURLConnection connection = null; // create a mock HttpURLConnection object
        Response response = new Response(connection);
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() throws IOException {
        HttpURLConnection connection = null; // create a mock HttpURLConnection object
        Response response = new Response(connection);
        assertNotNull(response.getCode());
        assertTrue(response.getCode() instanceof Integer);
    }

    @Test
    public void testGetHeaders() throws IOException {
        HttpURLConnection connection = null; // create a mock HttpURLConnection object
        Response response = new Response(connection);
        assertNotNull(response.getHeaders());
        assertTrue(response.getHeaders() instanceof Map);
    }

    @Test
    public void testGetHeader() throws IOException {
        HttpURLConnection connection = null; // create a mock HttpURLConnection object
        Response response = new Response(connection);
        assertNull(response.getHeader("non-existing"));
    }

}
