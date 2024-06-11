import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
  
  private CpioArchiveOutputStream cpioOutputStream;
  
  @Before
  public void setUp() {
    cpioOutputStream = new CpioArchiveOutputStream(null);
  }
  
  @After
  public void tearDown() {
    cpioOutputStream = null;
  }
  
  @Test
  public void testPutNextEntry() {
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    
    try {
      cpioOutputStream.putNextEntry(entry);
    } catch (Exception e) {
      fail("putNextEntry should not throw an exception");
    }
  }
  
  @Test
  public void testCloseArchiveEntry() {
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    
    try {
      cpioOutputStream.putNextEntry(entry);
      cpioOutputStream.closeArchiveEntry();
    } catch (Exception e) {
      fail("closeArchiveEntry should not throw an exception");
    }
  }
  
  @Test
  public void testWrite() {
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    
    byte[] data = "12345".getBytes();
    
    try {
      cpioOutputStream.putNextEntry(entry);
      cpioOutputStream.write(data, 0, data.length);
      cpioOutputStream.closeArchiveEntry();
    } catch (Exception e) {
      fail("write should not throw an exception");
    }
  }
  
  @Test
  public void testFinish() {
    try {
      cpioOutputStream.finish();
    } catch (Exception e) {
      fail("finish should not throw an exception");
    }
  }
  
  @Test
  public void testClose() {
    try {
      cpioOutputStream.close();
    } catch (Exception e) {
      fail("close should not throw an exception");
    }
  }
}