package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CharsetNames;

import org.junit.Test;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        assertEquals("- 2000 main.c", ArchiveUtils.toString(entry));
        entry = new ArchiveEntry("testfiles");
        assertEquals("d 100 testfiles", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello";
        byte[] buffer = toAsciiBytes(expected);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferFailed() {
        String expected = "hello";
        byte[] buffer = new byte[5];

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "hello";
        byte[] expected = new byte[5];
        Arrays.fill(expected, (byte) 0x68); // 'h'

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F }; // 'hello'
        String expected = "hello";

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F }; // 'hello'
        byte[] buffer2 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F }; // 'hello'

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        byte[] buffer3 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x61, 0x6D, 0x65 }; // 'helloworld'
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        byte[] buffer4 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x61, 0x6D, 0x65, 0x00 }; // 'helloworld\0'
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));
    }

    @Test
    public void testIsEqualIgnoringTrailingNulls() {
        byte[] buffer1 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x00 }; // 'hello\0'
        byte[] buffer2 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F }; // 'hello'

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x61, 0x6D, 0x65 }; // 'helloworld'
        byte[] buffer2 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F }; // 'hello'

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 4, false)); // 'hello'
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 6, buffer2, 0, 5, false)); // 'hello'

        assertTrue(ArchiveUtils.isEqual(buffer1, 1, 5, buffer2, 0, 5, false)); // 'ello'
        assertTrue(ArchiveUtils.isEqual(buffer1, 5, 3, buffer2, 0, 3, false)); // 'world'
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F }; // 'hello'
        byte[] buffer2 = new byte[0];

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualWithNullAndOffsetAndLength() {
        byte[] buffer1 = new byte[] { 0x68, 0x65, 0x6C, 0x6C, 0x6F }; // 'hello'
        byte[] buffer2 = new byte[0];

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        assertFalse(ArchiveUtils.isEqual(buffer1, 1, buffer1.length - 1, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 1, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length + 1, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, 1, buffer1.length - 1, buffer2, 1, buffer2.length - 1, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(array, 1));
        assertFalse(ArchiveUtils.isArrayZero(array, 5));
        assertFalse(ArchiveUtils.isArrayZero(array, 11));
        assertTrue(ArchiveUtils.isArrayZero(array, 10));
    }

    @Test
    public void testSanitize() {
        String inputString = "hello\u0000world!";
        String expected = "hello????????world?";
        String result = ArchiveUtils.sanitize(inputString);

        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytesWithUnsupportedEncodingException() {
        String inputString = "unicode";
        ArchiveUtils.toAsciiBytes(inputString);
    }

}