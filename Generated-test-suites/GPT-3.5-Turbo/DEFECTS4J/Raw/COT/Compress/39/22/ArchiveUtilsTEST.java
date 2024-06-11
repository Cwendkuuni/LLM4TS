
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {
  
  @Test
  public void testToString() {
    ArchiveEntry entry = new ArchiveEntry();
    entry.setName("main.c");
    entry.setSize(2000);
    entry.setDirectory(false);
    String expected = "-    2000 main.c";
    Assert.assertEquals(expected, ArchiveUtils.toString(entry));
    
    entry.setName("testfiles");
    entry.setSize(100);
    entry.setDirectory(true);
    expected = "d     100 testfiles";
    Assert.assertEquals(expected, ArchiveUtils.toString(entry));
  }
  
  @Test
  public void testMatchAsciiBuffer() {
    String expected = "Hello World";
    byte[] buffer = expected.getBytes();
    Assert.assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    
    expected = "Hello";
    buffer = expected.getBytes();
    Assert.assertFalse(ArchiveUtils.matchAsciiBuffer("Goodbye", buffer));
  }
  
  @Test
  public void testToAsciiBytes() {
    String inputString = "Hello World";
    byte[] expected = inputString.getBytes();
    byte[] result = ArchiveUtils.toAsciiBytes(inputString);
    Assert.assertArrayEquals(expected, result);
  }
  
  @Test
  public void testToAsciiString() {
    byte[] inputBytes = "Hello World".getBytes();
    String expected = "Hello World";
    String result = ArchiveUtils.toAsciiString(inputBytes);
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testToAsciiStringWithOffsetAndLength() {
    byte[] inputBytes = "Hello World".getBytes();
    int offset = 6;
    int length = 5;
    String expected = "World";
    String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testIsEqual() {
    byte[] buffer1 = "Hello World".getBytes();
    byte[] buffer2 = "Hello World".getBytes();
    Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    
    buffer2 = "Goodbye World".getBytes();
    Assert.assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
  }
  
  @Test
  public void testIsEqualWithIgnoreTrailingNulls() {
    byte[] buffer1 = "Hello\0\0\0".getBytes();
    byte[] buffer2 = "Hello".getBytes();
    Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    
    buffer2 = "Goodbye".getBytes();
    Assert.assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
  }
  
  @Test
  public void testIsEqualWithOffsetsAndLengths() {
    byte[] buffer1 = "Hello World".getBytes();
    byte[] buffer2 = "World".getBytes();
    int offset1 = 6;
    int offset2 = 0;
    int length1 = 5;
    int length2 = 5;
    Assert.assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
    
    buffer2 = "Goodbye".getBytes();
    Assert.assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
  }
  
  @Test
  public void testIsEqualWithNulls() {
    byte[] buffer1 = "Hello World".getBytes();
    byte[] buffer2 = "Hello World\0\0\0".getBytes();
    Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    
    buffer2 = "Goodbye World\0\0\0".getBytes();
    Assert.assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
  }
  
  @Test
  public void testIsArrayZero() {
    byte[] array = new byte[] {0, 0, 0, 0, 0, 0};
    int size = 6;
    Assert.assertTrue(ArchiveUtils.isArrayZero(array, size));
    
    array = new byte[] {0, 0, 1, 0, 0, 0};
    Assert.assertFalse(ArchiveUtils.isArrayZero(array, size));
  }
  
  @Test
  public void testSanitize() {
    String input = "Hello\tWorld\n";
    String expected = "Hello?World?";
    String result = ArchiveUtils.sanitize(input);
    Assert.assertEquals(expected, result);
    
    input = "Sanitized";
    Assert.assertEquals(input, ArchiveUtils.sanitize(input));
  }
  
}
