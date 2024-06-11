import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RequestTest {

    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://www.example.com");
    }

    @Test
    public void send_ValidUrl_SuccessfulResponse() {
        Response response = request.send();
        assertNotNull(response);
        assertEquals(200, response.getCode());
    }

    @Test(expected = OAuthException.class)
    public void send_ConnectionError_ThrowOAuthException() {
        request.setConnectTimeout(1, TimeUnit.MILLISECONDS);
        request.send();
    }

    @Test
    public void addHeader_ValidKeyValue_SuccessfullyAdded() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void addBodyParameter_ValidKeyValue_SuccessfullyAdded() {
        request.addBodyParameter("username", "testuser");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("testuser", bodyParams.get("username"));
    }

    @Test
    public void addQuerystringParameter_ValidKeyValue_SuccessfullyAdded() {
        request.addQuerystringParameter("sortBy", "name");
        Map<String, String> queryParams = request.getQueryStringParams();
        assertTrue(queryParams.containsKey("sortBy"));
        assertEquals("name", queryParams.get("sortBy"));
    }

    @Test
    public void addPayload_ValidPayload_SuccessfullyAdded() {
        request.addPayload("This is a test payload");
        assertEquals("This is a test payload", request.getBodyContents());
    }

    @Test
    public void addPayload_ValidBytePayload_SuccessfullyAdded() {
        byte[] payload = "This is a test payload".getBytes();
        request.addPayload(payload);
        assertEquals("This is a test payload", new String(request.getByteBodyContents()));
    }

    @Test
    public void getUrl_ValidUrl_ReturnsUrl() {
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void getSanitizedUrl_ValidUrl_ReturnsUrlWithoutQuery() {
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void getBodyContents_ValidContents_ReturnsStringContents() {
        request.addPayload("This is a test payload");
        assertEquals("This is a test payload", request.getBodyContents());
    }

    @Test
    public void getByteBodyContents_ValidContents_ReturnsByteContents() {
        byte[] payload = "This is a test payload".getBytes();
        request.addPayload(payload);
        assertEquals("This is a test payload", new String(request.getByteBodyContents()));
    }

    @Test
    public void getVerb_ValidVerb_ReturnsVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void getHeaders_ValidHeaders_ReturnsHeaders() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void getCharset_NoCharsetSet_ReturnsDefaultCharset() {
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void setConnectionKeepAlive_KeepAliveSet_ConnectionKeptAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void setConnection_ConnectionSet_ConnectionSuccessfullySet() throws ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://www.example.com").openConnection();
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }

    @Test
    public void toString_ValidRequest_ReturnsStringRepresentation() {
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }

    @Test
    public void setConnectTimeout_ValidDuration_SetSuccessfully() {
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getConnectTimeout());
    }

    @Test
    public void setReadTimeout_ValidDuration_SetSuccessfully() {
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getReadTimeout());
    }

    @Test
    public void setCharset_ValidCharset_SetSuccessfully() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void getQueryStringParams_ValidParams_ReturnsAllParams() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> queryParams = request.getQueryStringParams();
        assertEquals(2, queryParams.size());
        assertTrue(queryParams.containsKey("param1"));
        assertTrue(queryParams.containsKey("param2"));
        assertEquals("value1", queryParams.get("param1"));
        assertEquals("value2", queryParams.get("param2"));
    }

    @Test
    public void getBodyParams_ValidParams_ReturnsAllParams() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(2, bodyParams.size());
        assertTrue(bodyParams.containsKey("param1"));
        assertTrue(bodyParams.containsKey("param2"));
        assertEquals("value1", bodyParams.get("param1"));
        assertEquals("value2", bodyParams.get("param2"));
    }

    @Test
    public void createConnection_NoExistingConnection_SuccessfullyCreated() throws IOException {
        request.createConnection();
        assertNotNull(request.getConnection());
    }

    @Test
    public void doSend_ValidRequest_SuccessfulResponse() throws IOException {
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(200, response.getCode());
    }

    @Test
    public void addHeaders_ValidRequest_SuccessfullyAddedHeaders() throws ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://www.example.com").openConnection();
        request.setConnection(connection);

        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");

        request.addHeaders(connection);
        assertEquals(2, connection.getRequestProperties().size());
        assertEquals("application/json", connection.getRequestProperty("Content-Type"));
        assertEquals("Bearer token", connection.getRequestProperty("Authorization"));
    }

    @Test
    public void addBody_ValidRequest_SuccessfullyAddedBody() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://www.example.com").openConnection();
        request.setConnection(connection);

        String body = "This is a test body";
        byte[] byteBody = body.getBytes();

        request.addBody(connection, byteBody);

        InputStream inputStream = connection.getInputStream();
        StringBuilder response = new StringBuilder();
        int c;
        while ((c = inputStream.read()) != -1) {
            response.append((char) c);
        }

        assertEquals(body, response.toString());
    }

}