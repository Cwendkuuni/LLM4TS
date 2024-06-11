package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '7', '5', '0', '\u0000'};
 long expected = 0177500L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalid() {
 byte[] buffer = new byte[]{'0', '8', '0', '\u0000'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'a', 'b', 'c', '\u0000'};
 String expected = "abc";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "testname";
 byte[] buffer = new byte[10];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String actual = new String(buffer, 0, length);
 String expected = "testname";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "021013";
 String actual = new String(buffer, 0, 5)+"..."; // ... represents trailing zeros
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[10];
 int length = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "021013 ";
 String actual = new String(buffer, 0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 1234567890123L;
 byte[] buffer = new byte[11];
 int length = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String expected = "0123457170123 ";
 String actual = new String(buffer, 0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 1234567890123L;
 byte[] buffer = new byte[6];
 int length = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345 ";
 String actual = new String(buffer, 0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{1, 2, 3, 4, 5};
 long expected = 297;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}