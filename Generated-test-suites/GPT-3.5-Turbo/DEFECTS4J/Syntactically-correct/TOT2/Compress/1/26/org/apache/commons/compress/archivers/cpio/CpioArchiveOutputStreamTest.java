import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;

public class CpioArchiveOutputStreamTest {
  
  @Test
  public void testPutNextEntry() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
    
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    
    cpioOutputStream.putNextEntry(entry);
    
    cpioOutputStream.closeArchiveEntry();
    cpioOutputStream.close();
    
    // Check if the entry was successfully added
    assertEquals(5, entry.getSize());
  }
  
  @Test
  public void testWrite() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
    
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    
    cpioOutputStream.putNextEntry(entry);
    cpioOutputStream.write("12345".getBytes(), 0, 5);
    
    cpioOutputStream.closeArchiveEntry();
    cpioOutputStream.close();
    
    // Check if the data was successfully written
    assertEquals("12345", new String(outputStream.toByteArray()));
  }
  
  @Test(expected = IOException.class)
  public void testInvalidEntrySize() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
    
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);
    
    cpioOutputStream.putNextEntry(entry);
    cpioOutputStream.write("123456".getBytes(), 0, 6);
    
    cpioOutputStream.closeArchiveEntry();
    cpioOutputStream.close();
  }

}