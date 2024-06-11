
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Base64InputStreamTest {
  
  @Test
  public void testRead_returnsMinusOneWhenInputIsEmpty() {
    byte[] input = new byte[0];
    InputStream inputStream = new ByteArrayInputStream(input);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    int result = base64InputStream.read();
    
    assertEquals(-1, result);
  }
  
  @Test
  public void testRead_returnsEncodedDataCorrectly() {
    byte[] input = "SGVsbG8gd29ybGQ=".getBytes();
    InputStream inputStream = new ByteArrayInputStream(input);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
    
    int result1 = base64InputStream.read();
    int result2 = base64InputStream.read();
    
    assertEquals(72, result1);
    assertEquals(101, result2);
  }
}
