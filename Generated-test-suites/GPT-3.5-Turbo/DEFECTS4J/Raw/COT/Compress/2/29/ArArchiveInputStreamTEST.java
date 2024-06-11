
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {
  
  private ArArchiveInputStream arInputStream;

  @Before
  public void setUp() {
    // Create InputStream for testing
    InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
  
    // Create ArArchiveInputStream instance
    arInputStream = new ArArchiveInputStream(inputStream);
  }

  // Test getNextArEntry method
  @Test
  public void testGetNextArEntry() throws IOException {
    ArArchiveEntry entry = arInputStream.getNextArEntry();
    assertNotNull(entry);
    
    // Verify the entry's metadata
    assertEquals("Test Entry", entry.getName());
    assertEquals(10, entry.getLength());
  }

  // Test getNextEntry method
  @Test
  public void testGetNextEntry() throws IOException {
    ArchiveEntry entry = arInputStream.getNextEntry();
    assertNotNull(entry);
    
    // Verify the entry's metadata
    assertEquals("Test Entry", entry.getName());
    assertEquals(10, entry.getLength());
  }

  // Test close method
  @Test
  public void testClose() throws IOException {
    // Call the close method
    arInputStream.close();
    
    // Verify that the input stream is closed
    assertTrue(arInputStream.isClosed());
  }

  // Test read method
  @Test
  public void testRead() throws IOException {
    int data = arInputStream.read();
    
    // Verify the read data
    assertEquals('T', data);
  }

  // Test read(byte[]) method
  @Test
  public void testReadByteArray() throws IOException {
    byte[] buffer = new byte[5];
    int bytesRead = arInputStream.read(buffer);
  
    // Verify the number of bytes read
    assertEquals(5, bytesRead);
    
    // Verify the read data
    assertArrayEquals("Test ".getBytes(), buffer);
  }

  // Test read(byte[], int, int) method
  @Test
  public void testReadByteArrayWithOffsetAndLength() throws IOException {
    byte[] buffer = new byte[10];
    int bytesRead = arInputStream.read(buffer, 2, 5);
  
    // Verify the number of bytes read
    assertEquals(5, bytesRead);
    
    // Verify the read data
    assertArrayEquals(new byte[]{0, 0, 'T', 'e', 's', 't', 0, 0, 0, 0}, buffer);
  }

  // Test matches method
  @Test
  public void testMatches() {
    // Test matching signature
    byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    assertTrue(ArArchiveInputStream.matches(signature, 8));
  
    // Test non-matching signature
    byte[] nonMatchingSignature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x00};
    assertFalse(ArArchiveInputStream.matches(nonMatchingSignature, 8));
    
    // Test signature with length less than 8
    byte[] shortSignature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68};
    assertFalse(ArArchiveInputStream.matches(shortSignature, 6));
  }
}

