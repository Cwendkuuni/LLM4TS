package org.apache.commons.compress.compressors.deflate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

  @Test
  public void testRead() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

    int result = compressorInputStream.read();

    assertEquals(1, result);
    compressorInputStream.close();
  }

  @Test
  public void testReadWithBuffer() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
    byte[] buffer = new byte[3];

    int result = compressorInputStream.read(buffer, 0, buffer.length);

    assertEquals(3, result);
    assertArrayEquals(new byte[]{1, 2, 3}, buffer);
    compressorInputStream.close();
  }

  @Test
  public void testSkip() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

    long result = compressorInputStream.skip(2);

    assertEquals(2, result);
    compressorInputStream.close();
  }

  @Test
  public void testAvailable() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

    int result = compressorInputStream.available();

    assertEquals(5, result);
    compressorInputStream.close();
  }

  @Test
  public void testClose() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

    compressorInputStream.close();

    // No exception should be thrown
  }

}