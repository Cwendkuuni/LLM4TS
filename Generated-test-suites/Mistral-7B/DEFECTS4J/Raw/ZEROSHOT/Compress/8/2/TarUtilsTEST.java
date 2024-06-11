import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7', '5', '5', '7'};
 int offset = 0;
 int length = buffer.length;
 long expected = 561445656614556L; // hex value: 0x123456789abcdef0
 long actual = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'.','.','.','/','d','i','r','/','t','e','s','t','/'};
 String expected = "dir/test/";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "dir/test/";
 byte[] expected = new byte[]{'d','i','r','/','t','e','s','t','/'};
 byte[] buffer = new byte[expected.length];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, expected.length);
 assertEquals(expected.length, offset);
 for (int i = 0; i < expected.length; ++i) {
 assertEquals((byte) expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567890L;
 byte[] buffer = new byte[14];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "0123455576441";
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals((byte) expected.charAt(i), buffer[i]);
 }
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[16];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "0123455576441  ";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[17];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345675512345  ";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[18];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345675512345  \0";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
 long expected = 37862113324121L;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}