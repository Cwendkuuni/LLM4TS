import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToString() {
 ArchiveEntry entry = new ArchiveEntry("test.txt");
 String result = ArchiveUtils.toString(entry);
 String expected = "- 0 test.txt";
 assertEquals(expected, result);

 entry.setDirectory(true);
 result = ArchiveUtils.toString(entry);
 expected = "d 0 test.txt";
 assertEquals(expected, result);
 }

 @Test
 public void testMatchAsciiBuffer() {
 String expected = "Hello World";
 byte[] buffer1 = toAsciiBytes(expected);
 byte[] buffer2 = Arrays.copyOfRange(buffer1, 0, 5);
 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1, 0, buffer1.length));
 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2, 0, buffer2.length));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMatchAsciiBufferWithInvalidInput() {
 String expected = "Hello World";
 byte[] buffer1 = toAsciiBytes(expected);
 int length1 = buffer1.length + 1;
 byte[] buffer2 = new byte[length1];
 System.arraycopy(buffer1, 0, buffer2, 0, length1 - 1);
 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer1, 0, buffer1.length));
 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer2, 0, length1));
 }

 @Test
 public void testToAsciiString() {
 byte[] inputBytes = toAsciiBytes("Hello World");
 String result = ArchiveUtils.toAsciiString(inputBytes);
 String expected = "Hello World";
 assertEquals(expected, result);
 }

 @Test
 public void testToAsciiStringWithOffsetAndLength() {
 byte[] inputBytes = toAsciiBytes("Hello World");
 String result = ArchiveUtils.toAsciiString(inputBytes, 5, 6);
 String expected = "World";
 assertEquals(expected, result);
 }

 @Test
 public void testIsEqual() {
 byte[] buffer1 = toAsciiBytes("Hello World");
 byte[] buffer2 = Arrays.copyOf(buffer1, buffer1.length);
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
 assertTrue(result);
 }

 @Test
 public void testIsEqualWithNull() {
 byte[] buffer1 = toAsciiBytes("Hello World");
 byte[] buffer2 = Arrays.copyOf(buffer1, buffer1.length + 1);
 buffer2[buffer1.length] = (byte) 0;
 boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(result);
 }

 @Test
 public void testIsEqualWithDifferentLengths() {
 byte[] buffer1 = toAsciiBytes("Hello World");
 byte[] buffer2 = Arrays.copyOf(buffer1, buffer1.length - 1);
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
 assertFalse(result);
 }

 @Test
 public void testIsEqualWithTrailingNulls() {
 byte[] buffer1 = Arrays.copyOf(toAsciiBytes("Hello World"), toAsciiBytes("Hello World").length + 1);
 byte[] buffer2 = toAsciiBytes("Hello World");
 boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(result);
 }

 @Test
 public void testIsEqualWithTrailingNulls2() {
 byte[] buffer1 = toAsciiBytes("Hello World");
 byte[] buffer2 = Arrays.copyOf(buffer1, buffer1.length + 1);
 boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertFalse(result);
 }

 @Test
 public void testIsEqualWithDifferentContents() {
 byte[] buffer1 = toAsciiBytes("Hello World");
 byte[] buffer2 = toAsciiBytes("Hello Java");
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
 assertFalse(result);
 }

 @Test
 public void testIsArrayZero() {
 byte[] buffer = new byte[10];
 assertTrue(ArchiveUtils.isArrayZero(buffer, 5));
 }

 @Test
 public void testSanitize() {
 String result = ArchiveUtils.sanitize("test\\012file.txt");
 String expected = "test??file.txt";
 assertEquals(expected, result);
 }

}