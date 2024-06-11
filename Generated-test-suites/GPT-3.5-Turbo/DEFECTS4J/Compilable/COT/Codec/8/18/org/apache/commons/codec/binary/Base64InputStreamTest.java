package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;

public class Base64InputStreamTest {

  @Test
  public void testRead() throws Exception {
    byte[] input = new byte[]{104, 101, 108, 108, 111};
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
    Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
    
    int result = base64InputStream.read();
    
    assertEquals(104, result);
  }
  
  @Test
  public void testReadByteArray() throws Exception {
    byte[] input = new byte[]{104, 101, 108, 108, 111};
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
    Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
    byte[] output = new byte[5];
    
    int result = base64InputStream.read(output);
    
    assertEquals(5, result);
    assertArrayEquals(new byte[]{104, 101, 108, 108, 111}, output);
  }
  
  @Test
  public void testReadByteArrayOffset() throws Exception {
    byte[] input = new byte[]{104, 101, 108, 108, 111};
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
    Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
    byte[] output = new byte[5];
    
    int result = base64InputStream.read(output, 1, 3);
    
    assertEquals(3, result);
    assertArrayEquals(new byte[]{0, 104, 101, 108, 0}, output);
  }
  
  @Test(expected = NullPointerException.class)
  public void testReadNullByteArray() throws Exception {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[5]);
    Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
    
    base64InputStream.read(null);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadInvalidOffset() throws Exception {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[5]);
    Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
    byte[] output = new byte[5];
    
    base64InputStream.read(output, -1, 3);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadInvalidLength() throws Exception {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[5]);
    Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
    byte[] output = new byte[5];
    
    base64InputStream.read(output, 1, -3);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadInvalidBufferLength() throws Exception {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[5]);
    Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
    byte[] output = new byte[5];
    
    base64InputStream.read(output, 1, 10);
  }
}
