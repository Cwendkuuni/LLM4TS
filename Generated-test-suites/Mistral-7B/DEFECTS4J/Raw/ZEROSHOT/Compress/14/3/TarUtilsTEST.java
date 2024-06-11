package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', ' '};
 long expected = 01755; // octal number
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', 'A'}; // invalid byte 'A'
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalMissingTrailer() {
 byte[] buffer = new byte[]{'0', '7', '5', '5'}; // missing trailing space or NUL
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseOctalAllNUL() {
 byte[] buffer = new byte[]{'0'}; // all NULs
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(0L, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalLeadingNUL() {
 byte[] buffer = new byte[]{'0'}; // leading NUL
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseOctalLong() {
 byte[] buffer = new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
 long expected = 0177777777777777L; // octal number
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseOctalOrBinaryLeadingZero() {
 byte[] buffer = new byte[]{'0', '0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
 long expected = 0017777777777777L; // octal number with leading zeroes
 long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseOctalOrBinaryBinaryNumber() {
 byte[] buffer = new byte[]{0x80, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
 long expected = 0x01020304050607L; // binary number
 long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalOrBinaryInvalidByte() {
 byte[] buffer = new byte[]{0x80, 0x00, 0x01, 0x02, 0x03, 0xFF, 0x05, 0x06, 0x07}; // invalid byte 'FF'
 TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalOrBinaryInvalidLength() {
 byte[] buffer = new byte[]{0x80, 0x00, 0x01}; // invalid length 3
 TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 }

 @Test
 public void testParseBooleanTrue() {
 byte[] buffer = new byte[]{1};
 boolean expected = true;
 boolean actual = TarUtils.parseBoolean(buffer, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBooleanFalse() {
 byte[] buffer = new byte[]{0};
 boolean expected = false;
 boolean actual = TarUtils.parseBoolean(buffer, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBooleanInvalidByte() {
 byte[] buffer = new byte[]{2}; // invalid byte '2'
 TarUtils.parseBoolean(buffer, 0);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{0, 0, 'T', 'a', 'r', ' ', 'T', 'e', 's', 't', '.', 't', 'a', 'r', '.', 'g', 'z'};
 String expected = "Tar Test.tar.gz";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "Tar Test.tar.gz";
 byte[] buffer = new byte[32];
 int expected = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 int actual = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytesTruncated() {
 String name = "Tar Test.tar.gz";
 byte[] buffer = new byte[16];
 int expected = 16;
 int actual = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[11];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "012345";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[13];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345 \0";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[13];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345789012345 \0";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[13];
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
 String expected = "012345789012345";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatLongOctalOrBinaryBytesBinaryNumber() {
 long value = 0x0123456789012345L;
 byte[] buffer = new byte[13];
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
 String expected = "0123456789012345";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{0, 1, 2, 3, 4};
 long expected = (0x00 + BYTE_MASK & 0xFF) +
         ((0x01 + BYTE_MASK & 0xFF) << 8) +
         ((0x02 + BYTE_MASK & 0xFF) << 16) +
         ((0x03 + BYTE_MASK & 0xFF) << 24) +
         ((0x04 + BYTE_MASK & 0xFF) << 32);
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[6];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345 \0";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }
}