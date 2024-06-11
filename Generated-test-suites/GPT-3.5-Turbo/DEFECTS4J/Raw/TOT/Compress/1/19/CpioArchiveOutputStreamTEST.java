import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

  // Test case for putNextEntry method
  @Test
  public void testPutNextEntry() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);

    // Test typical use case
    CpioArchiveEntry entry = new CpioArchiveEntry();
    cpioOutput.putNextEntry(entry);
    cpioOutput.closeArchiveEntry();

    // Test edge case
    CpioArchiveEntry entryEdge = new CpioArchiveEntry();
    entryEdge.setName("testfile");
    entryEdge.setSize(10);
    cpioOutput.putNextEntry(entryEdge);
    cpioOutput.closeArchiveEntry();

    // Test error scenario
    try {
      CpioArchiveEntry entryError = new CpioArchiveEntry();
      cpioOutput.putNextEntry(entryError);
      fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      assertEquals("no current CPIO entry", e.getMessage());
    }

    cpioOutput.close();
  }

  // Test case for closeArchiveEntry method
  @Test
  public void testCloseArchiveEntry() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    CpioArchiveEntry entry = new CpioArchiveEntry();
    cpioOutput.putNextEntry(entry);

    // Test typical use case
    cpioOutput.closeArchiveEntry();

    // Test edge case
    try {
      cpioOutput.closeArchiveEntry();
      fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      assertEquals("invalid entry size (expected 0 but got 10 bytes)", e.getMessage());
    }

    cpioOutput.close();
  }

  // Test case for write method
  @Test
  public void testWrite() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setSize(100);
    cpioOutput.putNextEntry(entry);

    byte[] data = "Hello World".getBytes();

    // Test typical use case
    cpioOutput.write(data);

    // Test edge cases
    try {
      byte[] dataEdge = new byte[200];
      cpioOutput.write(dataEdge);
      fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      assertEquals("attempt to write past end of STORED entry", e.getMessage());
    }

    // Test error scenario
    try {
      byte[] dataError = null;
      cpioOutput.write(dataError);
      fail("Expected an IOException to be thrown");
    } catch (IOException e) {
      assertEquals("no current CPIO entry", e.getMessage());
    }

    cpioOutput.close();
  }

  // Test case for finish method
  @Test
  public void testFinish() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    cpioOutput.finish();
    cpioOutput.finish();
    cpioOutput.close();
  }

  // Test case for close method
  @Test
  public void testClose() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    cpioOutput.close();
    cpioOutput.close();
  }
}