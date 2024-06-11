import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {
  
  @Test
  public void testToString() {
    ArchiveEntry entry1 = new ArchiveEntry();
    entry1.setDirectory(false);
    entry1.setSize(1000);
    entry1.setName("main.c");
    
    String expected1 = "-    1000 main.c";
    String result1 = ArchiveUtils.toString(entry1);
    
    assertEquals(expected1, result1);
    
    ArchiveEntry entry2 = new ArchiveEntry();
    entry2.setDirectory(true);
    entry2.setSize(100);
    entry2.setName("testfiles");
    
    String expected2 = "d     100 testfiles";
    String result2 = ArchiveUtils.toString(entry2);
    
    assertEquals(expected2, result2);
  }
  
  @Test
  public void testMatchAsciiBuffer() {
    String expected = "hello";
    byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o'};
    
    boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
    
    assertTrue(result);
  }
  
  @Test
  public void testToAsciiBytes() {
    String inputString = "hello";
    byte[] expected = new byte[]{'h', 'e', 'l', 'l', 'o'};
    
    byte[] result = ArchiveUtils.toAsciiBytes(inputString);
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testToAsciiString() {
    byte[] inputBytes = new byte[]{'h', 'e', 'l', 'l', 'o'};
    String expected = "hello";
    
    String result = ArchiveUtils.toAsciiString(inputBytes);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testToAsciiStringWithOffsetAndLength() {
    byte[] inputBytes = new byte[]{'h', 'e', 'l', 'l', 'o'};
    int offset = 1;
    int length = 3;
    String expected = "ell";
    
    String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testIsEqual() {
    byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
    byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};
    
    boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsEqualWithIgnoreTrailingNulls() {
    byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0};
    byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0, 0};
    
    boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsEqualWithOffsetAndLength() {
    byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
    int offset1 = 1;
    int length1 = 3;
    byte[] buffer2 = new byte[]{'e', 'l', 'l'};
    int offset2 = 0;
    int length2 = 3;
    
    boolean result = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsArrayZero() {
    byte[] array = new byte[]{'h', 'e', 'l', 'l', 'o'};
    int size = 5;
    
    boolean result = ArchiveUtils.isArrayZero(array, size);
    
    assertFalse(result);
  }
  
  @Test
  public void testSanitize() {
    String s = "h\0e\0l\0l\0o";
    String expected = "h?e?l?l?o";
    
    String result = ArchiveUtils.sanitize(s);
    
    assertEquals(expected, result);
  }
}