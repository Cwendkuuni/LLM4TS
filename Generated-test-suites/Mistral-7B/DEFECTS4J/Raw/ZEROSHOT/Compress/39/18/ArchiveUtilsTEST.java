package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "-       0 test.txt";
        assertEquals(expected, result);

        entry.setDirectory(true);
        result = ArchiveUtils.toString(entry);
        expected = "d        0 test.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBuffer_WrongLength() {
        String expected = "test";
        byte[] buffer = new byte[5];
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBuffer_DifferentStrings() {
        String expected = "test";
        byte[] buffer = "Hello World".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        byte[] expected = new byte[]{'t', 'e', 's', 't'};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{'t', 'e', 's', 't'};
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "test";
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiString_WrongLength() {
        byte[] inputBytes = new byte[]{'t', 'e', 's'};
        String result = ArchiveUtils.toAsciiString(inputBytes, 0, 4);
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer3 = new byte[]{'t', 'e', 's', 't', 'r'};
        byte[] buffer4 = new byte[]{'t', 'e', 's', 't', 'r', 'e'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, true));

        byte[] buffer5 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer6 = new byte[]{'t', 'e', 's', 't', '\u0000'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer6, 0, buffer6.length, true));
    }

    @Test
    public void testIsEqual_WithNull() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't', '\u0000'};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));

        byte[] a1 = new byte[]{0};
        assertTrue(ArchiveUtils.isArrayZero(a1, 0));
        assertTrue(ArchiveUtils.isArrayZero(a1, 1));

        byte[] a2 = new byte[]{0, 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(a2, 0));
        assertTrue(ArchiveUtils.isArrayZero(a2, 1));
        assertTrue(ArchiveUtils.isArrayZero(a2, 2));

        byte[] a3 = new byte[]{0, 0, 0, 1};
        assertFalse(ArchiveUtils.isArrayZero(a3, 0));
        assertFalse(ArchiveUtils.isArrayZero(a3, 1));
        assertFalse(ArchiveUtils.isArrayZero(a3, 2));
        assertFalse(ArchiveUtils.isArrayZero(a3, 3));
    }

    @Test
    public void testSanitize() {
        String input = "\u0000\u0001test\u0002\u007f\u001aA\u001bB\u001cC\u001dD\u001eE\u001fF\u007f\u007f";
        String expected = "test??A?B?C?D??E?F?";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }
}