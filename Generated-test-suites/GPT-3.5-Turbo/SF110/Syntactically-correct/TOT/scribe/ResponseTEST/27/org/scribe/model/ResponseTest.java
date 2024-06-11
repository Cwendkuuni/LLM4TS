import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(mockConnection(200, "Test Body", new HashMap<>()));
        Assert.assertEquals("Test Body", response.getBody());
    }

    @Test
    public void testGetBodyWithNullBody() {
        Response response = new Response(mockConnection(200, null, new HashMap<>()));
        Assert.assertEquals("", response.getBody());
    }

    @Test
    public void testGetBodyWithIOException() {
        Response response = new Response(mockConnectionWithIOException());
        Assert.assertEquals("", response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(mockConnection(200, "Test Body", new HashMap<>()));
        Assert.assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(mockConnection(200, "Test Body", new HashMap<>()));
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "max-age=3600");
        Response response = new Response(mockConnection(200, "Test Body", expectedHeaders));
        Assert.assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "max-age=3600");
        Response response = new Response(mockConnection(200, "Test Body", headers));
        Assert.assertEquals("application/json", response.getHeader("Content-Type"));
    }

    private HttpURLConnection mockConnection(int responseCode, String responseMessage, Map<String, String> headers) {
        return new MockHttpURLConnection(responseCode, responseMessage, headers);
    }

    private HttpURLConnection mockConnectionWithIOException() {
        return new MockHttpURLConnectionWithIOException();
    }
}