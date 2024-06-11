package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.test.TestFiles;
import org.junit.Test;

/**
 * Unit tests for the ArchiveUtils class
 */
public class ArchiveUtilsTest {

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        ArchiveEntry sampleEntry = TestFiles.getTestFileEntry("testfile.txt");
        String expected = "- 123 testfile.txt";
        String result = ArchiveUtils.toString(sampleEntry);
        assertEquals(expected, result);
    }

    /**
     * Test matchAsciiBuffer method with correct string and buffer
     */
    @Test
    public void testMatchAsciiBufferCorrect() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    /**
     * Test matchAsciiBuffer method with incorrect string and buffer
     */
    @Test
    public void testMatchAsciiBufferIncorrect() {
        String expected = "test";
        byte[] buffer = new byte[] { 1, 2, 3 }; // different bytes
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    /**
     * Test isEqual method with equal byte arrays
     */
    @Test
    public void testIsEqualEqualByteArrays() {
        byte[] buffer1 = toAsciiBytes("test");
        byte[] buffer2 = toAsciiBytes("test");
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    /**
     * Test isEqual method with different byte arrays
     */
    @Test
    public void testIsEqualDifferentByteArrays() {
        byte[] buffer1 = toAsciiBytes("test");
        byte[] buffer2 = toAsciiBytes("test2");
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    /**
     * Test isEqual method with byte arrays of different lengths but equal contents
     */
    @Test
    public void testIsEqualByteArraysDifferentLengthsButEqual() {
        byte[] buffer1 = toAsciiBytes("test");
        byte[] buffer2 = new byte[] { 't', 'e', 's', 0 };
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test isEqual method with byte arrays of different lengths and different contents
     */
    @Test
    public void testIsEqualByteArraysDifferentLengthsAndDifferent() {
        byte[] buffer1 = toAsciiBytes("test");
        byte[] buffer2 = new byte[] { 't', 'e', 's', '2' };
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test isEqual method with byte arrays of different lengths but equal trailing nulls
     */
    @Test
    public void testIsEqualByteArraysDifferentLengthsButEqualTrailingNulls() {
        byte[] buffer1 = toAsciiBytes("test");
        byte[] buffer2 = new byte[] { 't', 'e', 's', 0, 0 };
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test isEqual method with byte arrays of different lengths but different trailing nulls
     */
    @Test
    public void testIsEqualByteArraysDifferentLengthsButDifferentTrailingNulls() {
        byte[] buffer1 = toAsciiBytes("test");
        byte[] buffer2 = new byte[] { 't', 'e', 's', 0, 1 };
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test isArrayZero method with zero byte array
     */
    @Test
    public void testIsArrayZeroZeroByteArray() {
        byte[] array = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(array, 0));
    }

    /**
     * Test isArrayZero method with non-zero byte array
     */
    @Test
    public void testIsArrayZeroNonZeroByteArray() {
        byte[] array = new byte[] { 1, 2, 3 };
        assertFalse(ArchiveUtils.isArrayZero(array, 0));
    }

    /**
     * Test sanitize method
     */
    @Test
    public void testSanitize() {
        String input = "test\u0001file\nname";
        String expected = "test?\u0001file?name";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }
}