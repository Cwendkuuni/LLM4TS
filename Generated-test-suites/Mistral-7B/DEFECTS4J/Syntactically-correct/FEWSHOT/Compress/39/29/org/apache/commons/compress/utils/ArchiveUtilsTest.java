package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.CharsetNames;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        assertEquals("-      0 test.txt", ArchiveUtils.toString(archiveEntry));
        archiveEntry.setDirectory(true);
        assertEquals("d      0 test.txt", ArchiveUtils.toString(archiveEntry));

        final byte[] randomBytes = new byte[10];
        new java.util.Random().nextBytes(randomBytes);
        ArchiveEntry randomArchiveEntry = new ArchiveEntry("random.bin");
        randomArchiveEntry.setSize(randomBytes.length);
        randomArchiveEntry.setInputStream(new java.io.ByteArrayInputStream(randomBytes));
        assertEquals("-     " + randomBytes.length + " random.bin", ArchiveUtils.toString(randomArchiveEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World!";
        byte[] buffer = new byte[13];
        Arrays.fill(buffer, (byte) 'H');
        Arrays.fill(buffer, 5, 13, (byte) 'W');
        Arrays.fill(buffer, 11, 13, (byte) '!');
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferNull() {
        String expected = null;
        byte[] buffer = new byte[5];
        assertNull(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferEmpty() {
        String expected = "";
        byte[] buffer = new byte[5];
        assertNull(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferWrongLength() {
        String expected = "Hello World!";
        byte[] buffer = new byte[13];
        Arrays.fill(buffer, (byte) 'H');
        Arrays.fill(buffer, 5, 13, (byte) 'W');
        Arrays.fill(buffer, 13, buffer.length, (byte) '!'); // wrong length
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferCaseSensitive() {
        String expected = "Hello World!";
        byte[] buffer = new byte[13];
        Arrays.fill(buffer, (byte) 'h');
        Arrays.fill(buffer, 5, 13, (byte) 'W');
        Arrays.fill(buffer, 11, 13, (byte) '!');
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello World!";
        byte[] expected = new byte[13];
        Arrays.fill(expected, (byte) 'H');
        Arrays.fill(expected, 5, 13, (byte) 'W');
        Arrays.fill(expected, 11, 13, (byte) '!');
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[] { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 };
        String expected = "Hello World!";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringOffsetLength() {
        byte[] inputBytes = new byte[] { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 };
        String expected = "Hello World!";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        byte[] buffer2 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        byte[] buffer3 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16 };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[] { 1, 2, 3, 4, 5, 0, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0 };
        byte[] buffer2 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualNull() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[] { 1, 2, 3, 4, 5 };
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 0, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualOffsetLengthNull() {
        byte[] buffer1 = new byte[] { 1, 2, 3, 4, 5 };
        byte[] buffer2 = null;
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, 0));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualOffsetLengthDifferentLengths() {
        byte[] buffer1 = new byte[] { 1, 2, 3, 4, 5 };
        byte[] buffer2 = new byte[] { 1, 2, 3, 4, 5, 6 };
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0 };
        byte[] buffer2 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, null, null, null, null, null, null };
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
        assertFalse(ArchiveUtils.isArrayZero(a, 6));
    }

    @Test
    public void testSanitize() {
        String s = "Hello 世界！你好！";
        String sanitized = ArchiveUtils.sanitize(s);
        assertEquals("Hello ?????????? ??????????", sanitized);
    }
}