package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Test;

/**
 * JUnit 4 test file for ArchiveUtils class.
 */
public class ArchiveUtilsTest {

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)}.
     */
    @Test
    public void testToString() {
        final ArchiveEntry entry = new ArchiveEntry("file.txt");
        final String expected = "-       1024 file.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));

        final ArchiveEntry directoryEntry = new ArchiveEntry("dir/");
        final String expectedDirectory = "d       0 dir/";
        assertEquals(expectedDirectory, ArchiveUtils.toString(directoryEntry));
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}.
     */
    @Test
    public void testMatchAsciiBuffer() {
        final byte[] buffer = "Hello World".getBytes(StandardCharsets.US_ASCII);
        final String expected = "Hello World";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}.
     */
    @Test
    public void testMatchAsciiBufferWithoutOffset() {
        final byte[] buffer = "Hello World".getBytes(StandardCharsets.US_ASCII);
        final String expected = "Hello World";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiBytes(String)}.
     */
    @Test
    public void testToAsciiBytes() {
        final String inputString = "Hello World";
        final byte[] expected = "Hello World".getBytes(StandardCharsets.US_ASCII);
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[])}.
     */
    @Test
    public void testToAsciiString() {
        final byte[] buffer = "Hello World".getBytes(StandardCharsets.US_ASCII);
        final String expected = "Hello World";
        assertEquals(expected, ArchiveUtils.toAsciiString(buffer));
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[], int, int)}.
     */
    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        final byte[] buffer = "Hello World".getBytes(StandardCharsets.US_ASCII);
        final int offset = 0;
        final int length = buffer.length;
        final String expected = "Hello World";
        assertEquals(expected, ArchiveUtils.toAsciiString(buffer, offset, length));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}.
     */
    @Test
    public void testIsEqual() {
        final byte[] buffer1 = "Hello World".getBytes(StandardCharsets.US_ASCII);
        final byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        final boolean expected = true;
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[])}.
     */
    @Test
    public void testIsEqualWithoutOffset() {
        final byte[] buffer1 = "Hello World".getBytes(StandardCharsets.US_ASCII);
        final byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        final boolean expected = true;
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[], boolean)}.
     */
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        final byte[] buffer1 = "Hello World".getBytes(StandardCharsets.US_ASCII);
        final byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', 0, 0, 0, 'W', 'o', 'r', 'l', 'd', 0, 0};
        final boolean expected = true;
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}.
     */
    @Test
    public void testIsArrayZero() {
        final byte[] buffer = new byte[10];
        final boolean expected = true;
        Arrays.fill(buffer, (byte) 0);
        assertTrue(ArchiveUtils.isArrayZero(buffer, 10));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqualWithNull(byte[], int, int, byte[], int, int)}.
     */
    @Test
    public void testIsEqualWithNull() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(
                "Hello World\0".getBytes(StandardCharsets.US_ASCII));
        final byte[] buffer1 = new byte[8];
        final int offset1 = 0;
        final int length1 = 6;
        final byte[] buffer2 = new byte[10];
        final int offset2 = 0;
        final int length2 = 8;
        final boolean expected = true;
        inputStream.read(buffer1, offset1, length1);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, offset1, length1, buffer2, offset2, length2));
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}.
     */
    @Test
    public void testSanitize() {
        final String input = "Hello World\u0001 \u001a\u001b\u0002";
        final String expected = "Hello World?? ???";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }
}