package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new TestArchiveEntry("test.txt", true, 10);
        assertEquals("-       10 test.txt\n", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Test String";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBuffer_False() {
        String expected = "Test String";
        byte[] buffer = new byte[10];
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testIsEqual_False() {
        byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
        byte[] buffer2 = new byte[]{0x01, 0x02, 0x04};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqual_True() {
        byte[] buffer1 = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0x04};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqual_True_IgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{"Test".getBytes(CharsetNames.US_ASCII), {0x00}};
        byte[] buffer2 = new byte[]{"Test".getBytes(CharsetNames.US_ASCII)};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqual_False_IgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{"Test".getBytes(CharsetNames.US_ASCII), {0x00}};
        byte[] buffer2 = new byte[]{"Test1".getBytes(CharsetNames.US_ASCII)};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testIsArrayZero_False() {
        byte[] a = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        assertFalse(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testSanitize() {
        String input = "Test\u00FCstring";
        String expected = "Test????string";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    private static class TestArchiveEntry extends ArchiveEntry {
        TestArchiveEntry(String name, boolean isDirectory, long size) {
            super(name, isDirectory, size);
        }
    }
}