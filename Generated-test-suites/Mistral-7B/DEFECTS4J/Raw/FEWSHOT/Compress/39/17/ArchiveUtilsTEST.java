package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(1024);
        assertEquals("- 1024 test.txt", ArchiveUtils.toString(entry));
        entry.setDirectory(true);
        assertEquals("d 1024 test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        final String expected = "Hello World";
        final byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferWithInvalidEncoding() {
        String expected = "Hello World";
        byte[] buffer = new byte[expected.length()];
        try {
            expected = new String(buffer, CharsetNames.US_ASCII);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToStringAsciiBytes() {
        String input = "Hello World";
        byte[] inputBytes = toAsciiBytes(input);
        assertEquals(input, ArchiveUtils.toAsciiString(inputBytes));
        assertEquals(input, ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));
    }

    @Test
    public void testToStringAsciiBytesWithInvalidEncoding() {
        byte[] inputBytes = new byte[10];
        String expected = "Invalid encoding";
        try {
            String result = ArchiveUtils.toAsciiString(inputBytes);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expected, e.getMessage());
        }

        try {
            String result = ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[5];
        byte[] buffer2 = new byte[5];
        Arrays.fill(buffer1, (byte) 0x01);
        Arrays.fill(buffer2, (byte) 0x01);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        Arrays.fill(buffer2, (byte) 0x02);
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
        // Test with offset and length
        Arrays.fill(buffer1, 1, 5, (byte) 0x02);
        Arrays.fill(buffer2, 1, 5, (byte) 0x01);
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 1, 5, 5));
        Arrays.fill(buffer2, 1, 5, (byte) 0x02);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, 1, 5, 5));

        // Test with ignoreTrailingNulls = true
        Arrays.fill(buffer1, 1, 5, (byte) 0x02);
        Arrays.fill(buffer2, 1, 5, (byte) 0x01);
        Arrays.fill(buffer1, 5, 10, (byte) 0x00);
        Arrays.fill(buffer2, 5, 10, (byte) 0x00);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithInvalidEncoding() {
        String expected = "Invalid encoding";
        byte[] buffer1 = toAsciiBytes("Hello World".getBytes());
        byte[] buffer2 = new byte[11];
        try {
            ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expected, e.getMessage());
        }

        try {
            ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[5];
        byte[] buffer2 = null;
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
        assertFalse(ArchiveUtils.isArrayZero(a, 6));
    }

    @Test
    public void testSanitize() {
        String input = "This\0is\0a\0test\nwith\nbad\ncharacters\tand\r\ftrailing\nstuff.";
        String expected = "This?is?a?test?with?bad?characters?and?trailing?stuff.";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }
}