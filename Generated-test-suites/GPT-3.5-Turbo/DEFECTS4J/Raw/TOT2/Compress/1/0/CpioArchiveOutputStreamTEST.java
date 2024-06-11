import static org.junit.Assert.*;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

  @Test
  public void testConstructorWithFormat() {
    // Test constructor with format
    OutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    assertNotNull(cpioOutput);
  }

  @Test
  public void testConstructorWithoutFormat() {
    // Test constructor without format
    OutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    assertNotNull(cpioOutput);
  }

  @Test
  public void testPutNextEntry() {
    // Test putNextEntry method
    OutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    try {
      cpioOutput.putNextEntry(entry);
    } catch (IOException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testWrite() {
    // Test write method
    OutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    try {
      cpioOutput.putNextEntry(entry);
      cpioOutput.write("12345".getBytes(), 0, 5);
    } catch (IOException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testFinish() {
    // Test finish method
    OutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    try {
      cpioOutput.finish();
    } catch (IOException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testClose() {
    // Test close method
    OutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
    try {
      cpioOutput.close();
    } catch (IOException e) {
      fail("Exception should not be thrown");
    }
  }

}