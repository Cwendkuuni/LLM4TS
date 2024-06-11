import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() {
        HttpURLConnection connection = null; // Initialize HttpURLConnection object
        response = new Response(connection);
    }

    @Test
    public void testParseBodyContents() {
        String expectedBody = "Test Body";
        response.getBody();
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");

        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testWasSuccessful() {
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        String expectedBody = "Test Body";
        response.getBody();
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetStream() {
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        int expectedCode = 200;
        assertEquals(expectedCode, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        assertNotNull(response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        String expectedHeaderValue = "application/json";
        assertEquals(expectedHeaderValue, response.getHeader("Content-Type"));
    }
}