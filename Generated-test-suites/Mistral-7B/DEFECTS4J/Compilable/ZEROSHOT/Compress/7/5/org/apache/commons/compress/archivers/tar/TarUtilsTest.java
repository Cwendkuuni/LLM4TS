package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '4'};
 long expected = 01754;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalWithInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', 'X'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'E', 'n', 't', 'r', 'y', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
 String expected = "tar Entry.txt";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "testName.ext";
 byte[] buffer = new byte[15];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'t', 'e', 's', 't', 'N', 'a', 'm', 'e', '.', 'e', 'x', 't', 0, 0, 0, 0};
 for (int i = 0; i < length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '5', '5', '5', '5'};
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '5', '5', '5', '5', ' ', '0'};
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 123456789;
 byte[] buffer = new byte[9];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 123456789;
 byte[] buffer = new byte[12];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '8', ' ', '0'};
 for (int i = 0; i < buffer.length; ++i) {
 assertEquals(expected[i], buffer[i]);
 }
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'E', 'n', 't', 'r', 'y', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
 long expected = TarUtils.computeCheckSum(buffer);
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }

}