package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '7', '7', '5', '0', ' '};
 long expected = 01757540L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '7', '7', '5', 'x', ' '};
 assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
 }

 @Test
 public void testParseOctalMissingTrailingSpace() {
 byte[] buffer = new byte[]{'0', '7', '5', '7', '7', '5'};
 assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x', 0};
 String expected = "tar.txt";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "test.tar.gz";
 byte[] buffer = new byte[name.length() + 1];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'t', 'e', 's', 't', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
 for (int i = 0; i < name.length(); ++i) {
 assertEquals((byte) name.charAt(i), expected[i]);
 }
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '5', '5', '5', '5'};
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345L;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '5', '5', '5', ' ', ' ', '0'};
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 12345L;
 byte[] buffer = new byte[11];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '5', '5', '5', ' ', '0'};
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 12345L;
 byte[] buffer = new byte[13];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '5', '5', '5', '5', ' ', ' ', '0'};
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x', 't', 0x0, 0x2, 0x0, 0x14, 0};
 long expected = 0x53d6537dL;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}