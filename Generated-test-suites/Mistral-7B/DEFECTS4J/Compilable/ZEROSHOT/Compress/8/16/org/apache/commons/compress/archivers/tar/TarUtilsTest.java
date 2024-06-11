package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
 long expected = 017550L;
 long actual = TarUtils.parseOctal(buffer, 0, 5);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '8'};
 TarUtils.parseOctal(buffer, 0, 5);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', '0'};
 String expected = "abcde";
 String actual = TarUtils.parseName(buffer, 0, 5);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "abcdef";
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, 10);
 String actual = new String(buffer, 0, offset);
 String expected = "abcdef";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
 String actual = new String(buffer, 0, 10);
 String expected = "012345";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, 12);
 String actual = new String(buffer, 0, 12);
 String expected = "012345  ";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[11];
 TarUtils.formatLongOctalBytes(value, buffer, 0, 11);
 String actual = new String(buffer, 0, 11);
 String expected = "0123456770";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[13];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 13);
 String actual = new String(buffer, 0, 13);
 String expected = "0123456770  ";
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e'};
 long expected = 0x2a5;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}