import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import org.junit.Test;

public class Base64InputStreamTest {
  
  @Test
  public void testRead() throws Exception {
    byte[] input = "Hello, World!".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    byte[] output = new byte[13];
    assertEquals(13, base64InputStream.read(output, 0, 13));
    assertArrayEquals(input, output);
  }
  
  @Test
  public void testReadWithOffset() throws Exception {
    byte[] input = "Hello, World!".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    byte[] output = new byte[20];
    assertEquals(13, base64InputStream.read(output, 2, 13));
    
    byte[] expectedOutput = new byte[20];
    System.arraycopy(input, 0, expectedOutput , 2, 13);
    
    assertArrayEquals(expectedOutput, output);
  }
  
  @Test
  public void testReadWithLength() throws Exception {
    byte[] input = "Hello, World!".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    byte[] output = new byte[10];
    assertEquals(10, base64InputStream.read(output, 0, 10));
    
    byte[] expectedOutput = new byte[10];
    System.arraycopy(input, 0, expectedOutput , 0, 10);
    
    assertArrayEquals(expectedOutput, output);
  }
  
  @Test
  public void testReadWithInvalidOffset() throws Exception {
    byte[] input = "Hello, World!".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    byte[] output = new byte[15];
    
    try {
      base64InputStream.read(output, -1, 13);
      fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException e) {
      // pass
    }
  }
  
  @Test
  public void testReadWithInvalidLength() throws Exception {
    byte[] input = "Hello, World!".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(input));
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    byte[] output = new byte[15];
    
    try {
      base64InputStream.read(output, 2, -1);
      fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException e) {
      // pass
    }
  }
  
  @Test
  public void testReadNoData() throws Exception {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    byte[] output = new byte[10];
    assertEquals(-1, base64InputStream.read(output, 0, 10));
  }
  
  @Test
  public void testReadNoDataWithOffset() throws Exception {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    byte[] output = new byte[10];
    
    try {
      base64InputStream.read(output, 2, 10);
      fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException e) {
      // pass
    }
  }
  
  @Test
  public void testReadWithNullArray() throws Exception {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    try {
      base64InputStream.read(null, 0, 10);
      fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // pass
    }
  }
  
  @Test
  public void testMarkNotSupported() throws Exception {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    
    assertFalse(base64InputStream.markSupported());
  }
 
}