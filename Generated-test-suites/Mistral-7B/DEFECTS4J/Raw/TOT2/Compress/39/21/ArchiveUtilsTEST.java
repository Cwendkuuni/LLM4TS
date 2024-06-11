import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 // Test toString method
 @Test
 public void testToString() {
 ArchiveEntry entry = new ArchiveEntry("test.txt");
 assertEquals("- 0 test.txt\n", ArchiveUtils.toString(entry));
 }

 // Test matchAsciiBuffer method with regular input
 @Test
 public void testMatchAsciiBufferWithRegularInput() {
 String expected = "Hello World";
 byte[] buffer = toAsciiBytes(expected);
 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 // Test matchAsciiBuffer method with offset and length
 @Test
 public void testMatchAsciiBufferWithOffsetAndLength() {
 String expected = "Hello World";
 byte[] buffer = toAsciiBytes(expected);
 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 6, 6));
 }

 // Test matchAsciiBuffer method with wrong input
 @Test(expected = RuntimeException.class)
 public void testMatchAsciiBufferWithWrongInput() {
 String expected = "Hello World";
 byte[] buffer = new byte[10];
 ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 5);
 }

 // Test toString method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testToStringWithNullInput() {
 ArchiveEntry entry = null;
 try {
 ArchiveUtils.toString(entry);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test isEqual method with regular input
 @Test
 public void testIsEqualWithRegularInput() {
 byte[] buffer1 = toAsciiBytes("Hello World".getBytes());
 byte[] buffer2 = toAsciiBytes("Hello World".getBytes());
 assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
 }

 // Test isEqual method with offset and length
 @Test
 public void testIsEqualWithOffsetAndLength() {
 byte[] buffer1 = toAsciiBytes("Hello World".getBytes());
 byte[] buffer2 = new byte[10];
 System.arraycopy(buffer1, 0, buffer2, 0, buffer1.length);
 assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));
 }

 // Test isEqual method with different lengths
 @Test
 public void testIsEqualWithDifferentLengths() {
 byte[] buffer1 = toAsciiBytes("Hello World".getBytes());
 byte[] buffer2 = Arrays.copyOfRange(buffer1, 0, buffer1.length-1);
 assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));
 }

 // Test isEqual method with ignoreTrailingNulls true
 @Test
 public void testIsEqualWithIgnoreTrailingNulls() {
 byte[] buffer1 = toAsciiBytes("Hello World\0".getBytes());
 byte[] buffer2 = toAsciiBytes("Hello World".getBytes());
 assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length));
 }

 // Test isEqual method with ignoreTrailingNulls false
 @Test
 public void testIsEqualWithIgnoreTrailingNullsFalse() {
 byte[] buffer1 = toAsciiBytes("Hello World\0".getBytes());
 byte[] buffer2 = toAsciiBytes("Hello World".getBytes());
 assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));
 }

 // Test isArrayZero method
 @Test
 public void testIsArrayZero() {
 byte[] array = new byte[10];
 assertTrue(ArchiveUtils.isArrayZero(array, 5));
 }

 // Test sanitize method with regular input
 @Test
 public void testSanitizeWithRegularInput() {
 String s = "Hello World!";
 String sanitized = ArchiveUtils.sanitize(s);
 assertEquals("Hello ???????????\n", sanitized);
 }

 // Test sanitize method with empty input
 @Test
 public void testSanitizeWithEmptyInput() {
 String s = "";
 String sanitized = ArchiveUtils.sanitize(s);
 assertEquals("", sanitized);
 }

 // Test sanitize method with null input
 @Test
 public void testSanitizeWithNullInput() {
 String s = null;
 try {
 String sanitized = ArchiveUtils.sanitize(s);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

}