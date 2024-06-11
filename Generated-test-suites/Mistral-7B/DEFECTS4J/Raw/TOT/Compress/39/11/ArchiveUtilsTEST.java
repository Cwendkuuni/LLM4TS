package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

 @Test
 public void testToStringMethod(){
 ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
 String toStringResult = ArchiveUtils.toString(archiveEntry);
 String expectedResult = "- 0 test.txt";
 assertEquals(expectedResult, toStringResult);

 ArchiveEntry directoryEntry = new ArchiveEntry("testdir/");
 String directoryToStringResult = ArchiveUtils.toString(directoryEntry);
 String expectedDirectoryResult = "d 0 testdir/";
 assertEquals(expectedDirectoryResult, directoryToStringResult);
 }

 @Test
 public void testMatchAsciiBufferMethodWithStringAndByteArray(){
 String expected = "test";
 byte[] buffer = toAsciiBytes(expected);
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 assertTrue(result);
 }

 @Test
 public void testMatchAsciiBufferMethodWithStringAndInvalidByteArray(){
 String expected = "test";
 byte[] buffer = new byte[5];
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 assertFalse(result);
 }

 @Test
 public void testMatchAsciiBufferMethodWithByteArrayAndString(){
 String expected = "test";
 byte[] buffer = toAsciiBytes(expected);
 boolean result = ArchiveUtils.matchAsciiBuffer(buffer, expected);
 assertTrue(result);
 }

 @Test
 public void testMatchAsciiBufferMethodWithByteArrayAndInvalidString(){
 byte[] buffer = toAsciiBytes("test");
 boolean result = ArchiveUtils.matchAsciiBuffer(buffer, "invalid");
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodWithValidByteArrays(){
 byte[] buffer1 = toAsciiBytes("test1");
 byte[] buffer2 = toAsciiBytes("test1");
 boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
 assertTrue(result);
 }

 @Test
 public void testIsEqualMethodWithInvalidByteArrays(){
 byte[] buffer1 = toAsciiBytes("test1");
 byte[] buffer2 = toAsciiBytes("test2");
 boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
 assertFalse(result);
 }

 @Test
 public void testIsEqualMethodWithValidByteArraysAndIgnoreTrailingNulls(){
 byte[] buffer1 = toAsciiBytes("test1");
 byte[] buffer2 = toAsciiBytes("test1\0");
 boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
 assertTrue(result);
 }

 @Test
 public void testIsEqualMethodWithInvalidByteArraysAndIgnoreTrailingNulls(){
 byte[] buffer1 = toAsciiBytes("test1");
 byte[] buffer2 = toAsciiBytes("test2");
 boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
 assertFalse(result);
 }

 @Test(expected = RuntimeException.class)
 public void testToStringMethodWithNullEntry(){
 ArchiveEntry archiveEntry = null;
 String toStringResult = ArchiveUtils.toString(archiveEntry);
 fail("ToString method should throw an exception with a null ArchiveEntry.");
 }

 @Test(expected = RuntimeException.class)
 public void testMatchAsciiBufferMethodWithNullExpectedString(){
 String expected = null;
 byte[] buffer = toAsciiBytes("test");
 boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
 fail("MatchAsciiBuffer method should throw an exception with a null expected string.");
 }

 @Test(expected = RuntimeException.class)
 public void testMatchAsciiBufferMethodWithInvalidByteArray(){
 byte[] buffer = new byte[5];
 boolean result = ArchiveUtils.matchAsciiBuffer("test", buffer);
 fail("MatchAsciiBuffer method should throw an exception with an invalid byte array.");
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testToAsciiBytesMethodWithInvalidEncoding(){
 String inputString = "test";
 try {
 ArchiveUtils.toAsciiBytes(inputString);
 fail("ToAsciiBytes method should throw an exception with an invalid encoding.");
 } catch (UnsupportedEncodingException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSanitizeMethodWithNullString(){
 String inputString = null;
 String result = ArchiveUtils.sanitize(inputString);
 fail("Sanitize method should throw an exception with a null string.");
 }
}