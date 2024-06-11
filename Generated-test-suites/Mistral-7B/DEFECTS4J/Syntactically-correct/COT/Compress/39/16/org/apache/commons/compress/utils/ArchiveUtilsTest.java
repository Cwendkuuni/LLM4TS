package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CharsetNames;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

 // ---------------------------------------------------------------------- //
 // toString test cases                                                     //
 // ---------------------------------------------------------------------- //

 @org.junit.Test
 public void testToString() {
 ArchiveEntry entry = new ArchiveEntry("main.c");
 ArchiveEntry directoryEntry = new ArchiveEntry("testfiles");

 assertEquals("- 2000 main.c\n", ArchiveUtils.toString(entry));
 assertEquals("d 100 testfiles\n", ArchiveUtils.toString(directoryEntry));
 }

 // ---------------------------------------------------------------------- //
 // matchAsciiBuffer test cases                                            //
 // ---------------------------------------------------------------------- //

 @org.junit.Test
 public void testMatchAsciiBuffer() {
 String expected = "Hello World";
 byte[] buffer = toAsciiBytes(expected);

 assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 @org.junit.Test
 public void testMatchAsciiBufferFalse() {
 String expected = "Hello World";
 byte[] buffer = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\n'};

 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 @org.junit.Test
 public void testMatchAsciiBufferNull() {
 String expected = "Hello World";
 byte[] buffer = null;

 assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
 }

 // ---------------------------------------------------------------------- //
 // toAsciiBytes test cases                                               //
 // ---------------------------------------------------------------------- //

 @org.junit.Test
 public void testToAsciiBytes() {
 String input = "Hello World";
 byte[] expected = new byte[]{
        'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0'
 };
 byte[] result = ArchiveUtils.toAsciiBytes(input);

 assertArrayEquals(expected, result);
 }

 // ---------------------------------------------------------------------- //
 // toAsciiString test cases                                              //
 // ---------------------------------------------------------------------- //

 @org.junit.Test
 public void testToAsciiString() {
 byte[] input = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0'};
 String expected = "Hello World";
 String result = ArchiveUtils.toAsciiString(input);

 assertEquals(expected, result);
 }

 // ---------------------------------------------------------------------- //
 // isEqual test cases                                                    //
 // ---------------------------------------------------------------------- //

 @org.junit.Test
 public void testIsEqual() {
 byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0'};
 byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0'};

 assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
 }

 @org.junit.Test
 public void testIsEqualFalse() {
 byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0'};
 byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\1'};

 assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
 }

 @org.junit.Test
 public void testIsEqualNull() {
 byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0'};
 byte[] buffer2 = null;

 assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
 }

 @org.junit.Test
 public void testIsEqualIgnoreTrailingNulls() {
 byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0', '\0'};
 byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0'};

 assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2));
 }

 @org.junit.Test
 public void testIsEqualIgnoreTrailingNullsFalse() {
 byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\0', '\0'};
 byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\1', '\1'};

 assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2));
 }

 // ---------------------------------------------------------------------- //
 // isArrayZero test cases                                                //
 // ---------------------------------------------------------------------- //

 @org.junit.Test
 public void testIsArrayZeroTrue() {
 byte[] a = new byte[0];

 assertTrue(ArchiveUtils.isArrayZero(a, 0));
 }

 @org.junit.Test
 public void testIsArrayZeroFalse() {
 byte[] a = new byte[]{1};

 assertFalse(ArchiveUtils.isArrayZero(a, 0));
 }

 @org.junit.Test
 public void testIsArrayZeroFalseLength() {
 byte[] a = new byte[5];

 assertFalse(ArchiveUtils.isArrayZero(a, 5));
 }

 @org.junit.Test
 public void testIsArrayZeroTrueLength() {
 byte[] a = new byte[5];
 Arrays.fill(a, (byte) 0);

 assertTrue(ArchiveUtils.isArrayZero(a, 5));
 }

 // ---------------------------------------------------------------------- //
 // sanitize test cases                                                   //
 // ---------------------------------------------------------------------- //

 @org.junit.Test
 public void testSanitize() {
 String s = "Hello World! こんにちは、世界";
 String expected = "Hello World!\001\024\030\055\066\067\071\072";
 String result = ArchiveUtils.sanitize(s);

 assertEquals(expected, result);
 }

}