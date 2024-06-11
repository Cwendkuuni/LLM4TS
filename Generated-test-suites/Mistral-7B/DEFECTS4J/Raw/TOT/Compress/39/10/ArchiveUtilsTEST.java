package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToString() {
 ArchiveEntry entry = new ArchiveEntry("testfile.txt");
 String result = ArchiveUtils.toString(entry);
 String expected = "-\t0\ttestfile.txt";
 assertEquals(expected, result);
 }

 @Test
 public void testMatchAsciiBuffer() {
 byte[] buffer1 = toAsciiBytes("teststring");
 byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 's', 't', 'r', 'i', 'n', 'g'};
 assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));
 assertTrue(ArchiveUtils.matchAsciiBuffer("teststring", buffer1));
 }

 @Test
 public void testMatchAsciiBuffer_InvalidEncoding() {
 String expected = "not a valid ASCII string";
 byte[] buffer = toAsciiBytes(expected); // Throws UnsupportedEncodingException
 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 @Test
 public void testMatchAsciiBuffer_MismatchedLength() {
 byte[] buffer1 = toAsciiBytes("teststring");
 byte[] buffer2 = new byte[buffer1.length + 1];
 Arrays.fill(buffer2, (byte) 'x');
 assertFalse(ArchiveUtils.matchAsciiBuffer("teststring", buffer2));
 }

 @Test
 public void testToString_InvalidArchiveEntry() {
 assertNull(ArchiveUtils.toString(null));
 }

 @Test
 public void testIsEqual() {
 byte[] buffer1 = toAsciiBytes("teststring1");
 byte[] buffer2 = toAsciiBytes("teststring2");
 assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));

 byte[] buffer3 = new byte[buffer1.length];
 System.arraycopy(buffer1, 0, buffer3, 0, buffer1.length);
 byte[] buffer4 = new byte[buffer1.length];
 System.arraycopy(buffer1, 0, buffer4, 0, buffer1.length);
 buffer4[buffer1.length - 1] = (byte) 0;
 assertTrue(ArchiveUtils.isEqualWithNull(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length));
 }

 @Test
 public void testIsEqual_InvalidBuffer1() {
 byte[] buffer1 = null;
 byte[] buffer2 = toAsciiBytes("teststring");
 assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, 0, 0, buffer2.length, false));
 }

 @Test
 public void testIsEqual_InvalidBuffer2() {
 byte[] buffer1 = toAsciiBytes("teststring");
 byte[] buffer2 = null;
 assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, 0, false));
 }

 @Test
 public void testIsEqual_InvalidLength() {
 byte[] buffer1 = toAsciiBytes("teststring");
 byte[] buffer2 = new byte[buffer1.length + 1];
 Arrays.fill(buffer2, (byte) 'x');
 assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));
 }

 @Test
 public void testIsEqual_IgnoreTrailingNulls() {
 byte[] buffer1 = toAsciiBytes("teststring1");
 byte[] buffer2 = toAsciiBytes("teststring2");
 byte[] buffer3 = toAsciiBytes("teststring1\0");
 byte[] buffer4 = toAsciiBytes("teststring2\0");
 assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
 assertTrue(ArchiveUtils.isEqualWithNull(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length));
 }

 @Test
 public void testIsEqual_IgnoreTrailingNulls_MismatchedBuffers() {
 byte[] buffer1 = toAsciiBytes("teststring1");
 byte[] buffer2 = toAsciiBytes("teststring2");
 assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
 }

 @Test
 public void testSanitize() {
 String input = "test\u0001string with ? (question mark) and \u001A control character.";
 String expected = "test??string with ?? (question mark) and ?? (control character)";
 String result = ArchiveUtils.sanitize(input);
 assertEquals(expected, result);
 }
}