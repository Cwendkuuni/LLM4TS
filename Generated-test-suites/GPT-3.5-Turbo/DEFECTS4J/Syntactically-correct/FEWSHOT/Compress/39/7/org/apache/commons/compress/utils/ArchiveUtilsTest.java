package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
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
        String expected = "hello world!";
        byte[] buffer1 = expected.getBytes();
        byte[] buffer2 = "hello world!".getBytes();
        byte[] buffer3 = "hello world".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer3));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "hello world!";
        byte[] expected = inputString.getBytes();

        assertEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "hello world!".getBytes();
        String expected = "hello world!";

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "hello world!".getBytes();
        int offset = 2;
        int length = 8;
        String expected = "llo worl";

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3};
        byte[] buffer3 = new byte[]{1, 2, 4};

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{1, 2, 0, 0};
        byte[] buffer2 = new byte[]{1, 2, 0, 0};
        byte[] buffer3 = new byte[]{1, 2, 1, 0};

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, true));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        int offset1 = 1;
        int length1 = 2;
        byte[] buffer2 = new byte[]{2, 3};
        int offset2 = 0;
        int length2 = 2;
        byte[] buffer3 = new byte[]{2, 4};
        int offset3 = 0;
        int length3 = 2;

        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
        assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer3, offset3, length3));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array1 = new byte[]{0, 0, 0, 0};
        byte[] array2 = new byte[]{0, 0, 1, 0};

        assertTrue(ArchiveUtils.isArrayZero(array1, 4));
        assertFalse(ArchiveUtils.isArrayZero(array2, 4));
    }

    @Test
    public void testSanitize() {
        String inputString = "Hello\tworld!\n";
        String expected = "Hello?world!?";

        assertEquals(expected, ArchiveUtils.sanitize(inputString));
    }
}