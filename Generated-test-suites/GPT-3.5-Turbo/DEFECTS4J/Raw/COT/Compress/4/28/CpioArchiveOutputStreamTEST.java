
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

  @Test
  public void testPutArchiveEntry() {
    CpioArchiveOutputStream output = new CpioArchiveOutputStream(null);
    CpioArchiveEntry entry = new CpioArchiveEntry();
    try {
      output.putArchiveEntry(entry);
      assertTrue(output.isInEntry());
      assertEquals(entry, output.getCurrentEntry());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testCloseArchiveEntry() {
    CpioArchiveOutputStream output = new CpioArchiveOutputStream(null);
    CpioArchiveEntry entry = new CpioArchiveEntry();
    try {
      output.putArchiveEntry(entry);
      output.closeArchiveEntry();
      assertFalse(output.isInEntry());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testWrite() {
    CpioArchiveOutputStream output = new CpioArchiveOutputStream(null);
    byte[] data = new byte[]{1, 2, 3, 4, 5};
    try {
      output.putArchiveEntry(new CpioArchiveEntry());
      output.write(data);
      output.closeArchiveEntry();
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testFinish() {
    CpioArchiveOutputStream output = new CpioArchiveOutputStream(null);
    try {
      output.finish();
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

  @Test
  public void testClose() {
    CpioArchiveOutputStream output = new CpioArchiveOutputStream(null);
    try {
      output.close();
      assertTrue(output.isClosed());
    } catch (IOException e) {
      fail("IOException occurred");
    }
  }

}
