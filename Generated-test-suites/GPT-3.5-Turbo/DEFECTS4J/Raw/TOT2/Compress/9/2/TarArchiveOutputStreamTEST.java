import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
  
  @Test
  public void testSetLongFileMode() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(outputStream);
    t.setLongFileMode(1);
    assertEquals(1, t.getLongFileMode());
  }
  
  @Test
  public void testPutArchiveEntry() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(outputStream);
    TarArchiveEntry entry = new TarArchiveEntry("testEntry");
    t.putArchiveEntry(entry);
    // Assert that an entry has been added
    assertTrue(t.haveUnclosedEntry());
  }
  
  @Test
  public void testCloseArchiveEntry() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(outputStream);
    TarArchiveEntry entry = new TarArchiveEntry("testEntry");
    t.putArchiveEntry(entry);
    t.closeArchiveEntry();
    // Assert that the entry has been closed
    assertFalse(t.haveUnclosedEntry());
  }
  
  @Test
  public void testWrite() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(outputStream);
    byte[] bytes = "Test".getBytes();
    t.write(bytes, 0, 4);
    // Assert that data has been written
    assertEquals(4, t.getCurrBytes());
  }
  
  @Test
  public void testFinish() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(outputStream);
    t.finish();
    // Assert that finish has been called
    assertTrue(t.isFinished());
  }
  
  @Test
  public void testClose() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(outputStream);
    t.close();
    // Assert that the stream has been closed
    assertTrue(t.isClosed());
  }
  
}