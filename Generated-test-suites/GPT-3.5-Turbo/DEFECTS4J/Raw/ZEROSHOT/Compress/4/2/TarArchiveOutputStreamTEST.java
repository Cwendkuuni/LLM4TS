import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
  
  private ByteArrayOutputStream outputStream;
  private TarArchiveOutputStream tarOutputStream;
  
  @Before
  public void setUp() {
    outputStream = new ByteArrayOutputStream();
    tarOutputStream = new TarArchiveOutputStream(outputStream);
  }
  
  @Test
  public void testConstructor() {
    assertNotNull(tarOutputStream);
  }

  @Test
  public void testSetLongFileMode() {
    tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
  }

  @Test
  public void testGetRecordSize() {
    assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
  }

  @Test
  public void testPutArchiveEntry() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.closeArchiveEntry();
    tarOutputStream.finish();
    assertTrue(outputStream.toByteArray().length > 0);
  }

  @Test
  public void testCloseArchiveEntry() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.write("test".getBytes());
    tarOutputStream.closeArchiveEntry();
    tarOutputStream.finish();
    assertTrue(outputStream.toByteArray().length > 0);
  }

  @Test
  public void testWrite() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.write("test".getBytes());
    tarOutputStream.closeArchiveEntry();
    tarOutputStream.finish();
    assertTrue(outputStream.toByteArray().length > 0);
  }

  @Test
  public void testFinish() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.write("test".getBytes());
    tarOutputStream.closeArchiveEntry();
    tarOutputStream.finish();
    assertTrue(outputStream.toByteArray().length > 0);
  }

  @Test
  public void testClose() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.write("test".getBytes());
    tarOutputStream.closeArchiveEntry();
    tarOutputStream.close();
    assertTrue(outputStream.toByteArray().length > 0);
  }

  @Test
  public void testCreateArchiveEntry() throws IOException {
    File file = new File("test.txt");
    TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(file, file.getName());
    assertEquals(file.getName(), entry.getName());
  }

}