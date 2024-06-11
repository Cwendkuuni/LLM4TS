package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

  private Base64InputStream base64InputStream;

  @Before
  public void setUp() {
    byte[] input = "Hello, World!".getBytes();
    InputStream inputStream = new ByteArrayInputStream(input);
    base64InputStream = new Base64InputStream(inputStream);
  }

  @Test
  public void testRead() throws IOException {
    int expected = 'H';
    int actual = base64InputStream.read();
    assertEquals(expected, actual);
  }

  @Test
  public void testReadWithOffsetAndLength() throws IOException {
    byte[] bytes = new byte[5];
    int expectedLen = 5;
    int actualLen = base64InputStream.read(bytes, 0, 5);
    assertEquals(expectedLen, actualLen);

    String expectedString = "Hello";
    String actualString = new String(bytes);
    assertEquals(expectedString, actualString);
  }

  @Test(expected = NullPointerException.class)
  public void testReadWithNullArray() throws IOException {
    base64InputStream.read(null, 0, 5);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidOffset() throws IOException {
    byte[] bytes = new byte[5];
    base64InputStream.read(bytes, -1, 5);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidLength() throws IOException {
    byte[] bytes = new byte[5];
    base64InputStream.read(bytes, 0, 10);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidBufferSize() throws IOException {
    byte[] bytes = new byte[5];
    base64InputStream.read(bytes, 0, 5);
    base64InputStream.read(bytes, 0, 1);
  }

  @Test
  public void testMarkSupported() {
    boolean expected = false;
    boolean actual = base64InputStream.markSupported();
    assertEquals(expected, actual);
  }
}