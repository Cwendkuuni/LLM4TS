import org.junit.*;
import static org.junit.Assert.*;

public class CodersTest {
  
  @Test
  public void testAddDecoder() {
    // Test case 1: is is null, coder is null, password is null
    try {
      Coders.addDecoder(null, null, null);
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      assertEquals("Unsupported compression method null", e.getMessage());
    }
    
    // Test case 2: is is null, coder is null, password is not null
    try {
      Coders.addDecoder(null, null, new byte[] {0, 1, 2});
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      assertEquals("Unsupported compression method null", e.getMessage());
    }
    
    // other test cases here...
  }
  
  @Test
  public void testAddEncoder() {
    // Test case 1: out is null, method is null, password is null
    try {
      Coders.addEncoder(null, null, null);
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      assertEquals("Unsupported compression method null", e.getMessage());
    }
    
    // Test case 2: out is null, method is null, password is not null
    try {
      Coders.addEncoder(null, null, new byte[] {0, 1, 2});
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      assertEquals("Unsupported compression method null", e.getMessage());
     }
    
    // other test cases here...
  }
}