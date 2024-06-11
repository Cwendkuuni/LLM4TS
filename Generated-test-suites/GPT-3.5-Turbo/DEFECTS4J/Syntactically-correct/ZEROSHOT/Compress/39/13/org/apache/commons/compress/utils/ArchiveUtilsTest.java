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
      
      String actual = ArchiveUtils.toString(entry);
      String expected = "-    2000 main.c";
      
      assertEquals(expected, actual);
   }
   
   @Test
   public void testMatchAsciiBuffer1() {
      String expected = "Hello";
      byte[] buffer = new byte[5];
      buffer[0] = 72;
      buffer[1] = 101;
      buffer[2] = 108;
      buffer[3] = 108;
      buffer[4] = 111;
      
      boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
      
      assertTrue(actual);
   }
   
   @Test
   public void testMatchAsciiBuffer2() {
      String expected = "World";
      byte[] buffer = new byte[10];
      buffer[0] = 87;
      buffer[1] = 111;
      buffer[2] = 114;
      buffer[3] = 108;
      buffer[4] = 100;
      
      boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 5);
      
      assertTrue(actual);
   }
   
   @Test
   public void testToAsciiBytes() {
      String inputString = "Test String";
      byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
      byte[] expected = new byte[inputString.length()];
      for (int i = 0; i < inputString.length(); i++) {
         expected[i] = (byte) inputString.charAt(i);
      }
      
      assertArrayEquals(expected, actual);
   }
   
   @Test
   public void testToAsciiString1() {
      byte[] inputBytes = {72, 101, 108, 108, 111};
      String actual = ArchiveUtils.toAsciiString(inputBytes);
      String expected = "Hello";
      
      assertEquals(expected, actual);
   }
   
   @Test
   public void testToAsciiString2() {
      byte[] inputBytes = {87, 111, 114, 108, 100};
      String actual = ArchiveUtils.toAsciiString(inputBytes, 0, 5);
      String expected = "World";
      
      assertEquals(expected, actual);
   }
   
   @Test
   public void testIsEqual1() {
      byte[] buffer1 = {72, 101, 108, 108, 111};
      byte[] buffer2 = {72, 101, 108, 108, 111};
      
      boolean actual = ArchiveUtils.isEqual(buffer1, buffer2);
      
      assertTrue(actual);
   }
   
   @Test
   public void testIsEqual2() {
      byte[] buffer1 = {87, 111, 114, 108, 100};
      byte[] buffer2 = {87, 111, 114, 108, 100};
      
      boolean actual = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5);
      
      assertTrue(actual);
   }
   
   @Test
   public void testIsEqual3() {
      byte[] buffer1 = {72, 101, 108, 108, 111};
      byte[] buffer2 = {72, 101, 108, 108, 111, 33};
      
      boolean actual = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 6);
      
      assertFalse(actual);
   }
   
   @Test
   public void testIsEqual4() {
      byte[] buffer1 = {72, 101, 108, 108, 111, 33};
      byte[] buffer2 = {72, 101, 108, 108, 111};
      
      boolean actual = ArchiveUtils.isEqual(buffer1, 0, 6, buffer2, 0, 5);
      
      assertFalse(actual);
   }
   
   @Test
   public void testIsEqual5() {
      byte[] buffer1 = {72, 101, 108, 108, 111, 0};
      byte[] buffer2 = {72, 101, 108, 108, 111};
      
      boolean actual = ArchiveUtils.isEqual(buffer1, 0, 6, buffer2, 0, 5, true);
      
      assertTrue(actual);
   }
   
   @Test
   public void testIsEqual6() {
      byte[] buffer1 = {72, 101, 108, 108, 111};
      byte[] buffer2 = {72, 101, 108, 108, 111, 0};
      
      boolean actual = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 6, true);
      
      assertTrue(actual);
   }
   
   @Test
   public void testIsEqualWithNull() {
      byte[] buffer1 = {72, 101, 108, 108, 111};
      byte[] buffer2 = {72, 101, 108, 108, 111, 0};
      
      boolean actual = ArchiveUtils.isEqualWithNull(buffer1, 0, 5, buffer2, 0, 6);
      
      assertFalse(actual);
   }
   
   @Test
   public void testIsArrayZero1() {
      byte[] a = {0, 0, 0, 0, 0};
      int size = 5;
      
      boolean actual = ArchiveUtils.isArrayZero(a, size);
      
      assertTrue(actual);
   }
   
   @Test
   public void testIsArrayZero2() {
      byte[] a = {0, 0, 0, 1, 0};
      int size = 5;
      
      boolean actual = ArchiveUtils.isArrayZero(a, size);
      
      assertFalse(actual);
   }
   
   @Test
   public void testSanitize() {
      String s = "Hello\r\nWorld";
      
      String actual = ArchiveUtils.sanitize(s);
      String expected = "Hello?World";
      
      assertEquals(expected, actual);
   }
}