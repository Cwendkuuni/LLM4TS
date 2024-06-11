import static org.junit.Assert.*;
import org.junit.Test;

public class RequestTest {
    
    @Test
    public void testConstructor() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://www.example.com", request.getUrl());
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");
        
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("key", "value");
        
        assertEquals("value", request.getBodyParams().get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addQuerystringParameter("param", "value");
        
        assertEquals("value", request.getQueryStringParams().get("param"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload("example payload");
        
        assertEquals("example payload", request.getPayload());
    }
    
    @Test
    public void testAddPayload_byteArray() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    // More test cases for other methods
    
}