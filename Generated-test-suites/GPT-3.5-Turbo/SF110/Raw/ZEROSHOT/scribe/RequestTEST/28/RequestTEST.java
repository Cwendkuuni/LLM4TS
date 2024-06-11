import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.*;

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
            HttpURLConnection connection = request.getConnection();
            assertNotNull(connection);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testDoSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        try {
            request.createConnection();
            Response response = request.doSend();
            assertNotNull(response);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = null;
        try {
            request.createConnection();
            connection = request.getConnection();
            request.addHeaders(connection);
            Map<String, String> headers = request.getHeaders();
            assertEquals(headers.size(), connection.getRequestProperties().size());
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                List<String> headerValues = connection.getRequestProperties().get(entry.getKey());
                assertTrue(headerValues.contains(entry.getValue()));
            }
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testAddBody() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        HttpURLConnection connection = null;
        try {
            request.createConnection();
            connection = request.getConnection();
            byte[] content = "Body Content".getBytes();
            request.addBody(connection, content);
            assertEquals(String.valueOf(content.length), connection.getHeaderField("Content-Length"));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Header1", "Value1");
        request.addHeader("Header2", "Value2");
        Map<String, String> headers = request.getHeaders();
        assertEquals(2, headers.size());
        assertEquals("Value1", headers.get("Header1"));
        assertEquals("Value2", headers.get("Header2"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(2, bodyParams.size());
        assertEquals("value1", bodyParams.get("param1"));
        assertEquals("value2", bodyParams.get("param2"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals(2, queryStringParams.size());
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    @Test
    public void testAddPayloadString() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        String payload = "This is a payload";
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testAddPayloadBytes() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "This is a payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals(2, queryStringParams.size());
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(2, bodyParams.size());
        assertEquals("value1", bodyParams.get("param1"));
        assertEquals("value2", bodyParams.get("param2"));
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com?a=1&b=2");
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        String payload = "This is a payload";
        request.addPayload(payload);
        assertEquals(payload, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "This is a payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Header1", "Value1");
        request.addHeader("Header2", "Value2");
        Map<String, String> headers = request.getHeaders();
        assertEquals(2, headers.size());
        assertEquals("Value1", headers.get("Header1"));
        assertEquals("Value2", headers.get("Header2"));
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnection().getHeaderField("http.keepAlive").equals("true"));
    }

    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = null;
        try {
            request.createConnection();
            connection = request.getConnection();
            request.setConnection(connection);
            assertEquals(connection, request.getConnection());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}