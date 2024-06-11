import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;

public class RequestTest {
    
    private Request request;
    
    @Before
    public void setUp() {
        request = new Request(Verb.GET, "https://example.com/api");
    }
    
    @Test
    public void testSend() {
        Response response = request.send();
        Assert.assertNotNull(response);
    }
    
    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        request.setConnectionKeepAlive(true);
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        
        request.send();
    }
    
    @Test
    public void testCreateConnection() {
        try {
            request.addQuerystringParameter("param1", "value1");
            request.addQuerystringParameter("param2", "value2");
            
            request.createConnection();
            
            Assert.assertNotNull(request.getQueryStringParams());
            Assert.assertEquals(2, request.getQueryStringParams().size());
        } catch (OAuthException ex) {
            Assert.fail("Failed to create connection: " + ex.getMessage());
        }
    }
    
    @Test
    public void testDoSendGET() {
        try {
            Response response = request.doSend();
            
            Assert.assertNotNull(response);
        } catch (Exception ex) {
            Assert.fail("Failed to send GET request: " + ex.getMessage());
        }
    }
    
    @Test
    public void testDoSendPUT() {
        request = new Request(Verb.PUT, "https://example.com/api");
        request.addHeader("Content-Type", "application/json");
        
        try {
            Response response = request.doSend();
            
            Assert.assertNotNull(response);
        } catch (Exception ex) {
            Assert.fail("Failed to send PUT request: " + ex.getMessage());
        }
    }
    
    @Test
    public void testDoSendPOST() {
        request = new Request(Verb.POST, "https://example.com/api");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        
        try {
            Response response = request.doSend();
            
            Assert.assertNotNull(response);
        } catch (Exception ex) {
            Assert.fail("Failed to send POST request: " + ex.getMessage());
        }
    }
    
    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer: token123");
        
        Assert.assertEquals(2, request.getHeaders().size());
        Assert.assertEquals("application/json", request.getHeaders().get("Content-Type"));
        Assert.assertEquals("Bearer: token123", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testAddBody() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        
        try {
            request.addBody(request.getConnection(), request.getByteBodyContents());
            Assert.assertEquals(28, request.getConnection().getRequestProperty("Content-Length"));
        } catch (Exception ex) {
            Assert.fail("Failed to add body to connection: " + ex.getMessage());
        }
    }
    
    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer: token123");
        
        Assert.assertEquals(2, request.getHeaders().size());
        Assert.assertEquals("application/json", request.getHeaders().get("Content-Type"));
        Assert.assertEquals("Bearer: token123", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        
        Assert.assertEquals(2, request.getBodyParams().size());
        Assert.assertEquals("value1", request.getBodyParams().get("param1"));
        Assert.assertEquals("value2", request.getBodyParams().get("param2"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        
        Assert.assertEquals(2, request.getQueryStringParams().size());
        Assert.assertEquals("value1", request.getQueryStringParams().get("param1"));
        Assert.assertEquals("value2", request.getQueryStringParams().get("param2"));
    }
    
    @Test
    public void testAddPayloadString() {
        request.addPayload("payload123");
        
        Assert.assertEquals("payload123", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByteArray() {
        byte[] payload = "payload123".getBytes();
        request.addPayload(payload);
        
        Assert.assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetUrl() {
        Assert.assertEquals("https://example.com/api", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        
        Assert.assertEquals("https://example.com/api", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        
        Assert.assertEquals("param1=value1&param2=value2", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        
        byte[] expected = "param1=value1&param2=value2".getBytes();
        byte[] actual = request.getByteBodyContents();
        
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetVerb() {
        Assert.assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer: token123");
        
        Assert.assertEquals(2, request.getHeaders().size());
        Assert.assertEquals("application/json", request.getHeaders().get("Content-Type"));
        Assert.assertEquals("Bearer: token123", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testGetCharset() {
        Assert.assertEquals(Charset.defaultCharset().name(), request.getCharset());
        
        request.setCharset("UTF-8");
        Assert.assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        
        Assert.assertEquals(5000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        
        Assert.assertEquals(5000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        request.setCharset("UTF-8");
        
        Assert.assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        Assert.assertTrue(request.getConnectionKeepAlive());
        
        request.setConnectionKeepAlive(false);
        Assert.assertFalse(request.getConnectionKeepAlive());
    }
}