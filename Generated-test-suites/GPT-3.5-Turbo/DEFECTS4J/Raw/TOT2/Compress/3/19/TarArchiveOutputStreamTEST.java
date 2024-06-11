import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

  @Test
  public void testConstructorWithOutputStream() {
    assertNotNull(new TarArchiveOutputStream(new ByteArrayOutputStream()));
  }

  @Test
  public void testConstructorWithOutputStreamAndBlockSize() {
    assertNotNull(new TarArchiveOutputStream(new ByteArrayOutputStream(), 512));
  }

  @Test
  public void testConstructorWithOutputStreamAndCustomBlockSizeAndRecordSize() {
    assertNotNull(new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024));
  }

  @Test
  public void testSetLongFileMode() {
    TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
    tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tar.longFileMode);
  }

  @Test
  public void testFinish() {
    TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
    try {
      tar.finish();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testClose() {
    TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
    try {
      tar.close();
      assertTrue(tar.closed);
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testGetRecordSize() {
    TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
    assertEquals(TarBuffer.DEFAULT_RCDSIZE, tar.getRecordSize());
  }

  @Test
  public void testPutArchiveEntry() {
    TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
    try {
      tar.putArchiveEntry(new TarArchiveEntry("test"));
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCloseArchiveEntry() {
    TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
    try {
      tar.putArchiveEntry(new TarArchiveEntry("test", TarArchiveEntry.DEFAULT_DIR_MODE));
      tar.closeArchiveEntry();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testWrite() {
    TarArchiveOutputStream tar = new TarArchiveOutputStream(new ByteArrayOutputStream());
    try {
      tar.putArchiveEntry(new TarArchiveEntry("test"));
      byte[] data = "Test data".getBytes();
      tar.write(data, 0, data.length);
      tar.closeArchiveEntry();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}