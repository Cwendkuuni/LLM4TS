import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {
  
  @Test
  public void testToString() {
    ArchiveEntry entry1 = new ArchiveEntry();
    entry1.setName("main.c");
    entry1.setDirectory(false);
    entry1.setSize(2000);
    String expected1 = "-    2000 main.c";
    assertEquals(expected1, ArchiveUtils.toString(entry1));
    
    ArchiveEntry entry2 = new ArchiveEntry();
    entry2.setName("testfiles");
    entry2.setDirectory(true);
    entry2.setSize(100);
    String expected2 = "d     100 testfiles";
    assertEquals(expected2, ArchiveUtils.toString(entry2));
  }
  
  @Test
  public void testMatchAsciiBuffer() {
    String expected = "abc";
    byte[] buffer = {97, 98, 99};
    assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    
  }
  
  @Test
  public void testToAsciiBytes() {
    String inputString = "abc";
    byte[] expected = {97, 98, 99};
    assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
  }
  
  @Test
  public void testToAsciiString() {
    byte[] inputBytes = {97, 98, 99};
    String expected = "abc";
    assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
  }
  
  @Test
  public void testIsEqual() {
    byte[] buffer1 = {97, 98, 99};
    byte[] buffer2 = {97, 98, 99};
    assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
  }
  
  @Test
  public void testIsEqualWithNull() {
    byte[] buffer1 = {97, 98, 99};
    byte[] buffer2 = {97, 98, 0, 0};
    assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 4));
  }
  
  @Test
  public void testIsArrayZero() {
    byte[] a = {0, 0, 0, 97, 98, 99};
    assertTrue(ArchiveUtils.isArrayZero(a, 3));
  }
  
  @Test
  public void testSanitize() {
    String s = "te\nst";
    String expected = "te?st";
    assertEquals(expected, ArchiveUtils.sanitize(s));
  }
  
}