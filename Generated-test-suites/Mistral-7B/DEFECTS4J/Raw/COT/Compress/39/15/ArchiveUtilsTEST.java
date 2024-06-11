package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "- 0      test.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringForDirectory() {
        ArchiveEntry archiveEntry = new ArchiveEntry("testDir");
        archiveEntry.setDirectory(true);
        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "d 0       testDir/";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expectedString";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBuffer_False() {
        String expected = "expectedString";
        byte[] buffer = new byte[10];
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBuffer_UnsupportedEncodingException() {
        String expected = "expectedString";
        byte[] buffer = new byte[10];
        ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{1, 2, 3, 1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3, 1, 2, 3};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_False() {
        byte[] buffer1 = new byte[]{1, 2, 3, 1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3, 1, 2, 4};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_IgnoreTrailingNulls_True() {
        byte[] buffer1 = new byte[]{1, 2, 3, 1, 2, 3, (byte)0};
        byte[] buffer2 = new byte[]{1, 2, 3, 1, 2, 3};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsEqual_IgnoreTrailingNulls_False() {
        byte[] buffer1 = new byte[]{1, 2, 3, 1, 2, 3, (byte)0};
        byte[] buffer2 = new byte[]{1, 2, 3, 1, 2, 3};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_LengthDifferent() {
        byte[] buffer1 = new byte[]{1, 2, 3, 1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(array, 5));
    }

    @Test
    public void testIsArrayZero_False() {
        byte[] array = new byte[10];
        assertFalse(ArchiveUtils.isArrayZero(array, 5));
    }

    @Test
    public void testSanitize() {
        String input = "test/string with spaces and special chars: !@#$%^&*()_+-={}[]|\\;:\"'<>,.?/~`";
        String expected = "test/string.................:.................?";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

}