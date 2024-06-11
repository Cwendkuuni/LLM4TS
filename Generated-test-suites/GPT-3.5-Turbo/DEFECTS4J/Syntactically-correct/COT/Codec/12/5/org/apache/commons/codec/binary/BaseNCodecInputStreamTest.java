import static org.junit.Assert.*;
import org.apache.commons.codec.binary.*;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
  
  // Test read() method
  @Test
  public void testRead() throws Exception {
    byte[] input = {0x61, 0x62, 0x63}; // "abc"
    BaseNCodec codec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), codec, false);
    
    // Read the first byte
    int result = inputStream.read();
    assertEquals(97, result);
    
    // Read the second byte
    result = inputStream.read();
    assertEquals(98, result);
    
    // Read the third byte
    result = inputStream.read();
    assertEquals(99, result);
    
    // Check that EOF is returned after reading all bytes
    result = inputStream.read();
    assertEquals(-1, result);
    
    inputStream.close();
  }

  // Test read(byte[] b, int offset, int len) method
  @Test
  public void testReadWithBuffer() throws Exception {
    byte[] input = {0x61, 0x62, 0x63}; // "abc"
    byte[] output = new byte[3];
    
    BaseNCodec codec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), codec, false);
    
    // Read the bytes into the output buffer
    int result = inputStream.read(output, 0, 3);
    
    // Check that the correct number of bytes is read
    assertEquals(3, result);
    
    // Check that the correct bytes are read
    assertArrayEquals(input, output);
    
    inputStream.close();
  }

  // Test markSupported() method
  @Test
  public void testMarkSupported() {
    BaseNCodec codec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] {}), codec, false);
    
    // Check that markSupported always returns false
    assertFalse(inputStream.markSupported());
    
    inputStream.close();
  }

  // Test available() method
  @Test
  public void testAvailable() throws Exception {
    BaseNCodec codec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] {}), codec, false);
    
    // Check that available always returns 1
    assertEquals(1, inputStream.available());
    
    inputStream.close();
  }
}