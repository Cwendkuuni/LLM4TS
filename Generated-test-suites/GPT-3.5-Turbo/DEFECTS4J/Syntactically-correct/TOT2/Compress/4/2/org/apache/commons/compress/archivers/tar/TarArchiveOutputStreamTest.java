import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
  
  @Test
  public void testConstructorDefaultBlockSize() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream t = new TarArchiveOutputStream(os);
    assertNotNull(t);
  }
  
  @Test
  public void testConstructorCustomBlockSize() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream t = new TarArchiveOutputStream(os, 512);
    assertNotNull(t);
  }
  
  @Test
  public void testConstructorCustomBlockAndRecordSize() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream t = new TarArchiveOutputStream(os, 512, 1024);
    assertNotNull(t);
  }
  
  @Test
  public void testSetLongFileMode() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    TarArchiveOutputStream t = new TarArchiveOutputStream(os);
    t.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, t.getLongFileMode());
  }
  
  @Test
  public void testPutArchiveEntry() {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      TarArchiveOutputStream t = new TarArchiveOutputStream(os);
      TarArchiveEntry entry = new TarArchiveEntry("test.txt");
      t.putArchiveEntry(entry);
      t.closeArchiveEntry();
      t.close();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testFinish() {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      TarArchiveOutputStream t = new TarArchiveOutputStream(os);
      t.finish();
      t.close();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testClose() {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      TarArchiveOutputStream t = new TarArchiveOutputStream(os);
      t.close();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testWrite() {
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      TarArchiveOutputStream t = new TarArchiveOutputStream(os);
      byte[] bytes = "Hello, World!".getBytes();
      t.write(bytes, 0, bytes.length);
      t.close();
    } catch (IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

}