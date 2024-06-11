import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {
  
    private HttpURLConnection mockConnection;
    private Response response;

    @Before
    public void setup() {
        mockConnection = Mockito.mock(HttpURLConnection.class);
        response = new Response(mockConnection);
    }

    @Test
    public void testConstructor_UnknownHostException() throws IOException {
        Mockito.when(mockConnection.getResponseCode()).thenThrow(UnknownHostException.class);
        response = new Response(mockConnection);

        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getStream());
        assertNotNull(response.getHeaders());
        assertTrue(response.getHeaders().isEmpty());
    }

    @Test
    public void testConstructor_SuccessfulResponse() throws IOException {
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        Mockito.when(mockConnection.getResponseCode()).thenReturn(200);
        Mockito.when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        response = new Response(mockConnection);

        assertEquals(200, response.getCode());
        assertEquals(mockInputStream, response.getStream());
        assertNotNull(response.getHeaders());
        assertFalse(response.getHeaders().isEmpty());
        // Add additional assertions for specific headers if necessary
    }

    @Test
    public void testParseBodyContents() {
        InputStream mockInputStream = Mockito.mock(InputStream.class);
        Mockito.when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        // Add test case for parsing body contents
        // Assert the parsed body contents

    }

    @Test
    public void testParseHeaders() {
        // Add test case for parsing headers
        // Assert the parsed headers

    }

    @Test
    public void testWasSuccessful() {
        // Add test cases for both successful and unsuccessful responses
        // Assert the expected return value of wasSuccessful()

    }

    @Test
    public void testGetBody() {
        // Add test case for retrieving body without parsing
        // Assert the expected body value

        // Add test case for retrieving body after parsing
        // Assert the expected body value

    }

    @Test
    public void testGetStream() {
        // Add test case for retrieving stream
        // Assert the expected stream value

    }

    @Test
    public void testGetCode() {
        // Add test case for retrieving code
        // Assert the expected code value

    }

    @Test
    public void testGetHeaders() {
        // Add test case for retrieving headers
        // Assert the expected headers value

    }

    @Test
    public void testGetHeader() {
        // Add test case for retrieving specific header
        // Assert the expected header value

        // Add test case for retrieving non-existing header
        // Assert null value

    }
}