import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CpioArchiveOutputStreamTest {
  
  private CpioArchiveOutputStream cpioOut;

  @Before
  public void setUp() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    cpioOut = new CpioArchiveOutputStream(outputStream);
    
    // Add some entries to the archive
    CpioArchiveEntry entry1 = new CpioArchiveEntry();
    entry1.setName("testfile1");
    String contents1 = "12345";
    entry1.setFileSize(contents1.length());
    entry1.setMode(CpioConstants.C_ISREG);
    cpioOut.putArchiveEntry(entry1);
    cpioOut.write(contents1.getBytes());
    cpioOut.closeArchiveEntry();
    
    CpioArchiveEntry entry2 = new CpioArchiveEntry();
    entry2.setName("testfile2");
    String contents2 = "67890";
    entry2.setFileSize(contents2.length());
    entry2.setMode(CpioConstants.C_ISREG);
    cpioOut.putArchiveEntry(entry2);
    cpioOut.write(contents2.getBytes());
    cpioOut.closeArchiveEntry();
    
    // Finish writing the archive
    cpioOut.finish();
    cpioOut.close();
  }
  
  @Test
  public void testEntries() throws IOException {
    File outputFile = new File("test.cpio");
    FileOutputStream fileOut = new FileOutputStream(outputFile);
    fileOut.write(cpioOut.toByteArray());
    fileOut.close();
    
    CpioArchiveInputStream cpioIn = new CpioArchiveInputStream(outputFile);
    
    // Validate the first entry
    CpioArchiveEntry entry1 = cpioIn.getNextEntry();
    assertEquals("testfile1", entry1.getName());
    assertEquals("12345", new String(cpioIn.readAllBytes(entry1.getSize())));
    
    // Validate the second entry
    CpioArchiveEntry entry2 = cpioIn.getNextEntry();
    assertEquals("testfile2", entry2.getName());
    assertEquals("67890", new String(cpioIn.readAllBytes(entry2.getSize())));
    
    cpioIn.close();
  }
  
  @Test
  public void testChecksum() {
    long checksum = calculateChecksum(cpioOut.toByteArray());
    assertEquals(checksum, cpioOut.getCRC());
  }
  
  private long calculateChecksum(byte[] bytes) {
    Checksum checksum = new CRC32();
    checksum.update(bytes, 0, bytes.length);
    return checksum.getValue();
  }
  
}