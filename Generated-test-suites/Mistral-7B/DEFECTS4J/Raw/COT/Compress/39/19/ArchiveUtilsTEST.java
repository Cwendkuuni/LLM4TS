package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

/**
 * ArchiveUtils Test Cases
 */
public class ArchiveUtilsTest {

 @Test
 public void testToStringMethod() {
 ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
 archiveEntry.setSize(2000L);
 archiveEntry.setDirectory(false);
 String expectedOutput = "- 2000 main.c";
 assertEquals(expectedOutput, ArchiveUtils.toString(archiveEntry));
 }

 @Test
 public void testMatchAsciiBuffer() {
 String expected = "test";
 byte[] buffer = new byte[5];
 Arrays.fill(buffer, (byte) 't');
 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testMatchAsciiBufferWithUnsupportedEncodingException() {
 String expected = "test";
 byte[] buffer = new byte[5];
 Arrays.fill(buffer, (byte) 't');
 ArchiveUtils.matchAsciiBuffer("invalid_encoding", buffer, 0, buffer.length);
 }

 @Test
 public void testIsEqualMethod() {
 byte[] buffer1 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
 byte[] buffer2 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
 byte[] buffer3 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 'd'};
 assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
 assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
 }

 @Test
 public void testIsEqualMethodWithIgnoreTrailingNulls() {
 byte[] buffer1 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 0, (byte) 0, (byte) 0};
 byte[] buffer2 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 0, (byte) 0, (byte) 0};
 assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
 }

 @Test
 public void testIsEqualMethodWithDifferentLengths() {
 byte[] buffer1 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
 byte[] buffer2 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 'd'};
 assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIsEqualMethodWithNegativeLength() {
 byte[] buffer1 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
 byte[] buffer2 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
 ArchiveUtils.isEqual(buffer1, 5, 3, buffer2, 0, buffer2.length, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsEqualMethodWithNegativeLength2() {
 byte[] buffer1 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
 byte[] buffer2 = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
 ArchiveUtils.isEqual(buffer1, 0, buffer1.length+1, buffer2, 0, buffer2.length, false);
 }

 @Test
 public void testIsArrayZeroMethod() {
 byte[] array = new byte[5];
 assertTrue(ArchiveUtils.isArrayZero(array, 5));
 }

 @Test
 public void testIsArrayZeroMethodWithDifferentLength() {
 byte[] array = new byte[5];
 assertFalse(ArchiveUtils.isArrayZero(array, 10));
 }

 @Test
 public void testSanitizeMethod() {
 String input = "\u0001test\u0002\u007F";
 String expectedOutput = "test?";
 assertEquals(expectedOutput, ArchiveUtils.sanitize(input));
 }
}