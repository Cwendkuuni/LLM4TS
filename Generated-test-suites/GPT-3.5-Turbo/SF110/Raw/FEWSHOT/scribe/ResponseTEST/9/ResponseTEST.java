import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    @Test
    public void testConstructor() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com")) {
            @Override
            public void connect() throws IOException {
                
            }

            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public InputStream getErrorStream() {
                return null;
            }
        };
        Response response = new Response(connection);
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        String bodyContents = response.parseBodyContents();
        Assert.assertEquals("", bodyContents);
    }
    
    @Test
    public void testParseHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = response.parseHeaders(null);
        Assert.assertEquals(new HashMap<String, String>(), headers);
    }
    
    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        response.getCode() = 200;
        boolean success = response.wasSuccessful();
        Assert.assertTrue(success);
        
        response.getCode() = 400;
        success = response.wasSuccessful();
        Assert.assertFalse(success);
    }
    
    @Test
    public void testGetBody() {
        Response response = new Response(null);
        String body = response.getBody();
        Assert.assertEquals("", body);
        
        response.body = "Sample Body";
        body = response.getBody();
        Assert.assertEquals("Sample Body", body);
    }

    @Test
    public void testGetStream() {
        Response response = new Response(null);
        InputStream stream = response.getStream();
        Assert.assertNull(stream);
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null);
        response.code = 200;
        int code = response.getCode();
        Assert.assertEquals(200, code);
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = response.getHeaders();
        Assert.assertEquals(new HashMap<String, String>(), headers);
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        response.headers.put("Content-Type", "application/json");
        String header = response.getHeader("Content-Type");
        Assert.assertEquals("application/json", header);
    }
}