
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void send_ValidRequest_ReturnsResponse() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");

        // Act
        Response response = request.send();

        // Assert
        assertNotNull(response);
    }

    @Test
    public void send_IOExceptionThrown_ThrowsOAuthException() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        
        // Act & Assert
        assertThrows(OAuthException.class, request::send);
    }

    @Test
    public void createConnection_ConnectionNotAlreadyCreated_CreatesConnection() throws Exception {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");

        // Act
        request.createConnection();

        // Assert
        assertNotNull(request.getConnection());
    }

    @Test
    public void createConnection_ConnectionAlreadyCreated_SkipsConnectionCreation() throws Exception {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.createConnection();
        HttpURLConnection initialConnection = request.getConnection();

        // Act
        request.createConnection();
        HttpURLConnection newConnection = request.getConnection();

        // Assert
        assertSame(initialConnection, newConnection);
    }

    @Test
    public void doSend_ValidRequest_SendsRequestAndReturnsResponse() throws Exception {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");

        // Act
        Response response = request.doSend();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
    }

    @Test
    public void addHeaders_ValidHeaders_AddsHeadersToConnection() throws Exception {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        request.setConnection(connection);
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");

        // Act
        request.addHeaders(connection);

        // Assert
        verify(connection, times(1)).setRequestProperty("Header1", "Value1");
        verify(connection, times(1)).setRequestProperty("Header2", "Value2");
    }

    @Test
    public void addBody_ValidBody_AddsBodyToConnection() throws Exception {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);
        request.setConnection(connection);
        byte[] content = "Test Body".getBytes();

        // Act
        request.addBody(connection, content);

        // Assert
        verify(connection, times(1)).setRequestProperty("Content-Length", String.valueOf(content.length));
        verify(connection, times(1)).setDoOutput(true);
        verify(connection.getOutputStream(), times(1)).write(content);
    }

    @Test
    public void addHeader_ValidHeader_AddsHeaderToHeadersMap() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        String key = "Header1";
        String value = "Value1";

        // Act
        request.addHeader(key, value);
        Map<String, String> headers = request.getHeaders();

        // Assert
        assertEquals(value, headers.get(key));
    }

    @Test
    public void addBodyParameter_ValidParameter_AddsParameterToBodyParamsMap() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        String key = "Param1";
        String value = "Value1";

        // Act
        request.addBodyParameter(key, value);
        Map<String, String> bodyParams = request.getBodyParams();

        // Assert
        assertEquals(value, bodyParams.get(key));
    }

    @Test
    public void addQuerystringParameter_ValidParameter_AddsParameterToQueryStringParamsMap() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        String key = "Param1";
        String value = "Value1";

        // Act
        request.addQuerystringParameter(key, value);
        Map<String, String> queryStringParams = request.getQueryStringParams();

        // Assert
        assertEquals(value, queryStringParams.get(key));
    }

    @Test
    public void addPayload_StringPayload_AddsPayloadAsString() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        String payload = "Test Payload";

        // Act
        request.addPayload(payload);

        // Assert
        assertEquals(payload, request.getPayload());
    }

    @Test
    public void addPayload_BytePayload_AddsPayloadAsBytes() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        byte[] payload = "Test Payload".getBytes();

        // Act
        request.addPayload(payload);

        // Assert
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void getQueryStringParams_ValidUrl_ReturnsQueryStringParams() {
        // Arrange
        String urlWithParams = "https://www.example.com?param1=value1&param2=value2";
        Request request = new Request(Verb.GET, urlWithParams);

        // Act
        Map<String, String> queryStringParams = request.getQueryStringParams();

        // Assert
        assertTrue(queryStringParams.containsKey("param1"));
        assertTrue(queryStringParams.containsKey("param2"));
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    @Test
    public void getBodyContents_ValidRequest_ReturnsBodyContentsAsString() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        String payload = "Test Payload";
        request.addPayload(payload);

        // Act
        String bodyContents = request.getBodyContents();

        // Assert
        assertEquals(payload, bodyContents);
    }

    @Test
    public void getByteBodyContents_ValidRequest_ReturnsBodyContentsAsBytes() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        byte[] payload = "Test Payload".getBytes();
        request.addPayload(payload);

        // Act
        byte[] byteBodyContents = request.getByteBodyContents();

        // Assert
        assertArrayEquals(payload, byteBodyContents);
    }

    @Test
    public void getVerb_ValidRequest_ReturnsVerb() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");

        // Act
        Verb verb = request.getVerb();

        // Assert
        assertEquals(Verb.GET, verb);
    }

    @Test
    public void getHeaders_ValidRequest_ReturnsHeaders() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        String key = "Header1";
        String value = "Value1";
        request.addHeader(key, value);

        // Act
        Map<String, String> headers = request.getHeaders();

        // Assert
        assertTrue(headers.containsKey(key));
        assertEquals(value, headers.get(key));
    }

    @Test
    public void getCharset_DefaultCharsetNotSet_ReturnsDefaultCharset() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");

        // Act
        String charset = request.getCharset();

        // Assert
        assertEquals(Charset.defaultCharset().name(), charset);
    }

    @Test
    public void getCharset_CustomCharsetSet_ReturnsCustomCharset() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        String customCharset = "UTF-8";
        request.setCharset(customCharset);

        // Act
        String charset = request.getCharset();

        // Assert
        assertEquals(customCharset, charset);
    }

    @Test
    public void setConnectTimeout_ValidDuration_SetsConnectTimeout() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Act
        request.setConnectTimeout(duration, unit);
        int actualTimeout = request.getConnection().getConnectTimeout();

        // Assert
        assertEquals(duration, actualTimeout);
    }

    @Test
    public void setReadTimeout_ValidDuration_SetsReadTimeout() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Act
        request.setReadTimeout(duration, unit);
        int actualTimeout = request.getConnection().getReadTimeout();

        // Assert
        assertEquals(duration, actualTimeout);
    }

    @Test
    public void setCharset_ValidCharset_SetsCharset() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        String customCharset = "UTF-8";

        // Act
        request.setCharset(customCharset);
        String charset = request.getCharset();

        // Assert
        assertEquals(customCharset, charset);
    }

    @Test
    public void setConnectionKeepAlive_True_SetsConnectionKeepAlive() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        boolean connectionKeepAlive = true;

        // Act
        request.setConnectionKeepAlive(connectionKeepAlive);
        boolean keepAlive = request.getConnectionKeepAlive();

        // Assert
        assertTrue(keepAlive);
    }

    @Test
    public void setConnectionKeepAlive_False_SetsConnectionNotKeepAlive() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        boolean connectionKeepAlive = false;

        // Act
        request.setConnectionKeepAlive(connectionKeepAlive);
        boolean keepAlive = request.getConnectionKeepAlive();

        // Assert
        assertFalse(keepAlive);
    }
}

