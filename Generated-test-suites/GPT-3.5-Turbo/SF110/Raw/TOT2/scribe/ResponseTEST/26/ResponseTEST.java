import org.junit.Test;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(mockHttpURLConnection());
        assertEquals("", response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(mockHttpURLConnection());
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(mockHttpURLConnection());
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(mockHttpURLConnection());
        assertNotNull(response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(mockHttpURLConnection());
        assertNull(response.getHeader("NonExistingHeader"));
    }

    private HttpURLConnection mockHttpURLConnection() {
        return new HttpURLConnection() {
            @Override
            public void connect() throws IOException {}

            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Test Body".getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return null;
            }

            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headerFields = new HashMap<>();
                List<String> headerValue = new ArrayList<>();
                headerValue.add("TestHeaderValue");
                headerFields.put("TestHeader", headerValue);
                return headerFields;
            }
            
            // Other abstract methods can be implemented as needed
        };
    }
}