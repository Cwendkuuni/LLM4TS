package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.archivers.test.TestUtils;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.ArchiveEntryUtils;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToStringMethod() {
 ArchiveEntry entry = TestUtils.createTestFileEntry("testdata/testfile.txt", "testfile.txt", false, 1234L);
 String expectedOutput = "- 1234 testfile.txt";
 String actualOutput = ArchiveUtils.toString(entry);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testMatchAsciiBufferMethodValid() {
 String expected = "teststring";
 byte[] buffer = ArchiveUtils.toAsciiBytes(expected);
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 assertTrue(result);
 }

 @Test
 public void testMatchAsciiBufferMethodInvalid() {
 String expected = "teststring";
 byte[] buffer = new byte[5];
 Arrays.fill(buffer, (byte) 65); // ASCII 'A'
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodValid() {
 byte[] buffer1 = ArchiveUtils.toAsciiBytes("teststring");
 byte[] buffer2 = ArchiveUtils.toAsciiBytes("teststring");
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
 assertTrue(result);
 }

 @Test
 public void testIsEqualMethodInvalid() {
 byte[] buffer1 = ArchiveUtils.toAsciiBytes("teststring");
 byte[] buffer2 = ArchiveUtils.toAsciiBytes("testdifferentstring");
 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
 assertFalse(result);
 }

 @Test
 public void testIsEqualWithNullMethodValid() {
 byte[] buffer1 = ArchiveUtils.toAsciiBytes("teststring");
 byte[] buffer2 = ArchiveUtils.toAsciiBytes("teststring");
 byte[] buffer3 = ArchiveUtils.toAsciiBytes("teststring");
 byte[] buffer4 = ArchiveUtils.toAsciiBytes("teststring1234");
 boolean resultWithNulls = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(resultWithNulls);
 resultWithNulls = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
 assertTrue(resultWithNulls);
 resultWithNulls = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length);
 assertFalse(resultWithNulls);
 }

 @Test
 public void testIsEqualWithNullMethodInvalid() {
 byte[] buffer1 = ArchiveUtils.toAsciiBytes("teststring");
 byte[] buffer2 = ArchiveUtils.toAsciiBytes("teststring");
 byte[] buffer3 = ArchiveUtils.toAsciiBytes("teststring");
 byte[] buffer4 = ArchiveUtils.toAsciiBytes("teststring1234");
 byte[] buffer5 = new byte[5];
 Arrays.fill(buffer5, (byte) 65); // ASCII 'A'
 boolean resultWithNulls = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 assertTrue(resultWithNulls);
 resultWithNulls = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
 assertTrue(resultWithNulls);
 resultWithNulls = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length);
 assertFalse(resultWithNulls);
 }

 @Test
 public void testArrayZeroMethod() {
 byte[] arrayZero = new byte[5];
 Arrays.fill(arrayZero, (byte) 0);
 assertTrue(ArchiveUtils.isArrayZero(arrayZero, 5));
 }

 @Test
 public void testSanitizeMethod() {
 String input = "corrupted_testfile_name_with_special_characters";
 String expected = "corrupted_testfile_name_with_qu?stion_mark_characters";
 String result = ArchiveUtils.sanitize(input);
 assertEquals(expected, result);
 }

 @Test(expected = RuntimeException.class)
 public void testMatchAsciiBufferMethodWithUnsupportedEncodingException() {
 String expected = "teststring";
 byte[] buffer = new byte[5];
 Arrays.fill(buffer, (byte) 128); // not ASCII
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 }

 @Test(expected = RuntimeException.class)
 public void testToAsciiBytesMethodWithUnsupportedEncodingException() {
 String input = "teststring";
 byte[] result = ArchiveUtils.toAsciiBytes("teststring_with_unicode"); // not ASCII
 }
}