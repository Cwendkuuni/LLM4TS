import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.net.*;
import java.util.*;

public class ResponseTest {
  
  @Test
  public void testGetBody() {
    Response response = new Response(connection);
    assertNotNull(response.getBody());
  }
  
  @Test
  public void testGetStream() {
    Response response = new Response(connection);
    assertNotNull(response.getStream());
  }
  
  @Test
  public void testGetCode() {
    Response response = new Response(connection);
    assertEquals(200, response.getCode());
  }
  
  @Test
  public void testGetHeaders() {
    Response response = new Response(connection);
    assertNotNull(response.getHeaders());
  }
  
  @Test
  public void testGetHeader() {
    Response response = new Response(connection);
    assertNotNull(response.getHeader("Content-Type"));
  }
  
  @Test
  public void testWasSuccessful() {
    Response response = new Response(connection);
    assertTrue(response.wasSuccessful());
  }
  
  @Test
  public void testInvalidConnection() {
    HttpURLConnection connection = null;
    try {
      Response response = new Response(connection);
    } catch (IOException e) {
      fail("Exception should not be thrown for valid connection");
    }
  }
  
  @Test
  public void testUnknownHostException() {
    HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
      public InputStream getErrorStream() {
        return null;
      }
    };
    try {
      Response response = new Response(connection);
      assertEquals(404, response.getCode());
      assertEquals("", response.getBody());
    } catch (IOException e) {
      fail("Exception should not be thrown for UnknownHostException");
    }
  }
}