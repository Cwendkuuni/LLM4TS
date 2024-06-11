import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;

public class DeflateCompressorInputStreamTest {

  @Test
  public void testRead() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);
    int result = compressor.read();
    assertEquals('T', result);
    compressor.close();
  }

  @Test
  public void testReadWithBuffer() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    byte[] buffer = new byte[5];
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);
    int result = compressor.read(buffer, 0, 5);
    assertEquals(5, result);
    assertEquals('T', buffer[0]);
    compressor.close();
  }

  @Test
  public void testSkip() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);
    long result = compressor.skip(5);
    assertEquals(5, result);
    int bytesRead = 0;
    int b;
    while ((b = compressor.read()) >= 0) {
        bytesRead++;
    }
    compressor.close();
    assertEquals(data.length - 5, bytesRead);
  }

  @Test
  public void testAvailable() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);
    int available = compressor.available();
    assertEquals(data.length, available);
    compressor.close();
  }

  @Test
  public void testClose() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input);
    compressor.close();
    // Ensure that attempting to read after closing throws an IOException
    try {
        compressor.read();
        fail("Expected IOException");
    } catch (IOException e) {
        // Expected exception
    }
  }

  @Test
  public void testDeflateParametersConstructor() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    DeflateParameters parameters = new DeflateParameters();
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input, parameters);
    int result = compressor.read();
    assertEquals('T', result);
    compressor.close();
  }

  @Test
  public void testZlibHeaderConstructor() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    DeflateParameters parameters = new DeflateParameters();
    parameters.setWithZlibHeader(true);
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(input, parameters);
    int result = compressor.read();
    assertEquals('T', result);
    compressor.close();
  }

  @Test
  public void testInputStreamConstructor() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    InputStream inputStream = new DeflaterOutputStream(input);
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(inputStream);
    int result = compressor.read();
    assertEquals('T', result);
    compressor.close();
  }

  @Test
  public void testInputStreamAndDeflateParametersConstructor() throws IOException {
    byte[] data = "This is a test".getBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    InputStream inputStream = new DeflaterOutputStream(input);
    DeflateParameters parameters = new DeflateParameters();
    parameters.setWithZlibHeader(true);
    DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(inputStream, parameters);
    int result = compressor.read();
    assertEquals('T', result);
    compressor.close();
  }
}