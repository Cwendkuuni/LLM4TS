import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    private Request request;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "http://www.example.com");
    }

    @Test
    public void testSend() {
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() {
        try {
            request.createConnection();
            assertTrue(request.getConnection() != null);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDoSend() {
        try {
            request.createConnection();
            Response response = request.doSend();
            assertNotNull(response);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");

        assertEquals(expectedHeaders, request.getHeaders());
    }

    @Test
    public void testAddBody() {
        try {
            request.createConnection();
            request.addBody("Test Body");
            assertEquals("Test Body", request.getBodyContents());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        Map<String, String> expectedBodyParams = new HashMap<>();
        expectedBodyParams.put("param1", "value1");
        expectedBodyParams.put("param2", "value2");

        assertEquals(expectedBodyParams, request.getBodyParams());
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> expectedQueryStringParams = new HashMap<>();
        expectedQueryStringParams.put("param1", "value1");
        expectedQueryStringParams.put("param2", "value2");

        assertEquals(expectedQueryStringParams, request.getQueryStringParams());
    }

    @Test
    public void testAddPayloadString() {
        request.addPayload("Test Payload");
        assertEquals("Test Payload", request.getPayload());
    }

    @Test
    public void testAddPayloadByteArray() {
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        request.setCharset("ISO-8859-1");
        assertEquals("ISO-8859-1", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }
}