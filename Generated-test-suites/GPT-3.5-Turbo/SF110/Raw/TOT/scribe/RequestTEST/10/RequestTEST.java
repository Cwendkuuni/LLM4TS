
import org.junit.*;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        Response response = request.send();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        // Additional assertions for the response content can be added
    }

    @Test(expected = OAuthException.class)
    public void testSend_IOException_Failure() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        // To simulate IOException, an invalid url is set

        // Act
        Response response = request.send();

        // Assert
        // Exception is expected to be thrown
    }

    @Test
    public void testCreateConnection_NormalCase_Success() throws IOException {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        request.createConnection();

        // Assert
        assertNotNull(request.getConnection());
        assertEquals("http://www.example.com", request.getConnection().getURL().toString());
    }

    @Test
    public void testDoSend_PUTRequest_Success() throws IOException {
        // Arrange
        Request request = new Request(Verb.PUT, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        byte[] payload = "{\"key\": \"value\"}".getBytes();
        request.addPayload(payload);

        // Act
        Response response = request.doSend();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        // Additional assertions for the response content can be added
    }

    @Test
    public void testDoSend_POSTRequest_Success() throws IOException {
        // Arrange
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        request.addBodyParameter("key", "value");

        // Act
        Response response = request.doSend();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        // Additional assertions for the response content can be added
    }

    @Test
    public void testAddHeaders_AddMultipleHeaders_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Header1", "Value1");
        request.addHeader("Header2", "Value2");

        // Act
        request.addHeaders(request.getConnection());

        // Assert
        assertNotNull(request.getConnection().getRequestProperty("Header1"));
        assertEquals("Value1", request.getConnection().getRequestProperty("Header1"));
        assertNotNull(request.getConnection().getRequestProperty("Header2"));
        assertEquals("Value2", request.getConnection().getRequestProperty("Header2"));
    }

    @Test
    public void testAddBody_AddBodyContent_Success() throws IOException {
        // Arrange
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] content = "Request body content".getBytes();

        // Act
        request.addBody(request.getConnection(), content);

        // Assert
        assertNotNull(request.getConnection().getRequestProperty("Content-Length"));
        assertEquals("19", request.getConnection().getRequestProperty("Content-Length"));
        assertNotNull(request.getConnection().getOutputStream());
        // Additional assertions for the output stream can be added
    }

    @Test
    public void testAddHeader_AddSingleHeader_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        request.addHeader("Header1", "Value1");

        // Assert
        assertNotNull(request.getHeaders().get("Header1"));
        assertEquals("Value1", request.getHeaders().get("Header1"));
    }

    @Test
    public void testAddBodyParameter_AddSingleBodyParameter_Success() {
        // Arrange
        Request request = new Request(Verb.POST, "http://www.example.com");

        // Act
        request.addBodyParameter("key", "value");

        // Assert
        assertNotNull(request.getBodyParams().get("key"));
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter_AddSingleQuerystringParameter_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        request.addQuerystringParameter("key", "value");

        // Assert
        assertNotNull(request.getQueryStringParams().get("key"));
        assertEquals("value", request.getQueryStringParams().get("key"));
    }

    @Test
    public void testAddPayload_AddStringPayload_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        request.addPayload("Request payload");

        // Assert
        assertNotNull(request.getPayload());
        assertEquals("Request payload", request.getPayload());
    }

    @Test
    public void testAddPayload_AddBytePayload_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");
        byte[] payload = "Request payload".getBytes();

        // Act
        request.addPayload(payload);

        // Assert
        assertNotNull(request.getBytePayload());
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams_NormalCase_Success() throws MalformedURLException {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com?key=value");

        // Act
        Map<String, String> queryStringParams = request.getQueryStringParams();

        // Assert
        assertNotNull(queryStringParams);
        assertEquals(1, queryStringParams.size());
        assertEquals("value", queryStringParams.get("key"));
    }

    @Test
    public void testGetBodyParams_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("key", "value");

        // Act
        Map<String, String> bodyParams = request.getBodyParams();

        // Assert
        assertNotNull(bodyParams);
        assertEquals(1, bodyParams.size());
        assertEquals("value", bodyParams.get("key"));
    }

    @Test
    public void testGetUrl_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        String url = request.getUrl();

        // Assert
        assertNotNull(url);
        assertEquals("http://www.example.com", url);
    }

    @Test
    public void testGetSanitizedUrl_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com?id=1");

        // Act
        String sanitizedUrl = request.getSanitizedUrl();

        // Assert
        assertNotNull(sanitizedUrl);
        assertEquals("http://www.example.com", sanitizedUrl);
    }

    @Test
    public void testGetBodyContents_NormalCase_Success() throws UnsupportedEncodingException {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");
        byte[] payload = "Request payload".getBytes();
        request.addPayload(payload);

        // Act
        String bodyContents = request.getBodyContents();

        // Assert
        assertNotNull(bodyContents);
        assertEquals("Request payload", bodyContents);
    }

    @Test
    public void testGetByteBodyContents_NormalCase_Success() throws UnsupportedEncodingException {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");
        byte[] payload = "Request payload".getBytes();
        request.addPayload(payload);

        // Act
        byte[] byteBodyContents = request.getByteBodyContents();

        // Assert
        assertNotNull(byteBodyContents);
        assertArrayEquals(payload, byteBodyContents);
    }

    @Test
    public void testGetVerb_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        Verb verb = request.getVerb();

        // Assert
        assertNotNull(verb);
        assertEquals(Verb.GET, verb);
    }

    @Test
    public void testGetHeaders_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Header1", "Value1");

        // Act
        Map<String, String> headers = request.getHeaders();

        // Assert
        assertNotNull(headers);
        assertEquals(1, headers.size());
        assertEquals("Value1", headers.get("Header1"));
    }

    @Test
    public void testGetCharset_DefaultCharset_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        String charset = request.getCharset();

        // Assert
        assertNotNull(charset);
        assertEquals(Charset.defaultCharset().name(), charset);
    }

    @Test
    public void testSetConnectTimeout_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        request.setConnectTimeout(500, TimeUnit.MILLISECONDS);

        // Assert
        assertEquals(500, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        request.setReadTimeout(500, TimeUnit.MILLISECONDS);

        // Assert
        assertEquals(500, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        request.setCharset("UTF-8");

        // Assert
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        request.setConnectionKeepAlive(true);

        // Assert
        assertTrue(request.getConnectionKeepAlive());
    }

    @Test
    public void testSetConnection_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);

        // Act
        request.setConnection(connection);

        // Assert
        assertEquals(connection, request.getConnection());
    }

    @Test
    public void testToString_NormalCase_Success() {
        // Arrange
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Act
        String str = request.toString();

        // Assert
        assertNotNull(str);
    }
}
