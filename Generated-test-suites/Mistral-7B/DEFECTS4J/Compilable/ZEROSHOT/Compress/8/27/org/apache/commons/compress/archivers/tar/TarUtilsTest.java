package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '0'};
 long expected = 0175500L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', 'x', '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x'};
 String expected = "tar.txt";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "test.tar";
 byte[] expected = new byte[]{'t', 'e', 's', 't', '.', 't', 'a', 'r'};
 byte[] buffer = new byte[expected.length];
 int offset = 0;
 int length = expected.length;
 TarUtils.formatNameBytes(name, buffer, offset, length);
 assertArrayEquals(expected, buffer);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[8];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0'};
 assertArrayEquals(expected, buffer);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', ' ', ' ', '0'};
 assertArrayEquals(expected, buffer);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[12];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
 assertArrayEquals(expected, buffer);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[12];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '};
 assertArrayEquals(expected, buffer);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x'};
 long expected = TarUtils.computeCheckSum(buffer);
 long actual = 221563760L;
 assertEquals(expected, actual);
 }
}