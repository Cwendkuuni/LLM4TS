import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;

public class ResponseTest {

    private Response response;
    private HttpURLConnection connection;

    @Before
    public void setUp() throws IOException {
        connection = new HttpURLConnection(new URL("http://www.example.com"));
        response = new Response(connection);
    }

    @Test
    public void testGetBody() {
        String body = response.getBody();
        assertNotNull(body);
    }

    @Test
    public void testGetStream() {
        InputStream stream = response.getStream();
        assertNotNull(stream);
    }

    @Test
    public void testGetCode() {
        int code = response.getCode();
        assertTrue(code >= 200 && code < 400);
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(1, headers.size());
    }

    @Test
    public void testGetHeader() {
        String header = response.getHeader("Content-Type");
        assertNotNull(header);
        assertEquals("text/html", header);
    }

    @Test
    public void testParseBodyContents() {
        InputStream stream = new ByteArrayInputStream("Test Body".getBytes());
        response.stream = stream;
        String body = response.parseBodyContents();
        assertNotNull(body);
        assertEquals("Test Body", body);
    }

    @Test
    public void testWasSuccessful() {
        response.code = 200;
        assertTrue(response.wasSuccessful());
    }

    @Test(expected = IOException.class)
    public void testResponseConstructorUnknownHostException() throws IOException {
        HttpURLConnection invalidConnection = new HttpURLConnection(new URL("http://www.invalidurl.com"));
        response = new Response(invalidConnection);
        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
    }

}