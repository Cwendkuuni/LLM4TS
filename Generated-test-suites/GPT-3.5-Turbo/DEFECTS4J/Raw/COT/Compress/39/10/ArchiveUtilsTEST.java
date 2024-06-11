
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

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
    String expected = "test";
    byte[] buffer = new byte[] {116, 101, 115, 116};
    
    boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
    
    assertTrue(result);
  }

  @Test
  public void testToAsciiBytes() {
    String inputString = "test";
    byte[] expected = new byte[] {116, 101, 115, 116};
    
    byte[] result = ArchiveUtils.toAsciiBytes(inputString);
    
    assertArrayEquals(expected, result);
  }

  @Test
  public void testToAsciiString() {
    byte[] inputBytes = new byte[] {116, 101, 115, 116};
    String expected = "test";
    
    String result = ArchiveUtils.toAsciiString(inputBytes);
    
    assertEquals(expected, result);
  }

  @Test
  public void testIsEqual() {
    byte[] buffer1 = new byte[] {116, 101, 115, 116};
    byte[] buffer2 = new byte[] {116, 101, 115, 116};
    
    boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
    
    assertTrue(result);
  }

  @Test
  public void testIsEqualWithNull() {
    byte[] buffer1 = new byte[] {116, 101, 115, 116};
    byte[] buffer2 = new byte[] {116, 101, 115, 116};
    
    boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
    
    assertTrue(result);
  }

  @Test
  public void testIsArrayZero() {
    byte[] array = new byte[] {0, 0, 0, 0};
    int size = 4;
    
    boolean result = ArchiveUtils.isArrayZero(array, size);
    
    assertTrue(result);
  }

  @Test
  public void testSanitize() {
    String s = "test";
    String expected = "test";
    
    String result = ArchiveUtils.sanitize(s);
    
    assertEquals(expected, result);
  }

}
