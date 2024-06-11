import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class ResponseTest {
  
  @Test
  public void testConstructor() {
    try {
      HttpURLConnection connection = null; // create a valid HttpURLConnection object
      Response response = new Response(connection);
      
      // Check that code, headers and stream are properly initialized
      assertNotNull(response.getCode());
      assertNotNull(response.getHeaders());
      assertNotNull(response.getStream());
    } catch(IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testParseBodyContents() {
    Response response = new Response(null);
    
    // Set a body value and check if it is properly returned by getBody()
    response.body = "Test body";
    assertEquals("Test body", response.getBody());
  }
  
  @Test
  public void testParseHeaders() {
    Response response = new Response(null);
    
    // Create a valid HttpURLConnection object with headers
    HttpURLConnection connection = null; 
    connection.setRequestProperty("Header1", "Value1");
    connection.setRequestProperty("Header2", "Value2");
    
    // Check that headers are properly parsed and returned as a hashmap
    assertEquals(new HashMap<>().put("Header1", "Value1"), response.parseHeaders(connection).get("Header1"));
    assertEquals(new HashMap<>().put("Header2", "Value2"), response.parseHeaders(connection).get("Header2"));
  }
  
  @Test
  public void testWasSuccessful() {
    Response response = new Response(null);
    
    // Set code value and check if return value of wasSuccessful() is correct
    response.code = 200;
    assertTrue(response.wasSuccessful());
    
    response.code = 404;
    assertFalse(response.wasSuccessful());
  }
  
  @Test
  public void testGetBody() {
    Response response = new Response(null);
    
    // Test when body is null and when it is not null
    assertNull(response.getBody());
    
    response.body = "Test body";
    assertEquals("Test body", response.getBody());
  }
  
  @Test
  public void testGetStream() {
    Response response = new Response(null);
    
    // Check if stream is properly returned
    assertNotNull(response.getStream());
  }
  
  @Test
  public void testGetCode() {
    Response response = new Response(null);
    
    // Check if code is properly returned
    assertEquals(0, response.getCode());
    
    response.code = 200;
    assertEquals(200, response.getCode());
  }
  
  @Test
  public void testGetHeaders() {
    Response response = new Response(null);
    
    // Check if headers are properly returned
    assertNotNull(response.getHeaders());
  }
  
  @Test
  public void testGetHeader() {
    Response response = new Response(null);
    response.headers.put("Header1", "Value1");
    
    // Check if header is properly returned
    assertEquals("Value1", response.getHeader("Header1"));
  }
  
}