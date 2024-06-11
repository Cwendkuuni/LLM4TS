package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ArchiveUtils.isArrayZero;
import static org.apache.commons.compress.utils.ArchiveUtils.isEqual;
import static org.apache.commons.compress.utils.ArchiveUtils.isEqualWithNull;
import static org.apache.commons.compress.utils.ArchiveUtils.matchAsciiBuffer;
import static org.apache.commons.compress.utils.ArchiveUtils.sanitize;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.test.util.TestFiles;
import org.apache.commons.compress.utils.ArchiveEntry;
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
        ArchiveEntry archiveEntry = TestFiles.getTestEntry("testfile.txt");
        String toString = ArchiveUtils.toString(archiveEntry);
        assertEquals("- 1024 testfile.txt", toString);

        archiveEntry = TestFiles.getTestDirectoryEntry("testdir");
        toString = ArchiveUtils.toString(archiveEntry);
        assertEquals("d     0 testdir/", toString);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBuffer() throws UnsupportedEncodingException {
        String expected = "HelloWorld";
        byte[] buffer = toAsciiBytes(expected);

        assertTrue(matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}
     */
    @Test
    public void testMatchAsciiBufferByteArray() throws UnsupportedEncodingException {
        String expected = "HelloWorld";
        byte[] buffer = toAsciiBytes(expected);

        assertTrue(matchAsciiBuffer(expected, buffer));
    }

    /**
     * Test method for {@link ArchiveUtils#toString(byte[])}
     */
    @Test
    public void testToStringByteArray() {
        byte[] bytes = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        String toString = ArchiveUtils.toAsciiString(bytes);
        assertEquals("HelloWorld", toString);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[], int, int)}
     */
    @Test
    public void testToAsciiStringByteArrayIntInt() {
        byte[] bytes = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        String toAsciiString = ArchiveUtils.toAsciiString(bytes, 0, bytes.length);
        assertEquals("HelloWorld", toAsciiString);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}
     */
    @Test
    public void testIsEqualByteArrayIntIntByteArrayIntIntBoolean() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        assertTrue(isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[])}
     */
    @Test
    public void testIsEqualByteArray() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        assertTrue(isEqual(buffer1, buffer2));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[])}
     */
    @Test
    public void testIsEqualByteArrayBoolean() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        assertTrue(isEqual(buffer1, buffer2, false));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqualWithNull(byte[], int, int, byte[], int, int)}
     */
    @Test
    public void testIsEqualWithNullByteArrayIntIntByteArrayIntInt() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', 0};
        assertTrue(isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}
     */
    @Test
    public void testIsArrayZeroByteArrayInt() {
        byte[] buffer = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        assertFalse(isArrayZero(buffer, 0, buffer.length));

        byte[] zeroBuffer = new byte[10];
        assertTrue(isArrayZero(zeroBuffer, 0, zeroBuffer.length));
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}
     */
    @Test
    public void testSanitize() {
        String s = "Hello\u0000World\u0001";
        String sanitize = ArchiveUtils.sanitize(s);
        assertEquals("Hello??World??", sanitize);
    }
}