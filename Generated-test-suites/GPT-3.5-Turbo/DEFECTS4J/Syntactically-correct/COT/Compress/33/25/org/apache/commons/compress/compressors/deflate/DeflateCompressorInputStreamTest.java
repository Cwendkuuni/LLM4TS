import static org.junit.Assert.*;
import org.junit.*;

public class DeflateCompressorInputStreamTest {

  private InputStream createTestInputStream() {
    // TODO: create a test input stream with deflate-compressed data
  }

  @Test
  public void testRead() throws IOException {
    DeflateCompressorInputStream input = new DeflateCompressorInputStream(createTestInputStream());
    int byteRead = input.read();
    assertEquals(expectedByte, byteRead);
  }

  @Test
  public void testReadBytes() throws IOException {
    DeflateCompressorInputStream input = new DeflateCompressorInputStream(createTestInputStream());
    byte[] buffer = new byte[1024];
    int bytesRead = input.read(buffer, 0, 1024);
    assertEquals(expectedBytesRead, bytesRead);
  }

  @Test
  public void testSkip() throws IOException {
    DeflateCompressorInputStream input = new DeflateCompressorInputStream(createTestInputStream());
    long bytesSkipped = input.skip(10);
    assertEquals(expectedBytesSkipped, bytesSkipped);
  }

  @Test
  public void testAvailable() throws IOException {
    DeflateCompressorInputStream input = new DeflateCompressorInputStream(createTestInputStream());
    int availableBytes = input.available();
    assertEquals(expectedAvailableBytes, availableBytes);
  }

  @Test
  public void testClose() throws IOException {
    DeflateCompressorInputStream input = new DeflateCompressorInputStream(createTestInputStream());
    input.close();
    // TODO: assert that the input stream is closed properly
  }

  @Test
  public void testSignatureMatch() {
    DeflateCompressorInputStream input = new DeflateCompressorInputStream(createTestInputStream());
    boolean signatureMatch = input.checkSignature(signatureBytes, length);
    assertEquals(expectedSignatureMatch, signatureMatch);
  }

}