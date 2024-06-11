import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
  
  // Test case for Response constructor when connection is successful
  @Test
  public void responseConstructor_SuccessfulConnection() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
      @Override
      public int getResponseCode() throws IOException {
        return 200;
      }
      
      @Override
      public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream("Test Body".getBytes());
      }
      
      @Override
      public Map<String, List<String>> getHeaderFields() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("text/plain"));
        return headers;
      }
    };
    
    Response response = new Response(connection);
    
    assertEquals(200, response.getCode());
    assertEquals("Test Body", response.getBody());
    assertEquals("text/plain", response.getHeader("Content-Type"));
  }
  
  // Test case for Response constructor when connection fails due to UnknownHostException
  @Test
  public void responseConstructor_UnknownHostException() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(new URL("http://www.invalidurl.com")) {
      @Override
      public int getResponseCode() throws IOException {
        throw new UnknownHostException();
      }
    };
    
    Response response = new Response(connection);
    
    assertEquals(404, response.getCode());
    assertEquals("", response.getBody());
  }
  
  // Test case for getBody() method when body is already parsed
  @Test
  public void getBody_AlreadyParsed() throws IOException {
    Response response = new Response(null);
    response.getBody();
    
    String body = response.getBody();
    
    assertEquals("", body);
  }
  
  // Test case for getBody() method when body is not parsed yet
  @Test
  public void getBody_NotParsed() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
      @Override
      public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream("Test Body".getBytes());
      }
    };
    
    Response response = new Response(connection);
    
    String body = response.getBody();
    
    assertEquals("Test Body", body);
  }
  
  // Test case for getStream() method
  @Test
  public void getStream() throws IOException {
    InputStream inputStream = new ByteArrayInputStream("Test Body".getBytes());
    HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
      @Override
      public InputStream getInputStream() throws IOException {
        return inputStream;
      }
    };
    
    Response response = new Response(connection);
    
    InputStream stream = response.getStream();
    
    assertSame(inputStream, stream);
  }
  
  // Test case for getCode() method
  @Test
  public void getCode() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
      @Override
      public int getResponseCode() throws IOException {
        return 200;
      }
    };
    
    Response response = new Response(connection);
    
    int code = response.getCode();
    
    assertEquals(200, code);
  }
  
  // Test case for getHeaders() method
  @Test
  public void getHeaders() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
      @Override
      public Map<String, List<String>> getHeaderFields() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("text/plain"));
        headers.put("Content-Length", Arrays.asList("10"));
        return headers;
      }
    };
    
    Response response = new Response(connection);
    
    Map<String, String> headers = response.getHeaders();
    
    assertEquals("text/plain", headers.get("Content-Type"));
    assertEquals("10", headers.get("Content-Length"));
  }
  
  // Test case for getHeader() method when header is present
  @Test
  public void getHeader_Present() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
      @Override
      public Map<String, List<String>> getHeaderFields() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("text/plain"));
        headers.put("Content-Length", Arrays.asList("10"));
        return headers;
      }
    };
    
    Response response = new Response(connection);
    
    String contentType = response.getHeader("Content-Type");
    String contentLength = response.getHeader("Content-Length");
    
    assertEquals("text/plain", contentType);
    assertEquals("10", contentLength);
  }
  
  // Test case for getHeader() method when header is not present
  @Test
  public void getHeader_NotPresent() throws IOException {
    HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
      @Override
      public Map<String, List<String>> getHeaderFields() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("text/plain"));
        return headers;
      }
    };
    
    Response response = new Response(connection);
    
    String nonExistingHeader = response.getHeader("NonExistingHeader");
    
    assertNull(nonExistingHeader);
  }
}