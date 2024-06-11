package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

/**
 * Test class for the ArchiveUtils class.
 */
public class ArchiveUtilsTest {

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)}.
     */
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        ArchiveEntry directoryEntry = new ArchiveEntry("testfiles/");

        String toStringResult = ArchiveUtils.toString(entry);
        assertEquals("- 2000 main.c", toStringResult);

        toStringResult = ArchiveUtils.toString(directoryEntry);
        assertEquals("d     100 testfiles/", toStringResult);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}.
     */
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}.
     */
    @Test
    public void testMatchAsciiBufferEmptyOffset() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}.
     */
    @Test
    public void testMatchAsciiBufferDifferentLengths() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        byte[] shorterBuffer = Arrays.copyOfRange(buffer, 0, 3);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, shorterBuffer);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)}.
     * This test case checks the edge case where the ArchiveEntry name is an empty string.
     */
    @Test
    public void testToStringEmptyEntryName() {
        ArchiveEntry emptyEntry = new ArchiveEntry("");

        String toStringResult = ArchiveUtils.toString(emptyEntry);
        assertEquals("d     0", toStringResult);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[], int, int, int, int, boolean)}.
     */
    @Test
    public void testIsEqual() {
        byte[] buffer1 = toAsciiBytes("test123");
        byte[] buffer2 = toAsciiBytes("test123");
        byte[] buffer3 = toAsciiBytes("test456");

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertTrue(result);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqualWithNull(byte[], byte[], int, int, int, int)}.
     */
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[10];
        byte[] buffer2 = new byte[10];
        byte[] buffer3 = new byte[5];
        Arrays.fill(buffer1, (byte) 0);
        Arrays.fill(buffer2, (byte) 0);
        Arrays.fill(buffer3, (byte) 0);

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);

        result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}.
     */
    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];

        boolean result = ArchiveUtils.isArrayZero(buffer, 5);
        assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}.
     */
    @Test
    public void testIsArrayZeroAllZero() {
        byte[] buffer = new byte[10];
        Arrays.fill(buffer, (byte) 0);

        boolean result = ArchiveUtils.isArrayZero(buffer, 10);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}.
     */
    @Test
    public void testSanitize() {
        String s = "\u0000\u0001test\u0002\u007f";
        String sanitized = ArchiveUtils.sanitize(s);
        String expected = "\u0000\u0001????\u007f";

        assertEquals(expected, sanitized);
    }
}