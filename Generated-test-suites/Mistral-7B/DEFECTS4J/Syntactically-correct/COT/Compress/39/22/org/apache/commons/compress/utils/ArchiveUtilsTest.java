package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    @org.junit.Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, result);

        entry.setDirectory(true);
        expected = "d 100 testfiles";
        result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }

    @ org.junit.Test
    public void testMatchAsciiBuffer() {
        String expected = "hello world";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferWithUnsupportedEncodingException() {
        String expected = "unicode string";
        ArchiveUtils.matchAsciiBuffer(expected, new byte[0]);
    }

    @org.junit.Test
    public void testToAsciiString() {
        byte[] inputBytes = toAsciiBytes("hello world");
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "hello world";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToAsciiStringWithInvalidOffset() {
        byte[] inputBytes = toAsciiBytes("hello world".getBytes());
        String result = ArchiveUtils.toAsciiString(inputBytes, -1, inputBytes.length);
        String expected = "hello world";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, false);
        assertTrue(result);

        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o', 'a'};
        result = ArchiveUtils.isEqual(buffer1, 0, 5, buffer4, 0, 6, false);
        assertFalse(result);

        buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        result = ArchiveUtils.isEqual(buffer1, 0, 5, buffer4, 0, 5, true);
        assertTrue(result);

        byte[] buffer5 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer6 = new byte[]{'h', 'e', 'l', 'l', 'o', 'a', 'b'};
        result = ArchiveUtils.isEqualWithNull(buffer5, 0, 5, buffer6, 0, 6);
        assertTrue(result);
    }

    @org.junit.Test
    public void testIsEqualWithNullFalse() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, false);
        assertTrue(result);

        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o', 'a'};
        result = ArchiveUtils.isEqual(buffer3, 0, 5, buffer4, 0, 6, false);
        assertFalse(result);

        byte[] buffer5 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer6 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        result = ArchiveUtils.isEqual(buffer5, 0, 5, buffer6, 0, 5, false);
        assertTrue(result);

        byte[] buffer7 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer8 = new byte[]{'h', 'e', 'l', 'l', 'o', 0};
        result = ArchiveUtils.isEqual(buffer7, 0, 5, buffer8, 0, 6, false);
        assertFalse(result);
    }

    @org.junit.Test
    public void testIsEqualWithNullTrue() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, true);
        assertTrue(result);

        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o', 'a'};
        result = ArchiveUtils.isEqual(buffer3, 0, 5, buffer4, 0, 6, true);
        assertTrue(result);

        byte[] buffer5 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer6 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        result = ArchiveUtils.isEqual(buffer5, 0, 5, buffer6, 0, 5, true);
        assertTrue(result);

        byte[] buffer7 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer8 = new byte[]{'h', 'e', 'l', 'l', 'o', 0};
        result = ArchiveUtils.isEqualWithNull(buffer7, 0, 5, buffer8, 0, 6, true);
        assertTrue(result);
    }

    @org.junit.Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        boolean result = ArchiveUtils.isArrayZero(a, 0);
        assertTrue(result);

        byte[] b = new byte[5];
        Arrays.fill(b, (byte) 0);
        result = ArchiveUtils.isArrayZero(b, 5);
        assertTrue(result);

        byte[] c = new byte[5];
        Arrays.fill(c, (byte) 1);
        result = ArchiveUtils.isArrayZero(c, 5);
        assertFalse(result);
    }

    @org.junit.Test
    public void testSanitize() {
        String s = "\u0001hello\u0002world\u0003!@#$%^&*()_+{}|~:\"<>\u007F";
        String result = ArchiveUtils.sanitize(s);
        String expected = "!!?!?????hello??????world??????!@#$%^&*()_+{}|~:\"<>\u007F";
        assertEquals(expected, result);
    }
}