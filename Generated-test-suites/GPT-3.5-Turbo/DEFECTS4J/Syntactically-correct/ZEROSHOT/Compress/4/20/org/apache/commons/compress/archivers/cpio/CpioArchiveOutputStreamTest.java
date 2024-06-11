import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

  @Test
  public void testPutArchiveEntry() {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);
    try {
      cpio.putArchiveEntry(entry);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
    // assert statements
    assertEquals(1, cpio.getNames().size());
    assertEquals(entry, cpio.getEntry());

    try {
      cpio.closeArchiveEntry();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testWrite() {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);
    byte[] data = "12345".getBytes();
    try {
      cpio.putArchiveEntry(entry);
      cpio.write(data, 0, data.length);
      cpio.closeArchiveEntry();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
    // assert statements
    assertEquals(data.length, cpio.getWritten());
  }

  @Test
  public void testClose() {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);
    try {
      cpio.close();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
    // assert statements
    assertTrue(cpio.isClosed());
  }

  @Test
  public void testFinish() {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);
    try {
      cpio.finish();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
    // assert statements
    assertTrue(cpio.isFinished());
  }

}