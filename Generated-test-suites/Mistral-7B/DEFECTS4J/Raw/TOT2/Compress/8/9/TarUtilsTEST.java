import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal_ValidInput() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '0'};
 long expected = 0175540L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInput_InvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', 'x', '4', '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInput_MissingTrailingSpace() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length - 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInput_EmptyBuffer() {
 byte[] buffer = new byte[0];
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName_ValidInput() {
 byte[] buffer = new byte[]{0, 'a', 'b', 0, 'c', 'd'};
 String expected = "abcd";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseName_InvalidInput_MissingTrailingNull() {
 byte[] buffer = new byte[]{0, 'a', 'b'};
 TarUtils.parseName(buffer, 0, buffer.length);
 }

 @Test
 public void testFormatNameBytes_ValidInput() {
 String name = "abcdef";
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String result = new String(buffer, 0, offset);
 assertEquals("abcdef", result);
 }

 @Test
 public void testFormatNameBytes_ValidInput_Truncated() {
 String name = "abcdef";
 byte[] buffer = new byte[5];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String result = new String(buffer, 0, offset);
 assertEquals("abcde", result);
 }

 @Test
 public void testFormatUnsignedOctalString_ValidInput() {
 long value = 12345;
 byte[] buffer = new byte[8];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String result = new String(buffer, 0, buffer.length);
 assertEquals("012345", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalString_InvalidInput_ValueTooLarge() {
 long value = Long.MAX_VALUE + 1;
 byte[] buffer = new byte[8];
 try {
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 } catch (IllegalArgumentException e) {
 assertEquals("123456789012345678901234567890="+Long.toOctalString(value)+" will not fit in octal number buffer of length 8", e.getMessage());
 }
 }

 @Test
 public void testFormatOctalBytes_ValidInput() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String result = new String(buffer, 0, 1);
 assertEquals(" ", result);
 result = new String(buffer, 1, 8);
 assertEquals("012345", result);
 }

 @Test
 public void testFormatLongOctalBytes_ValidInput() {
 long value = 12345678901234567890L;
 byte[] buffer = new byte[20];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String result = new String(buffer, 0, 1);
 assertEquals(" ", result);
 result = new String(buffer, 1, 19);
 assertEquals("01234567754543210123456789012345678901234567890", result);
 }

 @Test
 public void testFormatCheckSumOctalBytes_ValidInput() {
 long value = 12345678901234567890L;
 byte[] buffer = new byte[22];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String result = new String(buffer, 0, 1);
 assertEquals(" ", result);
 result = new String(buffer, 1, 20);
 assertEquals("01234567754543210123456789012345678901234567890", result);
 result = new String(buffer, 21, 1);
 assertEquals(" ", result);
 }

 @Test
 public void testComputeCheckSum_ValidInput() {
 byte[] buffer = new byte[]{0, 1, 2};
 long expected = TarUtils.computeCheckSum(buffer);
 long actual = TarUtils.computeCheckSum(new byte[]{0, 1, 2});
 assertEquals(expected, actual);
 }
}