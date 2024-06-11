import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

  @Test
  public void testRead() {
    try {
      byte[] input = {104, 101, 108, 108, 111};
      Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(input));
      int result = stream.read();
      
      assertEquals("Failed to read byte from stream", 104, result);
      stream.close();
    } catch (IOException e) {
      fail("Caught IOException: " + e);
    }
  }
  
  @Test
  public void testReadWithBuffer() {
    try {
      byte[] input = {104, 101, 108, 108, 111};
      byte[] buffer = new byte[5];
      Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(input));
      int result = stream.read(buffer, 0, 5);
      
      assertEquals("Failed to read bytes from stream", 5, result);
      assertArrayEquals("Unexpected bytes read from stream", input, buffer);
      stream.close();
    } catch (IOException e) {
      fail("Caught IOException: " + e);
    }
  }
  
  @Test
  public void testReadEmptyInput() {
    try {
      byte[] input = {};
      byte[] buffer = new byte[5];
      Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(input));
      int result = stream.read(buffer, 0, 5);
      
      assertEquals("Failed to read bytes from stream", -1, result);
      stream.close();
    } catch (IOException e) {
      fail("Caught IOException: " + e);
    }
  }
  
  @Test
  public void testReadPartialInput() {
    try {
      byte[] input = {104, 101, 108};
      byte[] buffer = new byte[5];
      Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(input));
      int result = stream.read(buffer, 0, 5);
      
      assertEquals("Failed to read bytes from stream", 3, result);
      byte[] expected = {104, 101, 108, 0, 0};
      assertArrayEquals("Unexpected bytes read from stream", expected, buffer);
      stream.close();
    } catch (IOException e) {
      fail("Caught IOException: " + e);
    }
  }
  
  @Test
  public void testMarkSupported() {
    Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
    boolean result = stream.markSupported();
    
    assertFalse("Mark supported", result);
  }
}