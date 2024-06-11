package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000L);
        entry.setDirectory(false);

        assertEquals("- 2000 main.c", ArchiveUtils.toString(entry));

        entry.setDirectory(true);
        assertEquals("d 100 testfiles", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello world";
        byte[] buffer = toAsciiBytes(expected);

        byte[] testBuffer = Arrays.copyOfRange(buffer, 0, 6);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, testBuffer));

        testBuffer = Arrays.copyOfRange(buffer, 6, buffer.length);
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, testBuffer));

        testBuffer = Arrays.copyOfRange(buffer, 0, 5);
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, testBuffer));

        testBuffer = Arrays.copyOfRange(buffer, 0, 7);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, testBuffer));
    }

    @Test
    public void testToAsciiString() {
        String expected = "hello world";
        byte[] buffer = toAsciiBytes(expected);

        assertEquals(expected, ArchiveUtils.toAsciiString(buffer));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8 };
        byte[] buffer2 = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
        byte[] buffer3 = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x8 };

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer1, 0, buffer1.length, 0, buffer1.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, 0, buffer1.length, 0, buffer3.length, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer1, 0, buffer1.length, 0, buffer1.length, true));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer3, 0, buffer1.length, 0, buffer3.length, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8 };
        byte[] buffer2 = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0, 0, 0 };

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer1, 0, buffer1.length, 0, buffer1.length));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length));
    }

    @Test
    public void testisArrayZero() {
        byte[] a = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
        assertFalse(ArchiveUtils.isArrayZero(a, 6));
    }

    @Test
    public void testSanitize() {
        String s = "\u0000foo\u001Fbar\u007Fbaz\u0012quux\n";
        String expected = "????foo???bar???baz??quux?";
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }
}