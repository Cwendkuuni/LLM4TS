package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        assertEquals("- 2000 main.c", ArchiveUtils.toString(entry));

        entry.setDirectory(true);
        assertEquals("d 100 testfiles", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "magic";
        byte[] buffer = toAsciiBytes(expected);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferDifferentLengths() {
        String expected = "magic";
        byte[] buffer = new byte[10];

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferNull() {
        String expected = "magic";
        byte[] buffer = null;

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiBytes() {
        String expected = "magic";

        byte[] result = ArchiveUtils.toAsciiBytes(expected);

        assertArrayEquals(expected.getBytes(CharsetNames.US_ASCII), result);
    }

    @Test
    public void testToAsciiString() {
        String input = "magic";
        byte[] inputBytes = toAsciiBytes(input);

        assertEquals(input, ArchiveUtils.toAsciiString(inputBytes));
        assertEquals(input, ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'m', 'a', 'g', 'i', 'c'};
        byte[] buffer2 = new byte[]{'m', 'a', 'g', 'i', 'c'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        byte[] buffer3 = new byte[]{'m', 'a', 'g', 'i', 'c', 'd'};

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'m', 'a', 'g', 'i', 'c'};
        byte[] buffer2 = new byte[]{'m', 'a', 'g', 'i', 'c'};

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{'m', 'a', 'g', 'i', 'c', 'd'};

        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsEqualArrays() {
        byte[] buffer1 = new byte[]{'m', 'a', 'g', 'i', 'c'};
        byte[] buffer2 = new byte[]{'m', 'a', 'g', 'i', 'c'};

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualArraysWithNull() {
        byte[] buffer1 = new byte[]{'m', 'a', 'g', 'i', 'c'};
        byte[] buffer2 = null;

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];

        assertTrue(ArchiveUtils.isArrayZero(array, 0));
        assertTrue(ArchiveUtils.isArrayZero(array, 0, 10));
    }

    @Test
    public void testSanitize() {
        String s = "test\u0001string\u001fwith\u001cnon-printable\u007fcharacters.";
        String sanitized = ArchiveUtils.sanitize(s);

        assertEquals("test?string??with??characters.", sanitized);
    }
}