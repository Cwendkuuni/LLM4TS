package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)}
     */
    @Test
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

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBufferStringByteArrayIntInt() {
        String expected = "expected";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}
     */
    @Test
    public void testMatchAsciiBufferStringByteArray() {
        String expected = "expected";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiBytes(String)}
     */
    @Test
    public void testToAsciiBytesString() {
        String inputString = "inputString";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        String expected = inputString;
        assertArrayEquals(expected.getBytes(CharsetNames.US_ASCII), result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[])}
     */
    @Test
    public void testToAsciiStringByteArray() {
        byte[] inputBytes = "inputString".getBytes(CharsetNames.US_ASCII);
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "inputString";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[], int, int)}
     */
    @Test
    public void testToAsciiStringByteArrayIntInt() {
        byte[] inputBytes = "inputString".getBytes(CharsetNames.US_ASCII);
        String result = ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length);
        String expected = "inputString";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}
     */
    @Test
    public void testIsEqualByteArrayIntIntByteArrayIntIntBoolean() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertTrue(result);

        byte[] buffer3 = {1, 2, 3, 4};
        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int)}
     */
    @Test
    public void testIsEqualByteArrayIntIntByteArrayIntInt() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);

        byte[] buffer3 = {1, 2, 3, 4};
        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[])}
     */
    @Test
    public void testIsEqualByteArrayByteArray() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);

        byte[] buffer3 = {1, 2, 3, 4};
        result = ArchiveUtils.isEqual(buffer1, buffer3);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[], boolean)}
     */
    @Test
    public void testIsEqualByteArrayByteArrayBoolean() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, false);
        assertTrue(result);

        byte[] buffer3 = {1, 2, 3, 4};
        result = ArchiveUtils.isEqual(buffer1, buffer3, false);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqualWithNull(byte[], int, int, byte[], int, int)}
     */
    @Test
    public void testIsEqualWithNullByteArrayIntIntByteArrayIntInt() {
        byte[] buffer1 = {'a', 0, 0};
        byte[] buffer2 = {'a', 'b', 'c'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);

        byte[] buffer3 = {'a', 0, 0, 'd'};
        result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}
     */
    @Test
    public void testIsArrayZeroByteArrayInt() {
        byte[] a = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));

        byte[] a1 = {1, 2, 3};
        assertFalse(ArchiveUtils.isArrayZero(a1, a1.length));

        byte[] a2 = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(a2, a2.length));
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}
     */
    @Test
    public void testSanitizeString() {
        String s = "inputString";
        String result = ArchiveUtils.sanitize(s);
        String expected = "inputString";
        assertEquals(expected, result);

        s = "input\u0001String";
        expected = "input??String";
        result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);

        s = "input\nString";
        expected = "inputString";
        result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);
    }
}