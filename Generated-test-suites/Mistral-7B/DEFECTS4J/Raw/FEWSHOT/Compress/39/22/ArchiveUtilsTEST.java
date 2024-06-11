package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = null;
        assertEquals("", ArchiveUtils.toString(entry));

        entry = new ArchiveEntry("test.txt");
        assertEquals("-       0 test.txt", ArchiveUtils.toString(entry));

        entry.setSize(1024);
        assertEquals("-      1024 test.txt", ArchiveUtils.toString(entry));

        entry.setSize(-1);
        assertEquals("-      -1 test.txt", ArchiveUtils.toString(entry));

        entry.setDirectory(true);
        assertEquals("d       0 test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);

        byte[] buffer1 = Arrays.copyOfRange(buffer, 0, 3);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1, 0, 3));

        byte[] buffer2 = Arrays.copyOfRange(buffer, 3, 6);
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer2, 0, 3));

        byte[] buffer3 = Arrays.copyOfRange(buffer, 0, 7);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer3, 0, 7));

        byte[] buffer4 = Arrays.copyOfRange(buffer, 0, 4);
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer4, 0, 5));
    }

    @Test
    public void testToAsciiString() {
        String input = "test";
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(toAsciiBytes(input)));

        byte[] buffer = toAsciiBytes(input);
        int offset = 3;
        int length = 3;
        assertEquals(expected.substring(offset, offset + length), ArchiveUtils.toAsciiString(buffer, offset, length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer3 = new byte[]{'t', 'e', 's', 't', 'x'};
        byte[] buffer4 = new byte[]{'t', 'e', 's', 't', 'x', 'y'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length));

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer4));

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer4, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer4, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));

        byte[] a1 = new byte[]{0};
        assertTrue(ArchiveUtils.isArrayZero(a1, 1));

        byte[] a2 = new byte[]{0, 0};
        assertTrue(ArchiveUtils.isArrayZero(a2, 2));

        byte[] a3 = new byte[]{0, 0, 1};
        assertFalse(ArchiveUtils.isArrayZero(a3, 3));
    }

    @Test
    public void testSanitize() {
        String test = "test\u0001\u0010\u007Ftest";
        String expected = "test? ???test";
        assertEquals(expected, ArchiveUtils.sanitize(test));
    }
}