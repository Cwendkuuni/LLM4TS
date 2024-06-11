import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;
import org.scribe.utils.URLUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {
    
    @Test
    public void testSend() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Exercise
        Response response = request.send();

        // Verify
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Exercise
        try {
            request.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred while creating connection");
        }

        // Verify
        assertNotNull(request.getConnection());
    }
    
    @Test
    public void testDoSend() {
        // Setup
        Verb verb = Verb.POST;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Exercise
        try {
            Response response = request.doSend();

            // Verify
            assertNotNull(response);
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred while sending request");
        }
    }
    
    @Test
    public void testAddHeaders() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred while opening connection");
        }
        byte[] content = {};

        // Exercise
        request.addHeaders(conn);
        // Verify
        assertEquals(request.getHeaders(), conn.getRequestProperties());
    }
    
    @Test
    public void testAddBody() {
        // Setup
        Verb verb = Verb.PUT;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred while opening connection");
        }
        byte[] content = {};

        // Exercise
        try {
            request.addBody(conn, content);
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred while adding body to connection");
        }

        // Verify
        String expectedContentLength = String.valueOf(content.length);
        assertEquals(expectedContentLength, conn.getRequestProperty("Content-Length"));
    }
    
    @Test
    public void testAddHeader() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String key = "Content-Type";
        String value = "application/json";

        // Exercise
        request.addHeader(key, value);

        // Verify
        assertTrue(request.getHeaders().containsKey(key));
        assertEquals(value, request.getHeaders().get(key));
    }
    
    @Test
    public void testAddBodyParameter() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String key = "param";
        String value = "value";

        // Exercise
        request.addBodyParameter(key, value);

        // Verify
        assertTrue(request.getBodyParams().containsKey(key));
        assertEquals(value, request.getBodyParams().get(key));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String key = "param";
        String value = "value";

        // Exercise
        request.addQuerystringParameter(key, value);

        // Verify
        assertTrue(request.getQueryStringParams().containsKey(key));
        assertEquals(value, request.getQueryStringParams().get(key));
    }
    
    @Test
    public void testAddPayload() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String payload = "example payload";

        // Exercise
        request.addPayload(payload);

        // Verify
        assertEquals(payload, request.getPayload());
    }
    
    @Test
    public void testAddBytePayload() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        byte[] payload = {1, 2, 3};

        // Exercise
        request.addPayload(payload);

        // Verify
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetQueryStringParams() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String key1 = "param1";
        String value1 = "value1";
        String queryString = key1 + "=" + value1;
        
        try {
            URLUtils.queryStringToMap(queryString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("An exception occurred while converting query string to map");
        }

        // Exercise
        Map<String, String> queryParams = request.getQueryStringParams();

        // Verify
        assertTrue(queryParams.containsKey(key1));
        assertEquals(value1, queryParams.get(key1));
    }
    
    @Test
    public void testGetBodyParams() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String key1 = "param1";
        String value1 = "value1";
        
        request.addBodyParameter(key1, value1);

        // Exercise
        Map<String, String> bodyParams = request.getBodyParams();

        // Verify
        assertTrue(bodyParams.containsKey(key1));
        assertEquals(value1, bodyParams.get(key1));
    }
    
    @Test
    public void testGetUrl() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Exercise
        String actualUrl = request.getUrl();

        // Verify
        assertEquals(url, actualUrl);
    }
    
    @Test
    public void testGetSanitizedUrl() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com:8080?param1=value1";
        Request request = new Request(verb, url);
        
        String expectedSanitizedUrl = "http://example.com";

        // Exercise
        String actualSanitizedUrl = request.getSanitizedUrl();

        // Verify
        assertEquals(expectedSanitizedUrl, actualSanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String charset = "UTF-8";
        String bodyContents = "example body contents";
        
        request.setCharset(charset);
        request.addPayload(bodyContents);

        // Exercise
        String actualBodyContents = request.getBodyContents();

        // Verify
        assertEquals(bodyContents, actualBodyContents);
    }
    
    @Test
    public void testGetByteBodyContents() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String charset = "UTF-8";
        byte[] bodyContents = {1, 2, 3};
        
        request.setCharset(charset);
        request.addPayload(bodyContents);

        // Exercise
        byte[] actualByteBodyContents = request.getByteBodyContents();

        // Verify
        assertArrayEquals(bodyContents, actualByteBodyContents);
    }
    
    @Test
    public void testGetVerb() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        // Exercise
        Verb actualVerb = request.getVerb();

        // Verify
        assertEquals(verb, actualVerb);
    }
    
    @Test
    public void testGetHeaders() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String key1 = "Content-Type";
        String value1 = "application/json";
        
        request.addHeader(key1, value1);

        // Exercise
        Map<String, String> headers = request.getHeaders();

        // Verify
        assertTrue(headers.containsKey(key1));
        assertEquals(value1, headers.get(key1));
    }
    
    @Test
    public void testGetCharset_defaultCharset() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String defaultCharset = Charset.defaultCharset().name();

        // Exercise
        String actualCharset = request.getCharset();

        // Verify
        assertEquals(defaultCharset, actualCharset);
    }
    
    @Test
    public void testGetCharset_customCharset() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String customCharset = "ISO-8859-1";
        
        request.setCharset(customCharset);

        // Exercise
        String actualCharset = request.getCharset();

        // Verify
        assertEquals(customCharset, actualCharset);
    }
    
    @Test
    public void testSetConnectTimeout() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Exercise
        request.setConnectTimeout(duration, unit);

        // Verify
        assertEquals(duration, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Exercise
        request.setReadTimeout(duration, unit);

        // Verify
        assertEquals(duration, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String charset = "UTF-8";

        // Exercise
        request.setCharset(charset);

        // Verify
        assertEquals(charset, request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        boolean connectionKeepAlive = true;

        // Exercise
        request.setConnectionKeepAlive(connectionKeepAlive);

        // Verify
        assertTrue(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred while opening connection");
        }

        // Exercise
        request.setConnection(connection);

        // Verify
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        // Setup
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);
        
        String expectedToString = "@Request(GET http://example.com)";

        // Exercise
        String actualToString = request.toString();

        // Verify
        assertEquals(expectedToString, actualToString);
    }
}