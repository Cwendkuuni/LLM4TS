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
      
      String result = ArchiveUtils.toString(entry);
      
      assertEquals("-    2000 main.c", result);
   }
   
   @Test
   public void testToString_directory() {
      ArchiveEntry entry = new ArchiveEntry();
      entry.setDirectory(true);
      entry.setSize(100);
      entry.setName("testfiles");
      
      String result = ArchiveUtils.toString(entry);
      
      assertEquals("d     100 testfiles", result);
   }
   
   @Test
   public void testMatchAsciiBuffer() {
      String expected = "test";
      byte[] buffer = "test".getBytes();
      
      boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
      
      assertTrue(result);
   }
   
   @Test
   public void testMatchAsciiBuffer_offset() {
      String expected = "est";
      byte[] buffer = "test".getBytes();
      
      boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 1, 3);
      
      assertTrue(result);
   }
   
   @Test
   public void testToAsciiBytes() {
      String inputString = "test";
      
      byte[] result = ArchiveUtils.toAsciiBytes(inputString);
      
      assertArrayEquals(new byte[] { 116, 101, 115, 116 }, result);
   }
   
   @Test
   public void testToAsciiString() {
      byte[] inputBytes = new byte[] { 116, 101, 115, 116 };
      
      String result = ArchiveUtils.toAsciiString(inputBytes);
      
      assertEquals("test", result);
   }
   
   @Test
   public void testToAsciiString_offsetLength() {
      byte[] inputBytes = new byte[] { 116, 101, 115, 116 };
      
      String result = ArchiveUtils.toAsciiString(inputBytes, 1, 3);
      
      assertEquals("est", result);
   }
   
   @Test
   public void testIsEqual() {
      byte[] buffer1 = "test".getBytes();
      byte[] buffer2 = "test".getBytes();
      
      boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
      
      assertTrue(result);
   }
   
   @Test
   public void testIsEqual_offsetLength() {
      byte[] buffer1 = "test".getBytes();
      byte[] buffer2 = "test".getBytes();
      
      boolean result = ArchiveUtils.isEqual(buffer1, 1, 3, buffer2, 1, 3);
      
      assertTrue(result);
   }
   
   @Test
   public void testIsEqualWithNull() {
      byte[] buffer1 = "test".getBytes();
      byte[] buffer2 = "test".getBytes();
      
      boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 4);
      
      assertTrue(result);
   }
   
   @Test
   public void testIsArrayZero() {
      byte[] a = new byte[] { 0, 0, 0, 0 };
      int size = 4;
      
      boolean result = ArchiveUtils.isArrayZero(a, size);
      
      assertTrue(result);
   }
   
   @Test
   public void testSanitize() {
      String input = "abc\u0000def";
      
      String result = ArchiveUtils.sanitize(input);
      
      assertEquals("abc?def", result);
   }
   
   @Test
   public void testSanitize_longString() {
      String input = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrst";
      
      String result = ArchiveUtils.sanitize(input);
      
      assertEquals(255, result.length());
   }
   
}