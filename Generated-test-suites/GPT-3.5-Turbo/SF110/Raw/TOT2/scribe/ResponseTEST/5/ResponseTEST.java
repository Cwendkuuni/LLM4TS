import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Response;
import java.net.HttpURLConnection;
import java.io.*;
import org.junit.Before;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    private Response response1;
    private Response response2;
    
    @Before
    public void setUp() {
        HttpURLConnection connection1 = mock(HttpURLConnection.class);
        when(connection1.getResponseCode()).thenReturn(200);
        when(connection1.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());
        
        HttpURLConnection connection2 = mock(HttpURLConnection.class);
        when(connection2.getResponseCode()).thenReturn(404);
        when(connection2.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());
        
        response1 = new Response(connection1);
        response2 = new Response(connection2);
    }
    
    @Test
    public void testGetBody_Successful() {
        assertEquals("", response1.getBody());
    }
    
    @Test
    public void testGetBody_NotSuccessful() {
        assertEquals("", response2.getBody());
    }
    
    @Test
    public void testGetStream() {
        assertNotNull(response1.getStream());
    }
    
    @Test
    public void testGetCode() {
        assertEquals(200, response1.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        assertNotNull(response1.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        assertEquals("", response1.getHeader("TestHeader"));
    }
    
    @Test
    public void testGetHeader_Null() {
        assertNull(response1.getHeader("NonExistentHeader"));
    }
}