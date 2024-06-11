import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '5', '0'};
 long expected = 017557755L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '8', '7', '5', '0'}; // invalid byte '8'
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalMissingTrailingSpaceOrNull() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '5'}; // missing trailing space or null
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 // Test for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{0x0, 'a', 'b', 'c', 0x0};
 String expected = "abc";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 // Test for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "testName";
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = name.length();
 int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
 String result = new String(buffer, 0, actual);
 String expected = name;
 assertEquals(expected, result);
 }

 // Test for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567890L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 9;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 String expected = "012345667 \0";
 String result = new String(buffer, 0, offset + length);
 assertEquals(expected, result);
 }

 // Test for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 10;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 String expected = "0123456\0 \0";
 String result = new String(buffer, 0, offset + length);
 assertEquals(expected, result);
 }

 // Test for formatCheckSumOctalBytes method
 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 11;
 TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
 String expected = "0123456\00 \0";
 String result = new String(buffer, 0, offset + length);
 assertEquals(expected, result);
 }

 // Test for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
 long expected = 21152851123L;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}