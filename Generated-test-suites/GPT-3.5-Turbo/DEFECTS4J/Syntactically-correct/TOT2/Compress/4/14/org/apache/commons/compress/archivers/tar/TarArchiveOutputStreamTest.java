import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
  
  private TarArchiveOutputStream tarArchiveOutputStream;
  private ByteArrayOutputStream outputStream;
  
  @Before
  public void setup() {
    outputStream = new ByteArrayOutputStream();
    tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
  }
  
  @Test
  public void testPutArchiveEntry() throws IOException {
    ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    tarArchiveOutputStream.closeArchiveEntry();
    
    assertNotNull(tarArchiveOutputStream.getRecordSize());
  }
  
  @Test
  public void testCloseArchiveEntry() throws IOException {
    ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    tarArchiveOutputStream.closeArchiveEntry();
    
    assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
  }
  
  @Test
  public void testWrite() throws IOException {
    byte[] testBytes = {10, 20, 30};
    tarArchiveOutputStream.write(testBytes, 0, testBytes.length);
    tarArchiveOutputStream.closeArchiveEntry();
    
    assertEquals(3, tarArchiveOutputStream.currBytes);
  }
  
  @Test
  public void testFinish() throws IOException {
    ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    tarArchiveOutputStream.finish();
    
    assertEquals(2, outputStream.size());
  }
  
  @Test
  public void testClose() throws IOException {
    ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
    tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    tarArchiveOutputStream.close();
    
    assertTrue(tarArchiveOutputStream.closed);
  }
  
}