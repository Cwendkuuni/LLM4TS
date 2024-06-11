import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

  // Test case for toString() method
  @Test
  public void testToString() {
    ArchiveEntry entry = new ArchiveEntry();
    entry.setName("main.c");
    entry.setDirectory(false);
    entry.setSize(2000);

    assertEquals("-    2000 main.c", ArchiveUtils.toString(entry));
  }

  // Test cases for matchAsciiBuffer() method
  @Test
  public void testMatchAsciiBuffer() {
    String expected = "Hello, World!";
    byte[] buffer = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!', 'E', 'x', 't', 'r', 'a'};
    assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, expected.length()));
  }

  @Test
  public void testMatchAsciiBuffer_EmptyBuffer() {
    String expected = "";
    byte[] buffer = {};
    assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
  }

  // Test case for toAsciiBytes() method
  @Test
  public void testToAsciiBytes() {
    String inputString = "Hello, World!";
    byte[] expected = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
  
    assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
  }

  // Test case for toAsciiString() method
  @Test
  public void testToAsciiString() {
    byte[] inputBytes = {'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
    String expected = "Hello, World!";
  
    assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
  }

  // Test case for isEqual() method
  @Test
  public void testIsEqual() {
    byte[] buffer1 = {'H', 'e', 'l', 'l', 'o'};
    byte[] buffer2 = {'H', 'e', 'l', 'l', 'o'};
  
    assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
  }

  // Test case for isArrayZero() method
  @Test
  public void testIsArrayZero() {
    byte[] array = {'H', 'e', 'l', 'l', 'o'};
  
    assertFalse(ArchiveUtils.isArrayZero(array, array.length));
  }

  @Test
  public void testIsArrayZero_EmptyArray() {
    byte[] array = {};
  
    assertTrue(ArchiveUtils.isArrayZero(array, array.length));
  }

  // Test case for sanitize() method
  @Test
  public void testSanitize() {
    String s = "Hello, \u0007 World!";
    String expected = "Hello, ? World!";
  
    assertEquals(expected, ArchiveUtils.sanitize(s));
  }

}