import org.junit.*;
import java.io.*;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

  @Test
  public void testTarArchiveOutputStreamConstructor() {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    assertNotNull(tarArchiveOutputStream);
  }

  @Test
  public void testSetLongFileMode() {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    tarArchiveOutputStream.setLongFileMode(1);
    int longFileMode = tarArchiveOutputStream.getLongFileMode();
    assertEquals(1, longFileMode);
  }

  @Test
  public void testFinish() throws IOException {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    tarArchiveOutputStream.finish();
    tarArchiveOutputStream.close();
    assertEquals(1024, os.toString().length());
  }

  @Test
  public void testClose() throws IOException {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    tarArchiveOutputStream.close();
    assertTrue(tarArchiveOutputStream.isClosed());
  }

  @Test
  public void testGetRecordSize() {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    int recordSize = tarArchiveOutputStream.getRecordSize();
    assertEquals(512, recordSize);
  }

  @Test
  public void testPutArchiveEntry() throws IOException {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    tarArchiveOutputStream.putArchiveEntry(entry);
    tarArchiveOutputStream.write("This is a test file.".getBytes());
    tarArchiveOutputStream.closeArchiveEntry();
    tarArchiveOutputStream.close();
    assertEquals(575, os.toString().length());
  }

  @Test
  public void testCloseArchiveEntry() throws IOException {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    tarArchiveOutputStream.putArchiveEntry(entry);
    tarArchiveOutputStream.write("This is a test file.".getBytes());
    tarArchiveOutputStream.closeArchiveEntry();
    tarArchiveOutputStream.close();
    assertTrue(tarArchiveOutputStream.isClosed());
  }

  @Test
  public void testWrite() throws IOException {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    tarArchiveOutputStream.write("This is a test file.".getBytes());
    tarArchiveOutputStream.write(new byte[512]);
    tarArchiveOutputStream.close();
    assertEquals(1024, os.toString().length());
  }

  @Test
  public void testWriteWithOffsetAndNumToWrite() throws IOException {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    tarArchiveOutputStream.write("This is a test file.".getBytes(), 0, 8);
    tarArchiveOutputStream.write("This is a test file.".getBytes(), 8, 8);
    tarArchiveOutputStream.write("This is a test file.".getBytes(), 16, 8);
    tarArchiveOutputStream.close();
    assertEquals(1024, os.toString().length());
  }

  @Test
  public void testCreateArchiveEntry() throws IOException {
    OutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    File file = new File("test.txt");
    ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
    assertTrue(entry instanceof TarArchiveEntry);
    assertEquals("test.txt", entry.getName());
  }

}