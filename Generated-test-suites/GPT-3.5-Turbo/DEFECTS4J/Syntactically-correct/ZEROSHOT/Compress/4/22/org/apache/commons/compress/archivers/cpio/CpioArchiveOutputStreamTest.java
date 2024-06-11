import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;

public class CpioArchiveOutputStreamTest {

  @Test
  public void testPutArchiveEntry() {
    try {
      CpioArchiveOutputStream out = new CpioArchiveOutputStream(System.out);
      // Test case 1
      CpioArchiveEntry entry1 = new CpioArchiveEntry();
      entry1.setName("test1");
      entry1.setFileSize(5);
      entry1.setMode(CpioConstants.C_ISREG);
      out.putArchiveEntry(entry1);
      assertEquals(entry1, out.getEntry());
      // Test case 2
      CpioArchiveEntry entry2 = new CpioArchiveEntry();
      entry2.setName("test2");
      entry2.setFileSize(8);
      entry2.setMode(CpioConstants.C_ISDIR);
      out.putArchiveEntry(entry2);
      assertEquals(entry2, out.getEntry());
      out.close();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCloseArchiveEntry() {
    try {
      CpioArchiveOutputStream out = new CpioArchiveOutputStream(System.out);
      CpioArchiveEntry entry = new CpioArchiveEntry();
      entry.setName("test");
      entry.setFileSize(6);
      entry.setMode(CpioConstants.C_ISREG);
      out.putArchiveEntry(entry);
      out.closeArchiveEntry();
      assertNull(out.getEntry());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testWrite() {
    try {
      CpioArchiveOutputStream out = new CpioArchiveOutputStream(System.out);
      CpioArchiveEntry entry = new CpioArchiveEntry();
      entry.setName("test");
      entry.setFileSize(5);
      entry.setMode(CpioConstants.C_ISREG);
      out.putArchiveEntry(entry);
      byte[] content = "12345".getBytes();
      out.write(content);
      out.closeArchiveEntry();
      out.close();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testFinish() {
    try {
      CpioArchiveOutputStream out = new CpioArchiveOutputStream(System.out);
      CpioArchiveEntry entry = new CpioArchiveEntry();
      entry.setName("test");
      entry.setFileSize(6);
      entry.setMode(CpioConstants.C_ISREG);
      out.putArchiveEntry(entry);
      out.finish();
      assertNotNull(out.getEntry());
      out.close();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testClose() {
    try {
      CpioArchiveOutputStream out = new CpioArchiveOutputStream(System.out);
      CpioArchiveEntry entry = new CpioArchiveEntry();
      entry.setName("test");
      entry.setFileSize(5);
      entry.setMode(CpioConstants.C_ISREG);
      out.putArchiveEntry(entry);
      out.closeArchiveEntry();
      out.close();
      assertTrue(out.getOutput().isClosed());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveEntry() {
    try {
      CpioArchiveOutputStream out = new CpioArchiveOutputStream(System.out);
      File inputFile = new File("test.txt");
      String entryName = "testFile";
      CpioArchiveEntry entry = (CpioArchiveEntry)out.createArchiveEntry(inputFile, entryName);
      assertEquals(inputFile, entry.getInputFile());
      assertEquals(entryName, entry.getName());
      assertEquals(inputFile.length(), entry.getFileSize());
      assertEquals(CpioConstants.C_ISREG, entry.getMode());
      out.close();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}