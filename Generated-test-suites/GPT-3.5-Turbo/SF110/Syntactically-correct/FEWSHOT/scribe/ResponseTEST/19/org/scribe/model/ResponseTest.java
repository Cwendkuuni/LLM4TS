import org.junit.Before;
import org.junit.Test;
import org.scribe.utils.StreamUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {

    private HttpURLConnection mockConnection;
    private Response response;

    @Before
    public void setUp() throws IOException {
        mockConnection = new HttpURLConnection(new URL("https://www.example.com")) {
            @Override
            public void disconnect() {

            }

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {

            }
        };
    }

    @Test
    public void testConstructor_successfulConnection() throws IOException {
        response = new Response(mockConnection);
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNull(response.getBody());
        assertNotNull(response.getStream());
    }

    @Test
    public void testConstructor_failedConnection() throws IOException {
        mockConnection = new HttpURLConnection(new URL("https://www.nonexistent.com")) {
            @Override
            public void disconnect() {

            }

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {
                throw new UnknownHostException();
            }
        };
        response = new Response(mockConnection);
        assertNotNull(response);
        assertEquals(404, response.getCode());
        assertNotNull(response.getHeaders());
        assertEquals("", response.getBody());
        assertNull(response.getStream());
    }

    @Test
    public void testParseBodyContents_withBody() {
        InputStream inputStream = new ByteArrayInputStream("This is the response body".getBytes());
        response = new Response(mockConnection);
        response = new Response(mockConnection);
        try {
            mockConnection.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("This is the response body", response.parseBodyContents());
        assertEquals("This is the response body", response.getBody());
    }

    @Test
    public void testParseBodyContents_withoutBody() {
        InputStream inputStream = new ByteArrayInputStream("".getBytes());
        response = new Response(mockConnection);
        try {
            mockConnection.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("", response.parseBodyContents());
        assertEquals("", response.getBody());
    }

    @Test
    public void testParseHeaders() {
        InputStream inputStream = new ByteArrayInputStream("".getBytes());
        try {
            mockConnection.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response = new Response(mockConnection);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        headers.put("Content-Length", "100");
        headers.put("Server", "Apache");

        assertEquals(headers, response.parseHeaders(mockConnection));
        assertEquals("text/plain", response.getHeaders().get("Content-Type"));
    }

    @Test
    public void testWasSuccessful_withSuccessCode() {
        response = new Response(mockConnection);
        response = new Response(mockConnection);

        response.setCode(200);
        assertTrue(response.wasSuccessful());

        response.setCode(300);
        assertTrue(response.wasSuccessful());

        response.setCode(399);
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessful_withFailureCode() {
        response = new Response(mockConnection);
        response = new Response(mockConnection);

        response.setCode(400);
        assertFalse(response.wasSuccessful());

        response.setCode(500);
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        headers.put("Content-Length", "100");
        headers.put("Server", "Apache");
        response = new Response(mockConnection);
        response.setHeaders(headers);

        assertEquals("text/plain", response.getHeader("Content-Type"));
        assertNull(response.getHeader("Invalid-Header"));
    }
}