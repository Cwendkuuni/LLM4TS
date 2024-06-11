import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '0', '0', '1', '1', '2', '0', '7', ' ', '0'};
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(0121107L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '0', '0', '1', '1', '2', '0', '8'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', 0};
 String name = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals("abc", name);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "hello";
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals(10, offset);
 for (int i = 0; i < buffer.length; i++) {
 assertEquals(name.charAt(i), (char) buffer[i]);
 }
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 123;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 for (int i = 0; i < buffer.length; i++) {
 assertEquals('0', buffer[i]);
 }
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 123;
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertEquals(8, offset);
 for (int i = 0; i < buffer.length; i++) {
 if (i < 7) {
 assertEquals('0' + (i % 8 + 1), buffer[i]);
 } else {
 assertEquals(' ', buffer[i]);
 }
 }
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 assertEquals(9, offset);
 for (int i = 0; i < buffer.length; i++) {
 if (i < 8) {
 assertEquals('0' + (i % 8 + 1), buffer[i]);
 } else {
 assertEquals(' ', buffer[i]);
 }
 }
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 assertEquals(10, offset);
 for (int i = 0; i < buffer.length; i++) {
 if (i < 8) {
 assertEquals('0' + (i % 8 + 1), buffer[i]);
 } else if (i == 8) {
 assertEquals(' ', buffer[i]);
 } else {
 assertEquals('0', buffer[i]);
 }
 }
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'a', 'b', 'c', 'd'};
 long checksum = TarUtils.computeChecksum(buffer);
 long expected = (0x61 + 0x62 + 0x63 + 0x64) & 0xFFFFFFFFL;
 assertEquals(expected, checksum);
 }
}