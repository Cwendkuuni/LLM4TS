package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(1024);
        entry.setDirectory(false);

        assertEquals("- 1024 test.txt", ArchiveUtils.toString(entry));
        entry.setDirectory(true);
        assertEquals("d 1024 test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "foo";
        byte[] buffer = new byte[3];
        buffer[0] = (byte) 'f';
        buffer[1] = (byte) 'o';
        buffer[2] = (byte) 'o';

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer("bar", buffer, 0, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, new byte[3], 0, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, new byte[3], 1, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, new byte[4], 0, 3));
    }

    @Test
    public void testToAsciiBytes() {
        String expected = "foo";
        byte[] bytes = ArchiveUtils.toAsciiBytes(expected);

        assertEquals(3, bytes.length);
        for (int i = 0; i < 3; i++) {
            assertEquals(expected.charAt(i), (char) bytes[i]);
        }
    }

    @Test
    public void testToAsciiString() {
        byte[] bytes = new byte[3];
        bytes[0] = (byte) 'f';
        bytes[1] = (byte) 'o';
        bytes[2] = (byte) 'o';

        assertEquals("foo", ArchiveUtils.toAsciiString(bytes));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { (byte) 'f', (byte) 'o', (byte) 'o' };
        byte[] buffer2 = new byte[] { (byte) 'f', (byte) 'o', (byte) 'o' };
        byte[] buffer3 = new byte[] { (byte) 'f', (byte) 'o', (byte) 'a' };

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 3, buffer3, 0, 3, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 4, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 3, false));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 3, true));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer3, 0, 3, true));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 4, true));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 3, true));

        byte[] buffer4 = new byte[3];
        byte[] buffer5 = new byte[3];
        assertArrayEquals(buffer1, buffer5);
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 3, buffer5, 0, 3, false));

        byte[] buffer6 = new byte[1];
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 3, buffer6, 0, 1, false));

        byte[] buffer7 = new byte[4];
        Arrays.fill(buffer7, (byte) 0);
        assertTrue(ArchiveUtils.isArrayZero(buffer7, 4));
    }

    @Test
    public void testSanitize() {
        assertEquals("foo", ArchiveUtils.sanitize("foo"));
        assertEquals("f?o?o?", ArchiveUtils.sanitize("foo\u0001\u0002"));
        assertEquals("foo?", ArchiveUtils.sanitize("foo\uFFFD"));
        assertEquals("", ArchiveUtils.sanitize(""));
        assertEquals("", ArchiveUtils.sanitize(null));
    }
}