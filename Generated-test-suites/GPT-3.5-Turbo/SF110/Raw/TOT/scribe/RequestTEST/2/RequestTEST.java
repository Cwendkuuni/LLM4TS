
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;

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

    private Request request;

    @Before
    public void setup() {
        String url = "https://example.com";
        Verb verb = Verb.GET;
        request = new Request(verb, url);
    }

    @Test
    public void testSend_SuccessfulRequest_ReturnsResponse() {
        // Arrange
        HttpURLConnection connection = newMockHttpURLConnection();
        request.setConnection(connection);

        // Act
        Response response = request.send();

        // Assert
        assertNotNull(response);
    }

    @Test(expected = OAuthException.class)
    public void testSend_FailedRequest_ThrowsOAuthException() {
        // Arrange
        HttpURLConnection connection = newMockHttpURLConnection();
        connection.setDoOutput(true); // Simulate failed request
        request.setConnection(connection);

        // Act
        Response response = request.send();
    }

    @Test
    public void testAddHeader_ValidParameters_HeadersAdded() {
        // Arrange
        String key = "TestKey";
        String value = "TestValue";

        // Act
        request.addHeader(key, value);

        // Assert
        Map<String, String> headers = request.getHeaders();
        assertEquals(value, headers.get(key));
    }

    @Test
    public void testAddBodyParameter_ValidParameters_BodyParametersAdded() {
        // Arrange
        String key = "TestKey";
        String value = "TestValue";

        // Act
        request.addBodyParameter(key, value);

        // Assert
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(value, bodyParams.get(key));
    }

    @Test
    public void testAddQuerystringParameter_ValidParameters_QuerystringParametersAdded() {
        // Arrange
        String key = "TestKey";
        String value = "TestValue";

        // Act
        request.addQuerystringParameter(key, value);

        // Assert
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals(value, queryStringParams.get(key));
    }

    @Test
    public void testAddPayload_StringPayload_PayloadAdded() {
        // Arrange
        String payload = "TestPayload";

        // Act
        request.addPayload(payload);

        // Assert
        String actualPayload = request.getByteBodyContents().toString();
        assertEquals(payload, actualPayload);
    }

    @Test
    public void testAddPayload_BytePayload_PayloadAdded() {
        // Arrange
        byte[] payload = "TestPayload".getBytes();

        // Act
        request.addPayload(payload);

        // Assert
        byte[] actualPayload = request.getByteBodyContents();
        assertArrayEquals(payload, actualPayload);
    }

    @Test
    public void testGetQueryStringParams_ValidUrl_ReturnsQueryStringParams() {
        // Arrange
        String url = "https://example.com?param1=value1&param2=value2";
        request = new Request(Verb.GET, url);

        // Act
        Map<String, String> queryStringParams = request.getQueryStringParams();

        // Assert
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    @Test
    public void testGetBodyParams_ValidBodyParams_ReturnsBodyParams() {
        // Arrange
        String key = "TestKey";
        String value = "TestValue";
        request.addBodyParameter(key, value);

        // Act
        Map<String, String> bodyParams = request.getBodyParams();

        // Assert
        assertEquals(value, bodyParams.get(key));
    }

    @Test
    public void testGetUrl_ValidUrl_ReturnsUrl() {
        // Arrange
        String url = "https://example.com";
        request = new Request(Verb.GET, url);

        // Act
        String actualUrl = request.getUrl();

        // Assert
        assertEquals(url, actualUrl);
    }

    @Test
    public void testGetSanitizedUrl_ValidUrl_ReturnsSanitizedUrl() {
        // Arrange
        String url = "https://example.com:8080?param=value";
        request = new Request(Verb.GET, url);

        // Act
        String actualSanitizedUrl = request.getSanitizedUrl();

        // Assert
        assertEquals("https://example.com", actualSanitizedUrl);
    }

    @Test
    public void testGetBodyContents_ValidContents_ReturnsBodyContents() {
        // Arrange
        String contents = "TestContents";
        byte[] byteContents = contents.getBytes();
        request.addPayload(byteContents);

        // Act
        String actualContents = request.getBodyContents();

        // Assert
        assertEquals(contents, actualContents);
    }

    @Test
    public void testGetByteBodyContents_ValidContents_ReturnsByteBodyContents() {
        // Arrange
        String contents = "TestContents";
        byte[] byteContents = contents.getBytes();
        request.addPayload(byteContents);

        // Act
        byte[] actualByteContents = request.getByteBodyContents();

        // Assert
        assertArrayEquals(byteContents, actualByteContents);
    }

    @Test
    public void testGetVerb_ValidVerb_ReturnsVerb() {
        // Arrange
        Verb verb = Verb.GET;
        request = new Request(verb, "https://example.com");

        // Act
        Verb actualVerb = request.getVerb();

        // Assert
        assertEquals(verb, actualVerb);
    }

    @Test
    public void testGetHeaders_ValidHeaders_ReturnsHeaders() {
        // Arrange
        String key = "TestKey";
        String value = "TestValue";
        request.addHeader(key, value);

        // Act
        Map<String, String> headers = request.getHeaders();

        // Assert
        assertEquals(value, headers.get(key));
    }

    @Test
    public void testGetCharset_CharsestNotSet_ReturnsDefaultCharset() {
        // Arrange

        // Act
        String actualCharset = request.getCharset();
        Charset defaultCharset = Charset.defaultCharset();

        // Assert
        assertEquals(defaultCharset.name(), actualCharset);
    }

    @Test
    public void testSetConnectTimeout_ValidDuration_SetsConnectTimeout() {
        // Arrange
        int duration = 500;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Act
        request.setConnectTimeout(duration, unit);

        // Assert
        assertEquals(duration, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout_ValidDuration_SetsReadTimeout() {
        // Arrange
        int duration = 500;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Act
        request.setReadTimeout(duration, unit);

        // Assert
        assertEquals(duration, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset_ValidName_SetsCharset() {
        // Arrange
        String charsetName = "UTF-8";

        // Act
        request.setCharset(charsetName);

        // Assert
        assertEquals(charsetName, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive_ValidValue_SetsConnectionKeepAlive() {
        // Arrange
        boolean connectionKeepAlive = true;

        // Act
        request.setConnectionKeepAlive(connectionKeepAlive);

        // Assert
        assertEquals(connectionKeepAlive, request.getConnectionKeepAlive());
    }

    private HttpURLConnection newMockHttpURLConnection() {
        try {
            URL url = new URL("https://example.com");
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
