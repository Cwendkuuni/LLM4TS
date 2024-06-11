import org.apache.commons.compress.archivers.ArchiveEntry;
import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {
  
  @Test
  public void testToString_whenEntryIsFile() {
    ArchiveEntry entry = new ArchiveEntry();
    entry.setDirectory(false);
    entry.setSize(2000);
    entry.setName("main.c");
    
    String expected = "-    2000 main.c";
    String actual = ArchiveUtils.toString(entry);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testToString_whenEntryIsDirectory() {
    ArchiveEntry entry = new ArchiveEntry();
    entry.setDirectory(true);
    entry.setSize(100);
    entry.setName("testfiles");
    
    String expected = "d     100 testfiles";
    String actual = ArchiveUtils.toString(entry);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testMatchAsciiBuffer_whenBuffersMatch() {
    String expected = "test";
    byte[] buffer = new byte[]{116, 101, 115, 116};
    
    assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
  }
  
  @Test
  public void testMatchAsciiBuffer_whenBuffersDoNotMatch() {
    String expected = "test";
    byte[] buffer = new byte[]{116, 101, 115, 116, 49};
    
    assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
  }
  
  @Test
  public void testToAsciiBytes() {
    String inputString = "test";
    byte[] expected = new byte[]{116, 101, 115, 116};
    
    assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
  }
  
  @Test
  public void testToAsciiString() {
    byte[] inputBytes = new byte[]{116, 101, 115, 116};
    String expected = "test";
    
    assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
  }
  
  @Test
  public void testToAsciiString_withOffsetAndLength() {
    byte[] inputBytes = new byte[]{116, 101, 115, 116};
    int offset = 1;
    int length = 2;
    String expected = "es";
    
    assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
  }
  
  @Test
  public void testIsEqual_whenBuffersMatch() {
    byte[] buffer1 = new byte[]{116, 101, 115, 116};
    byte[] buffer2 = new byte[]{116, 101, 115, 116};
    
    assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
  }
  
  @Test
  public void testIsEqual_whenBuffersDoNotMatch() {
    byte[] buffer1 = new byte[]{116, 101, 115, 116};
    byte[] buffer2 = new byte[]{116, 101, 115, 116, 49};
    
    assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
  }
  
  @Test
  public void testIsEqual_whenBuffersHaveDifferentLengths() {
    byte[] buffer1 = new byte[]{116, 101, 115, 116};
    byte[] buffer2 = new byte[]{116, 101, 115};
    
    assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
  }
  
  @Test
  public void testIsEqual_whenIgnoringTrailingNullsAndBuffersMatch() {
    byte[] buffer1 = new byte[]{116, 101, 115, 116, 0, 0, 0};
    byte[] buffer2 = new byte[]{116, 101, 115, 116};
    boolean ignoreTrailingNulls = true;
    
    assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, ignoreTrailingNulls));
  }
  
  @Test
  public void testIsEqual_whenIgnoringTrailingNullsAndBuffersDoNotMatch() {
    byte[] buffer1 = new byte[]{116, 101, 115, 116, 0, 0, 0};
    byte[] buffer2 = new byte[]{116, 101, 115, 116, 49};
    boolean ignoreTrailingNulls = true;
    
    assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, ignoreTrailingNulls));
  }
  
  @Test
  public void testIsEqualWithNull_whenBuffersMatch() {
    byte[] buffer1 = new byte[]{116, 101, 115, 116, 0, 0, 0};
    byte[] buffer2 = new byte[]{116, 101, 115, 116};
    
    assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
  }
  
  @Test
  public void testIsEqualWithNull_whenBuffersDoNotMatch() {
    byte[] buffer1 = new byte[]{116, 101, 115, 116, 0, 0, 0};
    byte[] buffer2 = new byte[]{116, 101, 115, 116, 49};
    
    assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
  }
  
  @Test
  public void testIsArrayZero_whenArrayHasZeroBytes() {
    byte[] a = new byte[]{0, 0, 0, 0, 0};
    int size = 5;
    
    assertTrue(ArchiveUtils.isArrayZero(a, size));
  }
  
  @Test
  public void testIsArrayZero_whenArrayHasNonZeroBytes() {
    byte[] a = new byte[]{0, 1, 2, 3, 4};
    int size = 5;
    
    assertFalse(ArchiveUtils.isArrayZero(a, size));
  }
  
  @Test
  public void testSanitize_whenStringIsSanitized() {
    String s = "abcd\n1234";
    String expected = "abcd?1234";
    
    String actual = ArchiveUtils.sanitize(s);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSanitize_whenStringIsNotSanitized() {
    String s = "abcd1234";
    String expected = "abcd1234";
    
    String actual = ArchiveUtils.sanitize(s);
    
    assertEquals(expected, actual);
  }
}