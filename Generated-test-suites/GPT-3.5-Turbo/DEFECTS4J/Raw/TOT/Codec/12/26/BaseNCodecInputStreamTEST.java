import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.binary.*;

public class BaseNCodecInputStreamTest {
  
  private BaseNCodecInputStream bnCodecInputStream;
  private byte[] bytes;
  
  @Before
  public void setUp() {
    bytes = new byte[10];
    // Initialize the bytes array with some data
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte)i;
    }
    // Create BaseNCodecInputStream instance for testing
    bnCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(bytes), new BaseNCodec(), false);
  }
  
  @Test
  public void testRead() throws IOException {
    // Test reading individual bytes from the stream
    for (int i = 0; i < bytes.length; i++) {
      assertEquals(i, bnCodecInputStream.read());
    }
    // Test EOF reached
    assertEquals(-1, bnCodecInputStream.read());
  }
  
  @Test
  public void testReadWithOffsetAndLength() throws IOException {
    byte[] buffer = new byte[5];
    // Test reading bytes into a buffer with offset and length
    int bytesRead = bnCodecInputStream.read(buffer, 1, 3);
    // Validate the bytes read
    assertEquals(3, bytesRead);
    assertEquals(0, buffer[0]);
    assertEquals(1, buffer[1]);
    assertEquals(2, buffer[2]);
    // Test EOF reached
    bytesRead = bnCodecInputStream.read(buffer, 0, 5);
    assertEquals(-1, bytesRead);
  }
  
  @Test(expected = IOException.class)
  public void testNullByteArray() throws IOException {
    // Test passing null byte array
    bnCodecInputStream.read((byte[])null, 0, 5);
  }
  
  @Test(expected = NullPointerException.class)
  public void testNullOffset() throws IOException {
    // Test passing null offset
    bnCodecInputStream.read(new byte[5], null, 5);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testNegativeOffset() throws IOException {
    // Test passing negative offset
    bnCodecInputStream.read(new byte[5], -1, 5);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testInvalidOffsetAndLength() throws IOException {
    byte[] buffer = new byte[5];
    // Test passing invalid offset and length
    bnCodecInputStream.read(buffer, 3, 4);
  }
  
  @Test
  public void testMarkSupported() {
    // Test mark supported
    assertFalse(bnCodecInputStream.markSupported());
  }
  
  @Test
  public void testAvailable() throws IOException {
    // Test available bytes in stream
    assertEquals(bytes.length, bnCodecInputStream.available());
    // Read some bytes from stream
    bnCodecInputStream.read(new byte[3], 0, 3);
    // Validate available bytes after read
    assertEquals(bytes.length - 3, bnCodecInputStream.available());
  }
  
  @Test
  public void testSkip() throws IOException {
    // Test skipping bytes in stream
    long skippedBytes = bnCodecInputStream.skip(3);
    // Validate number of skipped bytes
    assertEquals(3, skippedBytes);
    // Read some bytes from stream
    bnCodecInputStream.read(new byte[2], 0, 2);
    // Skip zero bytes
    skippedBytes = bnCodecInputStream.skip(0);
    // Validate number of skipped bytes (0)
    assertEquals(0, skippedBytes);
    // Validate available bytes after skip
    assertEquals(bytes.length - 3 - 2, bnCodecInputStream.available());
    // Skip remaining bytes in stream
    skippedBytes = bnCodecInputStream.skip(bytes.length - 3 - 2);
    // Validate number of skipped bytes
    assertEquals(bytes.length - 3 - 2, skippedBytes);
    // Validate available bytes after skip
    assertEquals(0, bnCodecInputStream.available());
    // Read from stream after EOF
    int bytesRead = bnCodecInputStream.read();
    // Validate EOF after skipping all bytes
    assertEquals(-1, bytesRead);
  }
  
  @Test
  public void testHasData() {
    // Create BaseNCodecInputStream instance with empty data
    BaseNCodecInputStream emptyInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
    // Validate hasData() on empty input stream, should be false
    assertFalse(emptyInputStream.hasData());
    // Validate hasData() on non-empty input stream, should be true
    assertTrue(bnCodecInputStream.hasData());
  }
}