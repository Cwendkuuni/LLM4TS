import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

  @Test
  public void testPutArchiveEntry() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Test input values
    CpioArchiveEntry entry1 = new CpioArchiveEntry();
    entry1.setName("testfile");
    entry1.setFileSize(5);
    entry1.setMode(CpioConstants.C_ISREG);

    CpioArchiveEntry entry2 = new CpioArchiveEntry();
    entry2.setName("testfile2");
    entry2.setFileSize(10);
    entry2.setMode(CpioConstants.C_ISDIR);

    // Verify no exceptions are thrown
    cpio.putArchiveEntry(entry1);
    cpio.putArchiveEntry(entry2);
  }

  @Test(expected = IOException.class)
  public void testPutArchiveEntryDuplicate() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Test input values
    CpioArchiveEntry entry1 = new CpioArchiveEntry();
    entry1.setName("testfile");
    entry1.setFileSize(5);
    entry1.setMode(CpioConstants.C_ISREG);

    CpioArchiveEntry entry2 = new CpioArchiveEntry();
    entry2.setName("testfile");
    entry2.setFileSize(10);
    entry2.setMode(CpioConstants.C_ISDIR);

    // Verify exception is thrown
    cpio.putArchiveEntry(entry1);
    cpio.putArchiveEntry(entry2);
  }

  @Test
  public void testCloseArchiveEntry() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Test input values
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);

    // Verify no exceptions are thrown
    cpio.putArchiveEntry(entry);
    cpio.closeArchiveEntry();
  }

  @Test(expected = IOException.class)
  public void testCloseArchiveEntryInvalidSize() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Test input values
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);

    // Verify exception is thrown
    cpio.putArchiveEntry(entry);
    cpio.write(new byte[4]);
    cpio.closeArchiveEntry();
  }

  @Test
  public void testWrite() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Test input values
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);

    // Verify no exceptions are thrown
    cpio.putArchiveEntry(entry);
    cpio.write("12345".getBytes());
    cpio.closeArchiveEntry();
  }

  @Test(expected = IOException.class)
  public void testWriteExceedEntrySize() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Test input values
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);

    // Verify exception is thrown
    cpio.putArchiveEntry(entry);
    cpio.write("123456".getBytes());
    cpio.closeArchiveEntry();
  }

  @Test
  public void testFinish() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Verify no exceptions are thrown
    cpio.finish();
  }

  @Test(expected = IOException.class)
  public void testFinishUnclosedEntries() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Test input values
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);

    // Verify exception is thrown
    cpio.putArchiveEntry(entry);
    cpio.finish();
  }

  @Test
  public void testCreateArchiveEntry() throws Exception {
    CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(null);

    // Test input values
    File inputFile = new File("file.txt");
    String entryName = "testfile";

    // Verify no exceptions are thrown
    cpio.createArchiveEntry(inputFile, entryName);
  }

}