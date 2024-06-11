import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthException;

public class RequestTest {
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        try {
            request.createConnection();
        } catch (IOException e) {
            fail("Exception thrown while creating connection");
        }
        
        assertNotNull(request.connection);
    }
    
    @Test
    public void testDoSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        try {
            Response response = request.doSend();
            assertNotNull(response);
        } catch (IOException e) {
            fail("Exception thrown while sending request");
        }
    }
    
    // Add more test cases for other public methods
    
}