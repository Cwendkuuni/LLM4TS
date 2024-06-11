package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        assertEquals("-       0 test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] expected = "expected".getBytes();
        byte[] buffer = new byte[expected.length];
        System.arraycopy(expected, 0, buffer, 0, expected.length);
        assertTrue(ArchiveUtils.matchAsciiBuffer(new String("expected"), buffer));
    }

    @Test
    public void testMatchAsciiBuffer_Null() {
        assertNull(ArchiveUtils.matchAsciiBuffer(null, null, 0, 0));
    }

    @Test
    public void testMatchAsciiBuffer_False() {
        byte[] expected = "expected".getBytes();
        byte[] buffer =new byte[expected.length + 1];
        System.arraycopy(expected, 0, buffer, 0, expected.length);
        buffer[expected.length] = 1;
        assertFalse(ArchiveUtils.matchAsciiBuffer(new String("expected"), buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "expected";
        byte[] bytes = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expectedStringToByteArray("expected"), bytes);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{'e', 'x', 'p', 'e', 'c', 't', 'e', 'd'};
        assertEquals(new String("expected"), ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'e', 'x', 'p', 'e', 'c', 't', 'e', 'd'};
        byte[] buffer2 = new byte[]{'e', 'x', 'p', 'e', 'c', 't', 'e', 'd'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_False() {
        byte[] buffer1 = new byte[]{'e', 'x', 'p', 'e', 'c', 't', 'e', 'd'};
        byte[] buffer2 = new byte[]{'e', 'x', 'p', 'e', 'c', 't', 'e', 'd'};
        byte[] buffer3 = new byte[]{'e', 'x', 'p', 'e', 'c', 't', 'e', 'd', 'a'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'e', 'x', 'p', 'e', 'c', 't', 'e', 'd'};
        byte[] buffer2 = null;
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, 0));
    }

    @Test
    public void testIsEqualWithNull_Buffer2() {
        byte[] buffer1 = new byte[]{'e', 'x', 'p', 'e', 'c', 't', 'e', 'd'};
        byte[] buffer2 = null;
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, 0));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
        assertFalse(ArchiveUtils.isArrayZero(a, 10));
    }

    @Test
    public void testSanitize() {
        String s = "test\u0001string";
        String sanitized = ArchiveUtils.sanitize(s);
        assertEquals("test??string", sanitized);
    }

}