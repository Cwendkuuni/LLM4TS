
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {
  
  private DeflateCompressorInputStream inputStream;
  private byte[] testBytes;
  
  @Before
  public void setup() {
    testBytes = new byte[] { 65, 66, 67, 68, 69 };
    inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(testBytes));
  }
  
  @Test
  public void testRead() throws IOException {
    assertEquals(65, inputStream.read());
    assertEquals(66, inputStream.read());
    assertEquals(67, inputStream.read());
    assertEquals(68, inputStream.read());
    assertEquals(69, inputStream.read());
    assertEquals(-1, inputStream.read());
  }
  
  @Test
  public void testReadWithBufOffsetLen() throws IOException {
    byte[] buf = new byte[3];
    int bytesRead = inputStream.read(buf, 0, buf.length);
    assertEquals(3, bytesRead);
    assertEquals(65, buf[0]);
    assertEquals(66, buf[1]);
    assertEquals(67, buf[2]);
    bytesRead = inputStream.read(buf, 0, buf.length);
    assertEquals(2, bytesRead);
    assertEquals(68, buf[0]);
    assertEquals(69, buf[1]);
    bytesRead = inputStream.read(buf, 0, buf.length);
    assertEquals(-1, bytesRead);
  }
  
  @Test
  public void testSkip() throws IOException {
    long skipped = inputStream.skip(3);
    assertEquals(3, skipped);
    assertEquals(68, inputStream.read());
    assertEquals(69, inputStream.read());
    assertEquals(-1, inputStream.read());
  }
  
  @Test
  public void testAvailable() throws IOException {
    assertEquals(5, inputStream.available());
    inputStream.read();
    inputStream.read();
    assertEquals(3, inputStream.available());
    inputStream.read(new byte[2]);
    assertEquals(1, inputStream.available());
    inputStream.read();
    assertEquals(0, inputStream.available());
  }
  
  @Test
  public void testClose() throws IOException {
    inputStream.close();
    assertEquals(-1, inputStream.read());
  }
  
  @Test
  public void testZlibHeader() {
    byte[] signature = new byte[] { (byte) 0x78, (byte) 0x9C };
    assertTrue(inputStream.zlibHeader(signature, signature.length));
  }
  
  @Test
  public void testNonZlibHeader() {
    byte[] signature = new byte[] { (byte) 0x78, (byte) 0xAA };
    assertFalse(inputStream.zlibHeader(signature, signature.length));
  }
}
