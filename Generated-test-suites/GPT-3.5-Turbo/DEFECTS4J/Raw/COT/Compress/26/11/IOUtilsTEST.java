
import org.junit.Test;
import org.apache.commons.compress.utils.IOUtils;
import static org.junit.Assert.*;

public class IOUtilsTest {

  // Test copy(InputStream, OutputStream) method
  @Test
  public void testCopy() throws IOException {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    IOUtils.copy(in, out);
    assertEquals("test", out.toString());
  }
  
  // Test copy(InputStream, OutputStream, int) method
  @Test
  public void testCopyWithBufferSize() throws IOException {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    IOUtils.copy(in, out, 1024);
    assertEquals("test", out.toString());
  }
  
  // Test skip(InputStream, long) method
  @Test
  public void testSkip() throws IOException {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    long skipped = IOUtils.skip(in, 2);
    assertEquals(2, skipped);
    byte[] buf = new byte[2];
    int bytesRead = in.read(buf);
    assertEquals(2, bytesRead);
    assertEquals("st", new String(buf));
  }
  
  // Test readFully(InputStream, byte[]) method
  @Test
  public void testReadFully() throws IOException {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    byte[] buf = new byte[4];
    int bytesRead = IOUtils.readFully(in, buf);
    assertEquals(4, bytesRead);
    assertEquals("test", new String(buf));
  }
  
  // Test readFully(InputStream, byte[], int, int) method
  @Test
  public void testReadFullyWithOffsetAndLength() throws IOException {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    byte[] buf = new byte[3];
    int bytesRead = IOUtils.readFully(in, buf, 1, 2);
    assertEquals(2, bytesRead);
    assertEquals(0, buf[0]);
    assertEquals("te", new String(buf, 1, 2));
  }
  
  // Test toByteArray(InputStream) method
  @Test
  public void testToByteArray() throws IOException {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    byte[] byteArray = IOUtils.toByteArray(in);
    assertEquals("test", new String(byteArray));
  }
  
  // Test closeQuietly(Closeable) method
  @Test
  public void testCloseQuietly() {
    InputStream in = new ByteArrayInputStream("test".getBytes());
    IOUtils.closeQuietly(in);
    // No assertion can be made as it does not throw any exception
  }
}
