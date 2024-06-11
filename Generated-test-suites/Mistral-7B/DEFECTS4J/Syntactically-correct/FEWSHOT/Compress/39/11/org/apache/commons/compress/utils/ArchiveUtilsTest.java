package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        final ArchiveEntry entry = new ArchiveEntry("test.txt");
        assertEquals("-       0 test.txt", ArchiveUtils.toString(entry));
        final ArchiveEntry directoryEntry = new ArchiveEntry("test/");
        assertEquals("d       0 test/", ArchiveUtils.toString(directoryEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        final String expected = "test";
        final byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBuffer_False() {
        final String expected = "test";
        final byte[] buffer = new byte[] { 't', 'e', 's', 't' };
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testIsEqual() {
        final byte[] buffer1 = new byte[] { 't', 'e', 's', 't' };
        final byte[] buffer2 = new byte[] { 't', 'e', 's', 't' };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, false));

        final byte[] buffer3 = new byte[] { 't', 'e', 's', 't', 'a' };
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, false));

        final byte[] buffer4 = new byte[] { 't', 'e', 's', 't' };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, true));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer4));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer4, false));
        assertTrue(Arrays.equals(buffer1, buffer4));

        final byte[] buffer5 = new byte[] { 't', 'e', 's', 't', 0 };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, true));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer5));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer5, false));
        assertTrue(Arrays.equals(buffer1, Arrays.copyOf(buffer5, buffer5.length - 1)));

        final byte[] buffer6 = new byte[] { 0, 't', 'e', 's', 't' };
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer6, 0, buffer6.length, true));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer6));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer6, false));
        assertFalse(Arrays.equals(buffer1, buffer6));
    }

    @Test
    public void testToAsciiBytes() {
        assertEquals(toAsciiBytes("test"), new byte[] { 't', 'e', 's', 't' });
    }

    @Test
    public void testToAsciiString() {
        assertEquals(toAsciiString(toAsciiBytes("test")), "test");
        assertEquals(toAsciiString(toAsciiBytes("test"), 0, toAsciiBytes("test").length), "test");
        assertEquals(toAsciiString(toAsciiBytes("test"), 0, toAsciiBytes("test").length - 1), "test");
    }

    @Test(expected = RuntimeException.class)
    public void testToAsciiString_UnsupportedEncodingException() {
        toAsciiBytes("");
    }

    @Test
    public void testIsArrayZero() {
        assertTrue(ArchiveUtils.isArrayZero(new byte[0]));
        assertTrue(ArchiveUtils.isArrayZero(new byte[] { 0 }));
        assertTrue(ArchiveUtils.isArrayZero(new byte[] { 0, 0, 0 }));
        assertFalse(ArchiveUtils.isArrayZero(new byte[] { 1 }));
        assertFalse(ArchiveUtils.isArrayZero(new byte[] { 0, 1 }));
    }

    @Test
    public void testSanitize() {
        assertEquals("test", ArchiveUtils.sanitize("test"));
        assertEquals("test\u0001test", ArchiveUtils.sanitize("test\u0001test"));
        assertEquals("test?test", ArchiveUtils.sanitize("test\u0000test"));
        assertEquals("test?...test", ArchiveUtils.sanitize("test........test"));
    }
}