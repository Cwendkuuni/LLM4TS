package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("main.c", 2000, false);
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));

        ArchiveEntry entry2 = new ArchiveEntry("testfiles", 100, true);
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer1 = {1, 2, 3, 4, 5};
        assertTrue(ArchiveUtils.matchAsciiBuffer("abcde", buffer1));

        byte[] buffer2 = {1, 2, 3, 0, 0};
        assertTrue(ArchiveUtils.matchAsciiBuffer("abc", buffer2, 0, 3));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] expected = {97, 98, 99};
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes("abc"));
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = {97, 98, 99};
        assertEquals("abc", ArchiveUtils.toAsciiString(buffer));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        byte[] buffer3 = {1, 2};
        byte[] buffer4 = {1, 2, 3};
        assertFalse(ArchiveUtils.isEqual(buffer3, buffer4));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 3));

        byte[] buffer3 = {1, 2};
        byte[] buffer4 = {1, 2, 3};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer3, 0, 2, buffer4, 0, 3));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = {0, 0, 0, 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(array, 5));

        byte[] array2 = {0, 1, 0};
        assertFalse(ArchiveUtils.isArrayZero(array2, 3));
    }

    @Test
    public void testSanitize() {
        String s = "abc";
        assertEquals("abc", ArchiveUtils.sanitize(s));

        String s2 = "abc\n";
        assertEquals("abc?", ArchiveUtils.sanitize(s2));
    }
}