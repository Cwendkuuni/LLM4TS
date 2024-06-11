import org.junit.Test;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
  
  @Test
  public void testPutArchiveEntry() throws Exception {
    CpioArchiveOutputStream out = new CpioArchiveOutputStream();
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    String contents = "12345";
    entry.setFileSize(contents.length());
    entry.setMode(CpioConstants.C_ISREG);
    
    out.putArchiveEntry(entry);
    out.write(contents.getBytes());
    out.closeArchiveEntry();
    
    out.close();
  }
  
  @Test
  public void testCloseArchiveEntry() throws Exception {
    CpioArchiveOutputStream out = new CpioArchiveOutputStream();
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    String contents = "12345";
    entry.setFileSize(contents.length());
    entry.setMode(CpioConstants.C_ISREG);
    
    out.putArchiveEntry(entry);
    out.write(contents.getBytes());
    out.closeArchiveEntry();
    
    out.close();
  }
  
  @Test
  public void testWrite() throws Exception {
    CpioArchiveOutputStream out = new CpioArchiveOutputStream();
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    String contents = "12345";
    entry.setFileSize(contents.length());
    entry.setMode(CpioConstants.C_ISREG);
    
    out.putArchiveEntry(entry);
    out.write(contents.getBytes());
    out.closeArchiveEntry();
    
    out.close();
  }
  
  @Test
  public void testFinish() throws Exception {
    CpioArchiveOutputStream out = new CpioArchiveOutputStream();
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    String contents = "12345";
    entry.setFileSize(contents.length());
    entry.setMode(CpioConstants.C_ISREG);
    
    out.putArchiveEntry(entry);
    out.write(contents.getBytes());
    out.closeArchiveEntry();
    
    out.finish();
    
    out.close();
  }
  
  @Test
  public void testClose() throws Exception {
    CpioArchiveOutputStream out = new CpioArchiveOutputStream();
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    String contents = "12345";
    entry.setFileSize(contents.length());
    entry.setMode(CpioConstants.C_ISREG);
    
    out.putArchiveEntry(entry);
    out.write(contents.getBytes());
    out.closeArchiveEntry();
    
    out.close();
  }
  
  @Test
  public void testCreateArchiveEntry() throws Exception {
    CpioArchiveOutputStream out = new CpioArchiveOutputStream();
    
    File inputFile = new File("testfile.txt");
    String entryName = "testfile";
    
    CpioArchiveEntry entry = out.createArchiveEntry(inputFile, entryName);
    
    assertNotNull(entry);
    assertEquals(inputFile, entry.getFile());
    assertEquals(entryName, entry.getName());
    
    out.close();
  }
  
}