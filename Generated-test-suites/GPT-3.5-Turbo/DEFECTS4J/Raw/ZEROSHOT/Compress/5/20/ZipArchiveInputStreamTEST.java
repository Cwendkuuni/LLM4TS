import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

  ZipArchiveInputStream zipStream;

  @Before
  public void setup() {
    InputStream input = new ByteArrayInputStream(new byte[0]);
    zipStream = new ZipArchiveInputStream(input);
  }

  @Test
  public void testConstructor() throws IOException {
    assertNotNull(zipStream);
  }

  @Test
  public void testGetNextZipEntry() throws IOException {
    assertNull(zipStream.getNextZipEntry());
  }

  @Test
  public void testGetNextEntry() throws IOException {
    assertNull(zipStream.getNextEntry());
  }

  @Test
  public void testRead() throws IOException {
    byte[] buffer = new byte[1024];
    assertEquals(-1, zipStream.read(buffer, 0, 1024));
  }

  @Test(expected = IOException.class)
  public void testReadClosed() throws IOException {
    zipStream.close();
    byte[] buffer = new byte[1024];
    zipStream.read(buffer, 0, 1024);
  }

  @Test
  public void testClose() throws IOException {
    assertFalse(zipStream.isClosed());
    zipStream.close();
    assertTrue(zipStream.isClosed());
  }

  @Test
  public void testSkip() throws IOException {
    assertEquals(0, zipStream.skip(1024L));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSkipNegative() throws IOException {
    zipStream.skip(-1L);
  }

  @Test
  public void testMatches() {
    assertFalse(ZipArchiveInputStream.matches(new byte[1024], 0));
  }

  @Test
  public void testChecksig() {
    byte[] expected = new byte[] { 1, 2, 3, 4 };
    byte[] signature = new byte[] { 1, 2, 3, 4, 5 };
    assertFalse(ZipArchiveInputStream.checksig(signature, expected));
  }

  @Test(expected = IOException.class)
  public void testCloseEntryClosed() throws IOException {
    zipStream.close();
    zipStream.closeEntry();
  }

  @Test
  public void testFill() throws IOException {
    zipStream.fill();
  }

  @Test(expected = IOException.class)
  public void testFillClosed() throws IOException {
    zipStream.close();
    zipStream.fill();
  }

  @Test(expected = EOFException.class)
  public void testReadFully() throws IOException {
    byte[] buffer = new byte[1024];
    zipStream.readFully(buffer);
  }

  @Test(expected = IOException.class)
  public void testReadFullyClosed() throws IOException {
    zipStream.close();
    byte[] buffer = new byte[1024];
    zipStream.readFully(buffer);
  }
}