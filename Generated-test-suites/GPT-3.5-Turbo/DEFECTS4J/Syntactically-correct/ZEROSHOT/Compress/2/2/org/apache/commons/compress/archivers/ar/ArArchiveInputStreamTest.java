import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {
  
  @Test
  public void testGetNextArEntry() throws IOException {
    byte[] header = "#!/usr/bin/env".getBytes();
    byte[] name = "testfile".getBytes();
    byte[] length = "1024".getBytes();
    byte[] trailer = "#1/".getBytes();
    byte[] content = "This is a test file.".getBytes();
    byte[] arEntryBytes = new byte[header.length + name.length + length.length + trailer.length + content.length];
    System.arraycopy(header, 0, arEntryBytes, 0, header.length);
    System.arraycopy(name, 0, arEntryBytes, header.length, name.length);
    System.arraycopy(length, 0, arEntryBytes, header.length + name.length, length.length);
    System.arraycopy(trailer, 0, arEntryBytes, header.length + name.length + length.length, trailer.length);
    System.arraycopy(content, 0, arEntryBytes, header.length + name.length + length.length + trailer.length, content.length);
    
    ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryBytes);
    ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
    ArchiveEntry arEntry = arInputStream.getNextArEntry();
    
    assertEquals("testfile", arEntry.getName());
    assertEquals(1024, arEntry.getSize());
    
    inputStream.close();
    arInputStream.close();
  }
  
  @Test
  public void testGetNextEntry() throws IOException {
    byte[] header = "#!/usr/bin/env".getBytes();
    byte[] name = "testfile".getBytes();
    byte[] length = "1024".getBytes();
    byte[] trailer = "#1/".getBytes();
    byte[] content = "This is a test file.".getBytes();
    byte[] arEntryBytes = new byte[header.length + name.length + length.length + trailer.length + content.length];
    System.arraycopy(header, 0, arEntryBytes, 0, header.length);
    System.arraycopy(name, 0, arEntryBytes, header.length, name.length);
    System.arraycopy(length, 0, arEntryBytes, header.length + name.length, length.length);
    System.arraycopy(trailer, 0, arEntryBytes, header.length + name.length + length.length, trailer.length);
    System.arraycopy(content, 0, arEntryBytes, header.length + name.length + length.length + trailer.length, content.length);
    
    ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryBytes);
    ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
    ArchiveEntry arEntry = arInputStream.getNextEntry();
    
    assertEquals("testfile", arEntry.getName());
    assertEquals(1024, arEntry.getSize());
    
    inputStream.close();
    arInputStream.close();
  }
  
  @Test
  public void testClose() throws IOException {
    byte[] arEntryBytes = new byte[0];
    ByteArrayInputStream inputStream = new ByteArrayInputStream(arEntryBytes);
    ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
    arInputStream.close();
    
    assertTrue(arInputStream.isClosed());
    inputStream.close();
  }
  
  @Test
  public void testRead() throws IOException {
    byte[] content = "This is a test file.".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
    ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
    
    assertEquals('T', arInputStream.read());
    
    arInputStream.close();
    inputStream.close();
  }
  
  @Test
  public void testReadBytes() throws IOException {
    byte[] content = "This is a test file.".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
    ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
    byte[] buffer = new byte[5];
    int bytesRead = arInputStream.read(buffer);
    
    assertEquals(5, bytesRead);
    assertEquals("This ", new String(buffer));
    
    arInputStream.close();
    inputStream.close();
  }
  
  @Test
  public void testMatches() {
    byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    int length = 8;
    
    assertTrue(ArArchiveInputStream.matches(signature, length));
  }
}