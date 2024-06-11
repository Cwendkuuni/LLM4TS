import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
  
  @Test
  public void testGetBodyWithBodySetDirectly() {
    Response response = new Response(createDummyHttpURLConnection());
    response.body = "Test Body";
    assertEquals("Test Body", response.getBody());
  }
  
  @Test
  public void testGetBodyWithStreamSet() {
    Response response = new Response(createDummyHttpURLConnection());
    response.stream = new ByteArrayInputStream("Test Stream".getBytes());
    assertEquals("Test Stream", response.getBody());
  }
  
  @Test
  public void testGetBodyWithStreamError() {
    Response response = new Response(createDummyErrorHttpURLConnection());
    assertEquals("", response.getBody());
  }
  
  @Test
  public void testGetStream() {
    Response response = new Response(createDummyHttpURLConnection());
    assertNotNull(response.getStream());
  }
  
  @Test
  public void testGetCode() {
    Response response = new Response(createDummyHttpURLConnection());
    assertEquals(200, response.getCode());
  }
  
  @Test
  public void testGetHeaders() {
    Response response = new Response(createDummyHttpURLConnection());
    Map<String, String> expectedHeaders = new HashMap<>();
    expectedHeaders.put("Content-Type", "application/json");
    expectedHeaders.put("Authorization", "Bearer token");
    assertEquals(expectedHeaders, response.getHeaders());
  }
  
  @Test
  public void testGetHeader() {
    Response response = new Response(createDummyHttpURLConnection());
    assertEquals("application/json", response.getHeader("Content-Type"));
  }
  
  private HttpURLConnection createDummyHttpURLConnection() {
    return new DummyHttpURLConnection();
  }
  
  private HttpURLConnection createDummyErrorHttpURLConnection() {
    return new DummyErrorHttpURLConnection();
  }

  private class DummyHttpURLConnection extends HttpURLConnection {
    DummyHttpURLConnection() {
      super(null);
    }
    
    @Override
    public void connect() throws IOException {
      
    }
    
    @Override
    public int getResponseCode() throws IOException {
      return 200;
    }
    
    @Override
    public Map<String, String> getHeaderFields() {
      Map<String, String> headers = new HashMap<>();
      headers.put("Content-Type", "application/json");
      headers.put("Authorization", "Bearer token");
      return headers;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream("Test Stream".getBytes());
    }
  }
  
  private class DummyErrorHttpURLConnection extends HttpURLConnection {
    DummyErrorHttpURLConnection() {
      super(null);
    }
    
    @Override
    public void connect() throws IOException {
      
    }
    
    @Override
    public int getResponseCode() throws IOException {
      return 500;
    }
    
    @Override
    public InputStream getErrorStream() {
      return new ByteArrayInputStream("Test Error Stream".getBytes());
    }
  }
}