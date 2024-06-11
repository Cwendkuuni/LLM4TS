import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {
  
  @Test
  public void testToString() {
    ArchiveEntry entry = new ArchiveEntry();
    entry.setDirectory(false);
    entry.setSize(2000);
    entry.setName("main.c");
    
    String expected = "-    2000 main.c";
    
    String result = ArchiveUtils.toString(entry);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testMatchAsciiBuffer() {
    String expected = "Hello, World!";
    byte[] buffer = expected.getBytes();
    
    boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
    
    assertTrue(result);
  }
  
  @Test
  public void testToAsciiBytes() {
    String inputString = "Hello, World!";
    byte[] expected = inputString.getBytes();
    
    byte[] result = ArchiveUtils.toAsciiBytes(inputString);
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testToAsciiString() {
    byte[] inputBytes = "Hello, World!".getBytes();
    String expected = "Hello, World!";
    
    String result = ArchiveUtils.toAsciiString(inputBytes);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testToAsciiStringWithOffsetAndLength() {
    byte[] inputBytes = "Hello, World!".getBytes();
    int offset = 0;
    int length = 5;
    String expected = "Hello";
    
    String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testIsEqual() {
    byte[] buffer1 = "Hello".getBytes();
    byte[] buffer2 = "Hello".getBytes();
    
    boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsEqualWithIgnoreTrailingNulls() {
    byte[] buffer1 = "Hello\0\0\0".getBytes();
    byte[] buffer2 = "Hello".getBytes();
    
    boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsEqualWithOffsetAndLength() {
    byte[] buffer1 = "Hello, World!".getBytes();
    int offset1 = 0;
    int length1 = 5;
    byte[] buffer2 = "Hello, Universe!".getBytes();
    int offset2 = 0;
    int length2 = 5;
    
    boolean result = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsEqualWithOffsetAndLengthAndIgnoreTrailingNulls() {
    byte[] buffer1 = "Hello, World!\0\0\0".getBytes();
    int offset1 = 0;
    int length1 = 5;
    byte[] buffer2 = "Hello, Universe!".getBytes();
    int offset2 = 0;
    int length2 = 5;
    
    boolean result = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsArrayZero() {
    byte[] array = new byte[5];
    
    boolean result = ArchiveUtils.isArrayZero(array, array.length);
    
    assertTrue(result);
  }
  
  @Test
  public void testSanitize() {
    String input = "Hello, World!\n";
    String expected = "Hello, World!?\n";
    
    String result = ArchiveUtils.sanitize(input);
    
    assertEquals(expected, result);
  }
}