import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
  
  @Test
  public void testPutArchiveEntry() throws IOException {
    // Create a CpioArchiveOutputStream
    OutputStream out = new FileOutputStream(new File("test.cpio"));
    CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
    
    // Create a CpioArchiveEntry
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);
    
    // Add entry to cpio stream
    cpioOut.putArchiveEntry(entry);
    
    // Close the cpio stream
    cpioOut.closeArchiveEntry();
    cpioOut.close();
    
    // Verify that the file was written correctly
    File testFile = new File("test.cpio");
    assertTrue(testFile.exists());
    assertEquals(5, testFile.length());
  }
  
  @Test
  public void testCloseArchiveEntry() throws IOException {
    // Create a CpioArchiveOutputStream
    OutputStream out = new FileOutputStream(new File("test.cpio"));
    CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
    
    // Create a CpioArchiveEntry
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);
    
    // Add entry to cpio stream
    cpioOut.putArchiveEntry(entry);
    
    // Write data to cpio stream
    byte[] data = "12345".getBytes();
    cpioOut.write(data);
    
    // Verify that the entry size is correct
    assertEquals(data.length, entry.getSize());
    
    // Close the cpio entry
    cpioOut.closeArchiveEntry();
    
    // Close the cpio stream
    cpioOut.close();
    
    // Verify that the file was written correctly
    File testFile = new File("test.cpio");
    assertTrue(testFile.exists());
    assertEquals(9, testFile.length());
  }
  
  @Test
  public void testFinish() throws IOException {
    // Create a CpioArchiveOutputStream
    OutputStream out = new FileOutputStream(new File("test.cpio"));
    CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
    
    // Create a CpioArchiveEntry
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);
    
    // Add entry to cpio stream
    cpioOut.putArchiveEntry(entry);
    
    // Write data to cpio stream
    byte[] data = "12345".getBytes();
    cpioOut.write(data);
    
    // Close the cpio entry
    cpioOut.closeArchiveEntry();
    
    // Finish the cpio stream
    cpioOut.finish();
    
    // Close the cpio stream
    cpioOut.close();
    
    // Verify that the file was written correctly
    File testFile = new File("test.cpio");
    assertTrue(testFile.exists());
    assertEquals(13, testFile.length());
  }
  
  @Test
  public void testClose() throws IOException {
    // Create a CpioArchiveOutputStream
    OutputStream out = new FileOutputStream(new File("test.cpio"));
    CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
    
    // Create a CpioArchiveEntry
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    entry.setMode(CpioConstants.C_ISREG);
    
    // Add entry to cpio stream
    cpioOut.putArchiveEntry(entry);
    
    // Write data to cpio stream
    byte[] data = "12345".getBytes();
    cpioOut.write(data);
    
    // Close the cpio entry
    cpioOut.closeArchiveEntry();
    
    // Close the cpio stream
    cpioOut.close();
    
    // Verify that the file was written correctly
    File testFile = new File("test.cpio");
    assertTrue(testFile.exists());
    assertEquals(9, testFile.length());
    
    // Delete the test file
    testFile.delete();
    
    // Verify that the file was deleted
    assertFalse(testFile.exists());
  }
}