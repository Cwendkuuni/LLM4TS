import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {
  
  private CpioArchiveOutputStream cpioOutputStream;
  
  @Before
  public void setUp() {
    cpioOutputStream = new CpioArchiveOutputStream(new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        // Do nothing for testing purposes
      }
    });
  }
  
  @After
  public void tearDown() {
    try {
      cpioOutputStream.close();
    } catch (IOException e) {
      fail("Error closing CpioArchiveOutputStream");
    }
  }

  @Test
  public void testPutArchiveEntry() {
    // Create a new CpioArchiveEntry
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setMode(CpioConstants.C_ISREG);
    entry.setTime(System.currentTimeMillis());
    
    // Put the entry in the archive
    try {
      cpioOutputStream.putArchiveEntry(entry);
    } catch (IOException e) {
      fail("Error putting archive entry");
    }
    
    // Check if the entry is correctly stored in the output stream
    assertNotNull(cpioOutputStream.getEntry());
  }

  @Test
  public void testWrite() {
    // Create a new CpioArchiveEntry
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setMode(CpioConstants.C_ISREG);
    entry.setTime(System.currentTimeMillis());
    
    // Put the entry in the archive
    try {
      cpioOutputStream.putArchiveEntry(entry);
    } catch (IOException e) {
      fail("Error putting archive entry");
    }
    
    // Write data to the archive entry
    byte[] data = {1, 2, 3, 4, 5};
    try {
      cpioOutputStream.write(data, 0, data.length);
    } catch (IOException e) {
      fail("Error writing data to archive entry");
    }
    
    // Check if the data is correctly written to the output stream
    assertEquals(data.length, cpioOutputStream.getWritten());
  }

  @Test
  public void testCloseArchiveEntry() {
    // Create a new CpioArchiveEntry
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setMode(CpioConstants.C_ISREG);
    entry.setTime(System.currentTimeMillis());
    
    // Put the entry in the archive
    try {
      cpioOutputStream.putArchiveEntry(entry);
    } catch (IOException e) {
      fail("Error putting archive entry");
    }
    
    // Close the current archive entry
    try {
      cpioOutputStream.closeArchiveEntry();
    } catch (IOException e) {
      fail("Error closing archive entry");
    }
    
    // Check if the entry is closed
    assertNull(cpioOutputStream.getEntry());
  }

  @Test
  public void testFinish() {
    // Create a new CpioArchiveEntry
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setMode(CpioConstants.C_ISREG);
    entry.setTime(System.currentTimeMillis());
    
    // Put the entry in the archive
    try {
      cpioOutputStream.putArchiveEntry(entry);
    } catch (IOException e) {
      fail("Error putting archive entry");
    }
    
    // Finish the CPIO output stream
    try {
      cpioOutputStream.finish();
    } catch (IOException e) {
      fail("Error finishing CPIO output stream");
    }
    
    // Check if the stream is finished
    assertTrue(cpioOutputStream.isFinished());
  }

}