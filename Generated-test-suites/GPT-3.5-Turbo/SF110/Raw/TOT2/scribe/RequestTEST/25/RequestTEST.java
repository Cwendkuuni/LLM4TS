import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.model.Response;
import org.scribe.exceptions.OAuthException;

public class RequestTest {
    
    private Request request;
    
    @Before
    public void setup() {
        request = new Request(Verb.GET, "https://www.example.com");
    }
    
    @Test
    public void testSend() {
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("param1", "value1");
        assertEquals("value1", request.getBodyParams().get("param1"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param2", "value2");
        assertEquals("value2", request.getQueryStringParams().get("param2"));
    }
    
    @Test
    public void testAddPayload_String() {
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }
    
    @Test
    public void testAddPayload_byteArray() {
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        assertEquals("https://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        assertEquals("param1=value1", request.getBodyContents());
    }
    
    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        // No assertion as this method just sets a timeout
    }
    
    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(10, TimeUnit.SECONDS);
        // No assertion as this method just sets a timeout
    }
    
    @Test
    public void testSetCharset() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testExceptionHandling() {
        Request invalidRequest = new Request(null, null);
        try {
            invalidRequest.send();
            fail("Expected an OAuthException to be thrown");
        } catch (OAuthException e) {
            // Expected exception
        }
    }
}