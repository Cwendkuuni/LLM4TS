package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        String expected = "-       0 test.txt";
        String result = ArchiveUtils.toString(archiveEntry);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferWithUnsupportedEncodingException() {
        String expected = "test";
        byte[] buffer = new byte[10];
        ArchiveUtils.matchAsciiBuffer(expected, buffer);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = toAsciiBytes("test1").concat(Arrays.copyOfRange(toAsciiBytes("test2"), 3, 6));
        byte[] buffer2 = toAsciiBytes("test3").concat(Arrays.copyOfRange(toAsciiBytes("test4"), 3, 6));
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, false);
        assertFalse(result);

        byte[] buffer3 = toAsciiBytes("test1");
        byte[] buffer4 = toAsciiBytes("test1");
        byte[] buffer5 = toAsciiBytes("test2");
        boolean result1 = ArchiveUtils.isEqual(buffer3, buffer4, 0, buffer4.length, 0, buffer4.length, false);
        assertTrue(result1);
        assertFalse(ArchiveUtils.isEqual(buffer3, buffer5, 0, buffer3.length, 0, buffer5.length, false));

        byte[] buffer6 = toAsciiBytes("test1");
        byte[] buffer7 = toAsciiBytes("test2");
        boolean result2 = ArchiveUtils.isEqual(buffer6, buffer7, false);
        assertFalse(result2);
        boolean result3 = ArchiveUtils.isEqual(buffer6, buffer7, true);
        assertTrue(result3);

        byte[] buffer8 = new byte[10];
        Arrays.fill(buffer8, (byte) 0);
        boolean result4 = ArchiveUtils.isEqual(buffer3, buffer8, 0, buffer3.length, 0, buffer8.length, false);
        assertFalse(result4);
        boolean result5 = ArchiveUtils.isEqual(buffer3, buffer8, 0, buffer3.length, 0, buffer8.length, true);
        assertTrue(result5);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = toAsciiBytes("test1");
        byte[] buffer2 = toAsciiBytes("test1");
        byte[] buffer3 = toAsciiBytes("test2");
        boolean result1 = ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer2.length, 0, buffer2.length);
        assertTrue(result1);
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer3, 0, buffer3.length, 0, buffer3.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        Arrays.fill(a, (byte) 0);
        boolean result = ArchiveUtils.isArrayZero(a, 10);
        assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String s = "test\u0001test\u007Ftest";
        String expected = "test?test?test";
        String result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);
    }

}