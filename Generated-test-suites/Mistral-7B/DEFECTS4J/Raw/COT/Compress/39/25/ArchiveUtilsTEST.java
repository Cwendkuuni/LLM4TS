package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "-       0 test.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithDirectory() {
        ArchiveEntry entry = new ArchiveEntry("test");
        entry.setDirectory(true);
        String result = ArchiveUtils.toString(entry);
        String expected = "d       0 test/";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithOffset() {
        String expected = "test";
        byte[] buffer = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00};
        int offset = 0;
        int length = 7;
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithMismatchedLength() {
        String expected = "test";
        byte[] buffer = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00, 0x61};
        int offset = 0;
        int length = 7;
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length);
        assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferWithUnsupportedEncodingException() {
        String expected = "test";
        byte[] buffer = new byte[10];
        ArchiveUtils.matchAsciiBuffer(expected, buffer);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00};
        byte[] buffer2 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithTrailingNulls() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00};
        byte[] buffer2 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00, 0x00};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00};
        byte[] buffer2 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00, 0x01};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00};
        byte[] buffer2 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00, 0x01};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullsInFirstBuffer() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00, 0x00};
        byte[] buffer2 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullsInSecondBuffer() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00};
        byte[] buffer2 = {0x74, 0x65, 0x73, 0x74, 0x65, 0x6d, 0x00, 0x00};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        boolean result = ArchiveUtils.isArrayZero(a, 5);
        assertFalse(result);
    }

    @Test
    public void testIsArrayZeroWithArrayOfZeros() {
        byte[] a = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        boolean result = ArchiveUtils.isArrayZero(a, 11);
        assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String s = "test\\test/test\"test<test>!@#$%^&*()_+={[}]|\\:;\"'<>,.?/~`";
        String result = ArchiveUtils.sanitize(s);
        String expected = "test??test??test??test??test??test??test??test??test??test";
        assertEquals(expected, result);
    }
}