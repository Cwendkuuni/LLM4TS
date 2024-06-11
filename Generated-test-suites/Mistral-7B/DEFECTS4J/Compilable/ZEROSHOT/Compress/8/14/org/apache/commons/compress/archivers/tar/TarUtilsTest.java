package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = 01234567543L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', 'Q', '8', '9', '0'}; // Q is an invalid byte
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 fail("Exception expected but not thrown");
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', ' ', 'T', 'e', 's', 't', '.', 'b', 'a', 's', 'e', ' ', '0', '1', '2', '3'};
 String expected = "tar entry test.base 012";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "tar entry test.base 012";
 byte[] buffer = new byte[20];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String actual = new String(buffer, 0, length);
 assertEquals(name, actual);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 01234567543L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "01234567543";
 StringBuilder actual = new StringBuilder();
 for (int i = 0; i < buffer.length; ++i) {
 actual.append((char) (buffer[i] & 0xFF));
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 01234567543L;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "01234567543 ";
 StringBuilder actual = new StringBuilder();
 for (int i = 0; i < buffer.length; ++i) {
 actual.append((char) (buffer[i] & 0xFF));
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 01234567543L;
 byte[] buffer = new byte[11];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String expected = "01234567543";
 StringBuilder actual = new StringBuilder();
 for (int i = 0; i < buffer.length; ++i) {
 actual.append((char) (buffer[i] & 0xFF));
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 01234567543L;
 byte[] buffer = new byte[12];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String expected = "01234567543  ";
 StringBuilder actual = new StringBuilder();
 for (int i = 0; i < buffer.length; ++i) {
 actual.append((char) (buffer[i] & 0xFF));
 }
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', ' ', 'T', 'e', 's', 't', '.', 'b', 'a', 's', 'e', ' ', '0', '1', '2', '3'};
 long expected = 1729938073;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}