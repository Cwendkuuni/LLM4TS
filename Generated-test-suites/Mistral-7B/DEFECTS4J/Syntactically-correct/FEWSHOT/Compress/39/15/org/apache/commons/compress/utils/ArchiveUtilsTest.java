package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setDirectory(false);

        assertEquals("-      10 test.txt", ArchiveUtils.toString(entry));
        entry.setDirectory(true);
        assertEquals("d-      10 test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World";
        byte[] buffer = toAsciiBytes(expected);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferFalse() {
        String expected = "Hello World";
        byte[] buffer = new byte[10];

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiString() {
        String input = "Hello World";
        byte[] inputBytes = toAsciiBytes(input);

        assertEquals(input, ArchiveUtils.toAsciiString(inputBytes));
        assertEquals(input, ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] {0x68, 0x65, 0x6C, 0x6C, 0x6F};
        byte[] buffer2 = new byte[] {0x68, 0x65, 0x6C, 0x6C, 0x6F};
        byte[] buffer3 = new byte[] {0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x01};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));

        byte[] buffer4 = new byte[5];
        byte[] buffer5 = new byte[5];

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer4, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer4, false));

        assertTrue(ArchiveUtils.isArrayZero(new byte[0], 0));
        assertFalse(ArchiveUtils.isArrayZero(new byte[1], 0));
    }

    @Test
    public void testSanitize() {
        String s = "test\u0001\u007Ffile";
        String sanitized = ArchiveUtils.sanitize(s);

        assertEquals("test????file", sanitized);
    }
}