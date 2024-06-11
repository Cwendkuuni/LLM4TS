package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

  @Test
  public void testRead() {
    Base64InputStream inputStream = new Base64InputStream(null, false);
    try {
      int result = inputStream.read();
      assertEquals(-1, result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testReadWithParams() {
    Base64InputStream inputStream = new Base64InputStream(null, false);
    byte[] buffer = new byte[10];
    try {
      int result = inputStream.read(buffer, 0, 10);
      assertEquals(0, result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testReadWithInvalidParams() {
    Base64InputStream inputStream = new Base64InputStream(null, false);
    byte[] buffer = new byte[10];
    try {
      inputStream.read(buffer, -1, 10);
      fail("Expected IndexOutOfBoundsException not thrown");
    } catch (IndexOutOfBoundsException e) {
      // Do nothing, this is expected
    } catch (Exception e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testReadWithZeroLength() {
    Base64InputStream inputStream = new Base64InputStream(null, false);
    byte[] buffer = new byte[10];
    try {
      int result = inputStream.read(buffer, 0, 0);
      assertEquals(0, result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testMarkSupported() {
    Base64InputStream inputStream = new Base64InputStream(null, false);
    boolean result = inputStream.markSupported();
    assertFalse(result);
  }
}