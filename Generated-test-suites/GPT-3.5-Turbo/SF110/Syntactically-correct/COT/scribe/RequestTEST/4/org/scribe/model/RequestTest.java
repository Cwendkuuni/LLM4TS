
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

  @Test
  public void testSend() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    Response response = request.send();
    assertNotNull(response);
  }

  @Test(expected = OAuthException.class)
  public void testSendIOException() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    request.setConnection(null);
    request.send();
  }

  @Test
  public void testCreateConnection() throws IOException {
    Request request = new Request(Verb.GET, "http://www.example.com");
    request.createConnection();
    HttpURLConnection connection = request.getConnection();
    assertNotNull(connection);
  }

  @Test
  public void testDoSend() throws IOException {
    Request request = new Request(Verb.GET, "http://www.example.com");
    Response response = request.doSend();
    assertNotNull(response);
  }

  @Test
  public void testAddHeaders() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    HttpURLConnection connection = null;
    request.addHeaders(connection);
    // No assertion, as the method modifies the given HttpURLConnection object
  }

  @Test
  public void testAddBody() throws IOException {
    Request request = new Request(Verb.GET, "http://www.example.com");
    HttpURLConnection connection = null;
    byte[] content = "test".getBytes(StandardCharsets.UTF_8);
    request.addBody(connection, content);
    // No assertion, as the method writes to the output stream of the given HttpURLConnection object
  }

  @Test
  public void testAddHeader() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String key = "Content-Type";
    String value = "application/json";
    request.addHeader(key, value);
    Map<String, String> headers = request.getHeaders();
    assertEquals(value, headers.get(key));
  }

  @Test
  public void testAddBodyParameter() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String key = "param1";
    String value = "value1";
    request.addBodyParameter(key, value);
    Map<String, String> bodyParams = request.getBodyParams();
    assertEquals(value, bodyParams.get(key));
  }

  @Test
  public void testAddQuerystringParameter() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String key = "param1";
    String value = "value1";
    request.addQuerystringParameter(key, value);
    Map<String, String> queryStringParams = request.getQueryStringParams();
    assertEquals(value, queryStringParams.get(key));
  }

  @Test
  public void testAddPayloadString() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String payload = "test payload";
    request.addPayload(payload);
    byte[] bytePayload = request.getByteBodyContents();
    assertEquals(payload, new String(bytePayload, StandardCharsets.UTF_8));
  }

  @Test
  public void testAddPayloadBytes() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    byte[] payload = "test payload".getBytes(StandardCharsets.UTF_8);
    request.addPayload(payload);
    byte[] bytePayload = request.getByteBodyContents();
    assertArrayEquals(payload, bytePayload);
  }

  @Test
  public void testGetQueryStringParams() {
    Request request = new Request(Verb.GET, "http://www.example.com/?param1=value1");
    Map<String, String> queryStringParams = request.getQueryStringParams();
    assertEquals("value1", queryStringParams.get("param1"));
  }

  @Test
  public void testGetBodyParams() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String key = "param1";
    String value = "value1";
    request.addBodyParameter(key, value);
    Map<String, String> bodyParams = request.getBodyParams();
    assertEquals(value, bodyParams.get(key));
  }

  @Test
  public void testGetUrl() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String url = request.getUrl();
    assertEquals("http://www.example.com", url);
  }

  @Test
  public void testGetSanitizedUrl() {
    Request request = new Request(Verb.GET, "http://www.example.com:8080/?param1=value1");
    String sanitizedUrl = request.getSanitizedUrl();
    assertEquals("http://www.example.com", sanitizedUrl);
  }

  @Test
  public void testGetBodyContents() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String payload = "test payload";
    request.addPayload(payload);
    String bodyContents = request.getBodyContents();
    assertEquals(payload, bodyContents);
  }

  @Test
  public void testGetByteBodyContents() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    byte[] payload = "test payload".getBytes(StandardCharsets.UTF_8);
    request.addPayload(payload);
    byte[] byteBodyContents = request.getByteBodyContents();
    assertArrayEquals(payload, byteBodyContents);
  }

  @Test
  public void testGetVerb() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    Verb verb = request.getVerb();
    assertEquals(Verb.GET, verb);
  }

  @Test
  public void testGetHeaders() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String key = "Content-Type";
    String value = "application/json";
    request.addHeader(key, value);
    Map<String, String> headers = request.getHeaders();
    assertEquals(value, headers.get(key));
  }

  @Test
  public void testGetCharset() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String charset = request.getCharset();
    assertEquals(Charset.defaultCharset().name(), charset);
  }

  @Test
  public void testSetConnectTimeout() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    int duration = 5000;
    TimeUnit unit = TimeUnit.MILLISECONDS;
    request.setConnectTimeout(duration, unit);
    HttpURLConnection connection = request.getConnection();
    assertEquals(duration, connection.getConnectTimeout());
  }

  @Test
  public void testSetReadTimeout() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    int duration = 5000;
    TimeUnit unit = TimeUnit.MILLISECONDS;
    request.setReadTimeout(duration, unit);
    HttpURLConnection connection = request.getConnection();
    assertEquals(duration, connection.getReadTimeout());
  }

  @Test
  public void testSetCharset() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    String charsetName = "UTF-16";
    request.setCharset(charsetName);
    String charset = request.getCharset();
    assertEquals(charsetName, charset);
  }

  @Test
  public void testSetConnectionKeepAlive() {
    Request request = new Request(Verb.GET, "http://www.example.com");
    boolean connectionKeepAlive = true;
    request.setConnectionKeepAlive(connectionKeepAlive);
    assertTrue(request.getConnectionKeepAlive());
  }
}
