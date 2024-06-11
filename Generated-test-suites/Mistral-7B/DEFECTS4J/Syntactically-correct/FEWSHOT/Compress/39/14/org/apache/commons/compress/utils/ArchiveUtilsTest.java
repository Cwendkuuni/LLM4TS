package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        assertEquals("-      0 test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBuffer_offsetLength() {
        String expected = "test";
        byte[] buffer = new byte[10];
        System.arraycopy(toAsciiBytes(expected), 0, buffer, 0, expected.length());
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, expected.length()));
    }

    @Test
    public void testMatchAsciiBuffer_null() {
        assertFalse(ArchiveUtils.matchAsciiBuffer(null, new byte[0]));
        assertFalse(ArchiveUtils.matchAsciiBuffer("", new byte[0]));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_differentLengths() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 'a'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't', '\u0000'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls_differentLengths() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't', '\u0000'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length - 1, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsEqual_null() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        assertNull(ArchiveUtils.isEqual(null, 0, 0, buffer1, 0, buffer1.length, false));
        assertNull(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, null, 0, 0, false));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testIsArrayZero_partiallyZero() {
        byte[] a = new byte[5];
        Arrays.fill(a, (byte) 0, 3, (byte) 1);
        assertFalse(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testSanitize() {
        String s = "test\u0001this is \u001Aa \uD800test";
        String sanitized = ArchiveUtils.sanitize(s);
        assertEquals("test????this is ??a ????test", sanitized);
    }
}