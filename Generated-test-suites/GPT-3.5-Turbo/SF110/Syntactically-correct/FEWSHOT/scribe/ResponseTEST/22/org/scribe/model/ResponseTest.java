
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;

public class ResponseTest {

    private HttpURLConnection connection;
    private Response response;

    @Before
    public void setup() throws IOException {
        connection = mock(HttpURLConnection.class);
        response = new Response(connection);
    }

    @Test
    public void testConstructorSuccessful() throws IOException {
        when(connection.getResponseCode()).thenReturn(200);
        response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testConstructorError() throws IOException {
        when(connection.getResponseCode()).thenReturn(404);
        response = new Response(connection);
        assertEquals(404, response.getCode());
    }

    @Test
    public void testParseHeaders() throws IOException {
        when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put("header1", Arrays.asList("value1"));
            put("header2", Arrays.asList("value21", "value22"));
        }});

        Map<String, String> expectedHeaders = new HashMap<String, String>() {{
            put("header1", "value1");
            put("header2", "value21");
        }};

        response = new Response(connection);
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testWasSuccessful() {
        response = new Response(connection);

        response.getCode() = 200;
        assertEquals(true, response.wasSuccessful());

        response.getCode() = 399;
        assertEquals(true, response.wasSuccessful());

        response.getCode() = 400;
        assertEquals(false, response.wasSuccessful());

        response.getCode() = 500;
        assertEquals(false, response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        response = new Response(connection);

        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("test body".getBytes()));
        assertEquals("test body", response.getBody());
    }

    @Test
    public void testGetBodyNull() {
        response = new Response(connection);

        when(connection.getInputStream()).thenReturn(null);
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetStream() {
        response = new Response(connection);

        ByteArrayInputStream inputStream = new ByteArrayInputStream("test body".getBytes());
        when(connection.getInputStream()).thenReturn(inputStream);
        assertEquals(inputStream, response.getStream());
    }

    @Test
    public void testGetCode() throws IOException {
        when(connection.getResponseCode()).thenReturn(200);
        response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() throws IOException {
        when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put("header1", Arrays.asList("value1"));
            put("header2", Arrays.asList("value21", "value22"));
        }});

        Map<String, String> expectedHeaders = new HashMap<String, String>() {{
            put("header1", "value1");
            put("header2", "value21");
        }};

        response = new Response(connection);
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() throws IOException {
        when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>() {{
            put("header1", Arrays.asList("value1"));
            put("header2", Arrays.asList("value2"));
        }});

        response = new Response(connection);

        assertEquals("value1", response.getHeader("header1"));
        assertEquals("value2", response.getHeader("header2"));
    }
}
