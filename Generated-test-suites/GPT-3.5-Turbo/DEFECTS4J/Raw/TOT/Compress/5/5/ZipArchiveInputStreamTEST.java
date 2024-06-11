import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {
  
  private ZipArchiveInputStream zipInputStream;
  
  // Test data
  private String zipFilePath = "test.zip";
  private String extractedFilePath = "extractedFile.txt";
  private String content = "This is a test file";
  
  @Before
  public void setUp() {
    // Initialize the test input stream with a test zip file
    zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream(zipFilePath));
  }
  
  @Test
  public void getNextZipEntry_validZipEntry_returnsZipEntry() throws Exception {
    ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
    
    assertNotNull(entry);
    assertEquals(extractedFilePath, entry.getName());
    assertEquals(content.length(), entry.getSize());
    assertEquals(0, entry.getMethod());
    assertFalse(entry.isDirectory());
  }
  
  @Test
  public void getNextZipEntry_endOfZip_returnsNull() throws Exception {
    zipInputStream.getNextZipEntry();
    ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
    
    assertNull(entry);
  }
  
  @Test
  public void read_validZipEntry_returnsBytes() throws Exception {
    // Skip the first entry
    zipInputStream.getNextZipEntry();
    
    byte[] buffer = new byte[content.length()];
    int bytesRead = zipInputStream.read(buffer, 0, buffer.length);
    
    assertEquals(content.length(), bytesRead);
    assertEquals(content, new String(buffer));
  }
  
  @Test
  public void read_endOfZipEntry_returnsMinusOne() throws Exception {
    // Skip the first entry
    zipInputStream.getNextZipEntry();
    
    // Read the entire contents of the zip entry
    byte[] buffer = new byte[content.length()];
    zipInputStream.read(buffer, 0, buffer.length);
    
    // Read again should return -1
    int bytesRead = zipInputStream.read(buffer, 0, buffer.length);
    
    assertEquals(-1, bytesRead);
  }
  
  @Test(expected = IOException.class)
  public void close_inputStreamClosed_throwsIOException() throws Exception {
    zipInputStream.close();
    zipInputStream.getNextZipEntry();
  }
  
  @Test
  public void skip_validValue_skipsBytes() throws Exception {
    // Skip the first entry
    zipInputStream.getNextZipEntry();
    
    long skipped = zipInputStream.skip(content.length());
    
    assertEquals(content.length(), skipped);
    
    // Read should return -1 as all bytes are skipped
    byte[] buffer = new byte[10];
    int bytesRead = zipInputStream.read(buffer, 0, buffer.length);
    
    assertEquals(-1, bytesRead);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void skip_negativeValue_throwsIllegalArgumentException() throws Exception {
    zipInputStream.skip(-10);
  }
  
  @Test
  public void close_inputStreamNotClosed_noException() {
    try {
      zipInputStream.close();
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }
  
}