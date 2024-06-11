import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.scribe.exceptions.OAuthException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RequestTest {

    private Request request;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "http://www.example.com");
    }

    @Test
    public void testSend_SuccessfulRequest_ReturnsResponse() {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
        request.setConnection(connection);

        Response response = request.send();

        assertEquals(200, response.getCode());
        verify(connection, times(1)).setRequestMethod(Verb.GET.name());
    }

    @Test(expected = OAuthException.class)
    public void testSend_ConnectionError_ThrowsOAuthException() throws Exception {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        when(connection.getOutputStream()).thenThrow(new IOException());
        request.setConnection(connection);

        request.send();
    }

    @Test
    public void testAddHeader_ValidHeader_AddsToHeaders() {
        request.addHeader("Authorization", "Bearer token");

        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void testAddBodyParameter_ValidParameter_AddsToBodyParams() {
        request.addBodyParameter("username", "admin");

        assertEquals("admin", request.getBodyParams().get("username"));
    }

    @Test
    public void testAddQuerystringParameter_ValidParameter_AddsToQueryStringParams() {
        request.addQuerystringParameter("page", "1");

        assertEquals("1", request.getQueryStringParams().get("page"));
    }

    @Test
    public void testAddPayload_ValidPayload_SetsPayload() {
        request.addPayload("test");

        assertEquals("test", request.getBodyContents());
    }

    @Test
    public void testAddPayload_ValidPayloadByteArray_SetsPayload() {
        byte[] payload = "test".getBytes();
        request.addPayload(payload);

        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetUrl_ReturnsUrl() {
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl_ReturnsSanitizedUrl() {
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents_ReturnsBodyContents() {
        request.addPayload("test");

        assertEquals("test", request.getBodyContents());
    }

    @Test
    public void testGetVerb_ReturnsVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders_ReturnsHeaders() {
        request.addHeader("Authorization", "Bearer token");

        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void testGetCharset_DefaultCharset_ReturnsDefaultCharset() {
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testSetConnectTimeout_ValidDuration_SetsConnectTimeout() {
        request.setConnectTimeout(500, TimeUnit.MILLISECONDS);

        assertEquals(500, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout_ValidDuration_SetsReadTimeout() {
        request.setReadTimeout(500, TimeUnit.MILLISECONDS);

        assertEquals(500, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset_ValidCharset_SetsCharset() {
        request.setCharset("UTF-8");

        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive_True_SetsConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);

        assertTrue(request.getConnectionKeepAlive());
    }

    @Test
    public void testToString_ReturnsFormattedString() {
        String expected = "@Request(GET http://www.example.com)";

        assertEquals(expected, request.toString());
    }
}