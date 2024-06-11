import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ArArchiveInputStreamTest {
  
  @Test
  public void testGetNextArEntry() throws IOException {
    ArArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(new byte[10]));
    assertNull("Expected null when input is empty", input.getNextArEntry());
    
    byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00, 0x00};
    input = new ArArchiveInputStream(new ByteArrayInputStream(data));
    assertNotNull("Expected non-null when input has data", input.getNextArEntry());
  }
  
  @Test
  public void testGetNextEntry() throws IOException {
    ArArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(new byte[10]));
    assertNull("Expected null when input is empty", input.getNextEntry());
    
    byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00, 0x00};
    input = new ArArchiveInputStream(new ByteArrayInputStream(data));
    assertNotNull("Expected non-null when input has data", input.getNextEntry());
  }
  
  @Test
  public void testClose() throws IOException {
    ArArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(new byte[10]));
    input.close();
    assertTrue("Expected input to be closed", input.isClosed());
  }
  
  @Test
  public void testRead() throws IOException {
    byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00, 0x00};
    ArArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(data));
    int read = input.read();
    assertEquals("Expected read to return the correct value", 33, read);
  }
  
  @Test
  public void testReadByteArray() throws IOException {
    byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63};
    byte[] buffer = new byte[5];
    ArArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(data));
    int read = input.read(buffer);
    assertArrayEquals("Expected buffer to be filled with correct values", new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63}, buffer);
    assertEquals("Expected read to return the number of bytes read", 5, read);
  }
  
  @Test
  public void testReadByteArrayWithOffsetAndLength() throws IOException {
    byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63};
    byte[] buffer = new byte[3];
    ArArchiveInputStream input = new ArArchiveInputStream(new ByteArrayInputStream(data));
    int read = input.read(buffer, 1, 2);
    assertArrayEquals("Expected buffer to be filled with correct values", new byte[] {0, 0x21, 0x3c}, buffer);
    assertEquals("Expected read to return the number of bytes read", 2, read);
  }
  
  @Test
  public void testMatches() {
    byte[] data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00, 0x00};
    assertTrue("Expected matches to return true", ArArchiveInputStream.matches(data, data.length));
    
    data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68};
    assertFalse("Expected matches to return false", ArArchiveInputStream.matches(data, data.length));
    
    data = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x69, 0x3e, 0x0a, 0x00, 0x00};
    assertFalse("Expected matches to return false", ArArchiveInputStream.matches(data, data.length));
  }
  
}