package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToStringMethod() {
        ArchiveEntry archiveEntry = new ArchiveEntry("testfile.txt");
        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "- 0 testfile.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBufferMethodWithCorrectData() {
        String expected = "expectedString";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferMethodWithIncorrectData() {
        String expected = "expectedString";
        byte[] buffer = new byte[10];
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertFalse(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToStringMethodWithUnsupportedEncodingException() {
        String inputString = "inputString";
        ArchiveUtils.toAsciiBytes(inputString); // This should throw an UnsupportedEncodingException
    }

    @Test
    public void testIsEqualMethodWithEqualBuffers() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqualMethodWithDifferentBuffers() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'D', 'E', 'F'};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertFalse(result);
    }

    @Test
    public void testIsEqualMethodWithBuffersWithTrailingNulls() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', 0, 0, 0};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualMethodWithBuffersWithoutTrailingNulls() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZeroMethodWithZeroArray() {
        byte[] array = new byte[0];
        boolean result = ArchiveUtils.isArrayZero(array, 0);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZeroMethodWithNonZeroArray() {
        byte[] array = new byte[]{'A'};
        boolean result = ArchiveUtils.isArrayZero(array, 0);
        assertFalse(result);
    }

    @Test
    public void testIsArrayZeroMethodWithZeroArrayWithoutSizeParameter() {
        byte[] array = new byte[0];
        boolean result = ArchiveUtils.isArrayZero(array);
        assertTrue(result);
    }

    @Test
    public void testSanitizeMethod() {
        String input = "test\012string";
        String result = ArchiveUtils.sanitize(input);
        String expected = "test???";
        assertEquals(expected, result);
    }

}