
import org.junit.Test;
import org.junit.Assert;
import java.util.Map;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

public class ResponseTest {
  
  @Test
  public void testGetBody() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(null) {
      public int getResponseCode() {
        return 200;
      }
      
      public Map<String, String> getHeaderFields() {
        return null;
      }
      
      public InputStream getInputStream() {
        return null;
      }
      
      public InputStream getErrorStream() {
        return null;
      }
    };
    
    Response response = new Response(connection);
    Assert.assertEquals("", response.getBody());
  }
  
  @Test
  public void testGetStream() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(null) {
      public InputStream getInputStream() {
        return null;
      }
      
      public InputStream getErrorStream() {
        return null;
      }
      
      public int getResponseCode() {
        return 200;
      }
      
      public Map<String, String> getHeaderFields() {
        return null;
      }
    };
    
    Response response = new Response(connection);
    Assert.assertNull(response.getStream());
  }
  
  @Test
  public void testGetCode() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(null) {
      public int getResponseCode() {
        return 200;
      }
      
      public Map<String, String> getHeaderFields() {
        return null;
      }
      
      public InputStream getInputStream() {
        return null;
      }
      
      public InputStream getErrorStream() {
        return null;
      }
    };
    
    Response response = new Response(connection);
    Assert.assertEquals(200, response.getCode());
  }
  
  @Test
  public void testGetHeaders() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(null) {
      public int getResponseCode() {
        return 200;
      }
      
      public Map<String, String> getHeaderFields() {
        Map<String, String> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        return headers;
      }
      
      public InputStream getInputStream() {
        return null;
      }
      
      public InputStream getErrorStream() {
        return null;
      }
    };
    
    Response response = new Response(connection);
    Map<String, String> expectedHeaders = new HashMap<>();
    expectedHeaders.put("key1", "value1");
    expectedHeaders.put("key2", "value2");
    
    Assert.assertEquals(expectedHeaders, response.getHeaders());
  }
  
  @Test
  public void testGetHeader() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(null) {
      public int getResponseCode() {
        return 200;
      }
      
      public Map<String, String> getHeaderFields() {
        Map<String, String> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        return headers;
      }
      
      public InputStream getInputStream() {
        return null;
      }
      
      public InputStream getErrorStream() {
        return null;
      }
    };
    
    Response response = new Response(connection);
    Assert.assertEquals("value1", response.getHeader("key1"));
    Assert.assertEquals("value2", response.getHeader("key2"));
  }
  
  @Test
  public void testUnknownHostException() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(null) {
      public int getResponseCode() {
        throw new UnknownHostException();
      }
      
      public Map<String, String> getHeaderFields() {
        return null;
      }
      
      public InputStream getInputStream() {
        return null;
      }
      
      public InputStream getErrorStream() {
        return null;
      }
    };
    
    Response response = new Response(connection);
    Assert.assertEquals("", response.getBody());
    Assert.assertEquals(404, response.getCode());
  }
}
