
import org.junit.Test;
import org.scribe.model.*;

import static org.junit.Assert.*;

public class RequestTest {

    private static final String URL = "http://example.com";
    private static final String KEY1 = "key1";
    private static final String VALUE1 = "value1";
    private static final String KEY2 = "key2";
    private static final String VALUE2 = "value2";

    @Test
    public void testSend_SuccessfulRequest_ResponseNotNull() {
        Request request = new Request(Verb.GET, URL);
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection_NoQuerystringParams_UrlNotModified() {
        Request request = new Request(Verb.GET, URL);
        request.createConnection();
        assertEquals(URL, request.getUrl());
    }

    @Test
    public void testCreateConnection_QuerystringParams_UrlModifiedWithParams() {
        Request request = new Request(Verb.GET, URL);
        request.addQuerystringParameter(KEY1, VALUE1);
        request.addQuerystringParameter(KEY2, VALUE2);
        request.createConnection();
        String expectedUrl = URL + "?" + KEY1 + "=" + VALUE1 + "&" + KEY2 + "=" + VALUE2;
        assertEquals(expectedUrl, request.getUrl());
    }

    @Test
    public void testDoSend_PUTMethod_AddsBody() {
        Request request = new Request(Verb.PUT, URL);
        request.addBodyParameter(KEY1, VALUE1);
        Response response = request.doSend();
        String requestBody = response.getRequest().getBodyContents();
        assertTrue(requestBody.contains(KEY1) && requestBody.contains(VALUE1));
    }

    @Test
    public void testDoSend_POSTMethod_AddsBody() {
        Request request = new Request(Verb.POST, URL);
        request.addBodyParameter(KEY1, VALUE1);
        Response response = request.doSend();
        String requestBody = response.getRequest().getBodyContents();
        assertTrue(requestBody.contains(KEY1) && requestBody.contains(VALUE1));
    }

    @Test
    public void testAddHeader_ValidHeader_SuccessfullyAdded() {
        Request request = new Request(Verb.GET, URL);
        request.addHeader(KEY1, VALUE1);
        assertTrue(request.getHeaders().containsKey(KEY1));
        assertEquals(VALUE1, request.getHeaders().get(KEY1));
    }

    @Test
    public void testAddBodyParameter_ValidParameter_SuccessfullyAdded() {
        Request request = new Request(Verb.GET, URL);
        request.addBodyParameter(KEY1, VALUE1);
        assertTrue(request.getBodyParams().containsKey(KEY1));
        assertEquals(VALUE1, request.getBodyParams().get(KEY1));
    }

    @Test
    public void testAddQuerystringParameter_ValidParameter_SuccessfullyAdded() {
        Request request = new Request(Verb.GET, URL);
        request.addQuerystringParameter(KEY1, VALUE1);
        assertTrue(request.getQueryStringParams().containsKey(KEY1));
        assertEquals(VALUE1, request.getQueryStringParams().get(KEY1));
    }

    @Test
    public void testAddPayload_StringPayload_SuccessfullyAdded() {
        Request request = new Request(Verb.GET, URL);
        String payload = "example";
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testAddPayload_BytePayload_SuccessfullyAdded() {
        Request request = new Request(Verb.GET, URL);
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetUrl_InitializedWithUrl_ReturnsCorrectUrl() {
        Request request = new Request(Verb.GET, URL);
        assertEquals(URL, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl_InitializedWithUrl_ReturnsCorrectSanitizedUrl() {
        Request request = new Request(Verb.GET, URL);
        assertEquals(URL, request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents_InitializedWithPayload_ReturnsCorrectBodyContents() {
        Request request = new Request(Verb.GET, URL);
        String payload = "example";
        request.addPayload(payload);
        assertEquals(payload, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents_InitializedWithPayload_ReturnsCorrectByteBodyContents() {
        Request request = new Request(Verb.GET, URL);
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb_InitializedWithVerb_ReturnsCorrectVerb() {
        Request request = new Request(Verb.GET, URL);
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders_InitializedWithHeaders_ReturnsCorrectHeaders() {
        Request request = new Request(Verb.GET, URL);
        request.addHeader(KEY1, VALUE1);
        assertEquals(1, request.getHeaders().size());
        assertTrue(request.getHeaders().containsKey(KEY1));
        assertEquals(VALUE1, request.getHeaders().get(KEY1));
    }

    @Test
    public void testGetCharset_InitializedWithCharset_ReturnsCorrectCharset() {
        String charset = "UTF-8";
        Request request = new Request(Verb.GET, URL);
        request.setCharset(charset);
        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive_True_KeepAlivePropertySet() {
        Request request = new Request(Verb.GET, URL);
        request.setConnectionKeepAlive(true);
        assertEquals("true", System.getProperty("http.keepAlive"));
    }

    @Test
    public void testSetConnectionKeepAlive_False_KeepAlivePropertySet() {
        Request request = new Request(Verb.GET, URL);
        request.setConnectionKeepAlive(false);
        assertEquals("false", System.getProperty("http.keepAlive"));
    }
}

