import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {

    private Response response;
    private HttpURLConnection mockConnection;
    private InputStream mockInputStream;

    @Before
    public void setup() throws IOException {
        mockConnection = mock(HttpURLConnection.class);
        mockInputStream = mock(InputStream.class);
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);
        when(mockConnection.getErrorStream()).thenReturn(mockInputStream);
        response = new Response(mockConnection);
    }

    @Test
    public void testGetBody() {
        when(mockConnection.getResponseCode()).thenReturn(200);
        assertEquals("", response.getBody());
        when(mockConnection.getResponseCode()).thenReturn(404);
        assertNull(response.getBody());
    }

    @Test
    public void testGetStream() {
        assertEquals(mockInputStream, response.getStream());
    }

    @Test
    public void testGetCode() {
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        assertNull(response.getHeader("name"));
        response.getHeaders().put("name", "value");
        assertEquals("value", response.getHeader("name"));
    }

    @Test
    public void testParseBodyContents() throws IOException {
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockInputStream.toString()).thenReturn("body");
        assertEquals("body", response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("key", "value");
        when(mockConnection.getHeaderFields()).thenReturn(expectedHeaders);
        assertEquals(expectedHeaders, response.parseHeaders(mockConnection));
    }

    @Test
    public void testWasSuccessful() {
        when(mockConnection.getResponseCode()).thenReturn(200);
        assertEquals(true, response.wasSuccessful());
        when(mockConnection.getResponseCode()).thenReturn(400);
        assertEquals(false, response.wasSuccessful());
    }
}