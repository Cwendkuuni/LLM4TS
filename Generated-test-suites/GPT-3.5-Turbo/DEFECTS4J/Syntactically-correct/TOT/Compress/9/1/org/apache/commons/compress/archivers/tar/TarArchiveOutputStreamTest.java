import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.CRC32;

public class TarArchiveOutputStreamTest {

  private TarArchiveOutputStream tarOutputStream;
  private ByteArrayOutputStream byteArrayOutputStream;

  @Before
  public void setup() {
    byteArrayOutputStream = new ByteArrayOutputStream();
    tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
  }

  @Test
  public void testPutArchiveEntry_CreatesValidEntry() throws IOException {
    File file = new File("testFile.txt");
    TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(file, "testFile.txt");

    tarOutputStream.putArchiveEntry(entry);

    TarBuffer buffer = tarOutputStream.buffer;

    assertEquals(file.getName(), buffer.getName());
    assertEquals(file.isDirectory(), buffer.isDirectory());
    assertEquals(file.length(), buffer.getSize());
  }

  @Test
  public void testCloseArchiveEntry_ClosesEntryCorrectly() throws IOException {
    File file = new File("testFile.txt");
    TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(file, "testFile.txt");

    tarOutputStream.putArchiveEntry(entry);

    tarOutputStream.write("Hello, World!".getBytes());

    tarOutputStream.closeArchiveEntry();

    TarBuffer buffer = tarOutputStream.buffer;

    assertEquals(0, buffer.getAssemLen());
    assertFalse(buffer.isHaveUnclosedEntry());
  }

  @Test
  public void testSetLongFileMode_SetsCorrectMode() {
    int longFileMode = TarArchiveOutputStream.LONGFILE_GNU;

    tarOutputStream.setLongFileMode(longFileMode);

    assertEquals(longFileMode, tarOutputStream.getLongFileMode());
  }

  @Test
  public void testFinish_AddsTwoEOFRercordsToEndOfArchive() throws IOException {
    tarOutputStream.finish();

    byte[] byteArray = byteArrayOutputStream.toByteArray();

    assertEquals(1024, byteArray.length);
    assertEquals(0, byteArray[512]);
    assertEquals(0, byteArray[1023]);
  }

  @Test(expected = IOException.class)
  public void testClose_ArchiveNotFinished_ThrowsIOException() throws IOException {
    tarOutputStream.close();

    tarOutputStream.write("Hello, World!".getBytes());
  }

  @Test
  public void testGetRecordSize_ReturnsCorrectRecordSize() {
    assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
  }

  @Test
  public void testWrite_WritesBytesCorrectly() throws IOException {
    String testData = "Hello, World!";
    byte[] bytes = testData.getBytes();

    tarOutputStream.write(bytes);

    byte[] byteArray = byteArrayOutputStream.toByteArray();

    assertArrayEquals(bytes, byteArray);
  }

  @Test
  public void testCreateArchiveEntry_CreatesValidTarArchiveEntry() throws IOException {
    File file = new File("testFile.txt");

    ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, "testFile.txt");

    assertEquals(file.getName(), entry.getName());
    assertEquals(file.getAbsolutePath(), entry.getFile().getAbsolutePath());
  }

}