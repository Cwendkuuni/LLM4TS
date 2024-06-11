package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
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
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String output = ArchiveUtils.toString(entry);
        String expected = "-        0 test.txt";
        assertEquals(expected, output);

        entry.setDirectory(true);
        output = ArchiveUtils.toString(entry);
        expected = "d        0 test.txt";
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    /**
     * Test method for {@link ArchiveUtils#toString(byte[], int, int)}
     */
    @Test
    public void testToAsciiString() {
        byte[] buffer = toAsciiBytes("test".getBytes());
        String output = ArchiveUtils.toAsciiString(buffer);
        String expected = "test";
        assertEquals(expected, output);

        buffer = new byte[]{'t', 'e', 's', 't'};
        output = ArchiveUtils.toAsciiString(buffer, 0, buffer.length);
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}
     */
    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        buffer1 = new byte[]{'t', 'e', 's', 't'};
        buffer2 = new byte[]{'t', 'e', 's', 't', 't'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        buffer1 = new byte[]{'t', 'e', 's', 't'};
        buffer2 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}
     */
    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(buffer, 5));
        assertTrue(ArchiveUtils.isArrayZero(buffer, 10));
        assertFalse(ArchiveUtils.isArrayZero(buffer, 3));
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}
     */
    @Test
    public void testSanitize() {
        String input = "test\u0001\u007Ftest\ufffdtest";
        String output = ArchiveUtils.sanitize(input);
        String expected = "test????test?????";
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}
     */
    @Test
    public void testMatchAsciiBufferStringByteArray() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(byte[], int, int, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBufferByteArrayIntIntByteArrayIntInt() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.matchAsciiBuffer(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        byte[] buffer3 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer4 = new byte[]{'t', 'e', 's', 't', 't'};
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length, false));

        byte[] buffer5 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer6 = new byte[]{'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer5, 0, buffer5.length, buffer6, 0, buffer6.length, true));
    }
}