
import org.junit.*;
import static org.junit.Assert.*;

public class ResponseTest {

    private HttpURLConnection mockConnection;

    @Before
    public void setUp() {
        mockConnection = Mockito.mock(HttpURLConnection.class);
    }

    @Test
    public void testGetBody() {
        Response response = new Response(mockConnection);
        String body = response.getBody();
        assertEquals("", body);
    }

    @Test
    public void testGetStream() {
        Response response = new Response(mockConnection);
        InputStream stream = response.getStream();
        assertNotNull(stream);
    }

    @Test
    public void testGetCode() {
        Response response = new Response(mockConnection);
        int code = response.getCode();
        assertEquals(0, code);
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(mockConnection);
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(mockConnection);
        String header = response.getHeader("name");
        assertNull(header);
    }
}
