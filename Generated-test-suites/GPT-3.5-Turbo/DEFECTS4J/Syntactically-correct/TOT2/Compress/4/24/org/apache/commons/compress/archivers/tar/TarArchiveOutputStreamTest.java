import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class TarArchiveOutputStreamTest {
  
  // Test all public methods of TarArchiveOutputStream
  
  @Test
  public void testPutArchiveEntry() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);
    
    try {
      ArchiveEntry entry = tao.createArchiveEntry(new File("test.txt"), "test.txt");
      tao.putArchiveEntry(entry);
      tao.closeArchiveEntry();
    } catch (IOException e) {
      fail("Failed to put archive entry");
    }
    
    assertEquals(512, baos.size()); // Header size should be 512 bytes
  }
  
  @Test
  public void testCloseArchiveEntry() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);
    
    try {
      ArchiveEntry entry = tao.createArchiveEntry(new File("test.txt"), "test.txt");
      tao.putArchiveEntry(entry);
      tao.write("Test data".getBytes());
      tao.closeArchiveEntry();
    } catch (IOException e) {
      fail("Failed to close archive entry");
    }
    
    assertEquals(1024, baos.size()); // Header size + data size should be 1024 bytes
  }
  
  @Test
  public void testWrite() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);
    
    try {
      ArchiveEntry entry = tao.createArchiveEntry(new File("test.txt"), "test.txt");
      tao.putArchiveEntry(entry);
      tao.write("Test data".getBytes());
      tao.closeArchiveEntry();
    } catch (IOException e) {
      fail("Failed to write data to archive entry");
    }
    
    assertEquals(1024, baos.size()); // Header size + data size should be 1024 bytes
  }
  
  @Test
  public void testFinish() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);
    
    try {
      tao.finish();
      tao.close();
    } catch (IOException e) {
      fail("Failed to finish and close the archive");
    }
    
    assertTrue(baos.size() > 0); // Archive should contain data
  }
}