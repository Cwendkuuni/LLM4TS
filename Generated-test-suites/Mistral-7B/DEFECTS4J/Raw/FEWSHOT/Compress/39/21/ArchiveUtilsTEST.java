package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
        archiveEntry.setSize(2000L);
        archiveEntry.setDirectory(false);

        assertEquals("- 2000 main.c", ArchiveUtils.toString(archiveEntry));

        ArchiveEntry archiveEntryDirectory = new ArchiveEntry("testfiles");
        archiveEntryDirectory.setSize(100L);
        archiveEntryDirectory.setDirectory(true);

        assertEquals("d 100 testfiles", ArchiveUtils.toString(archiveEntryDirectory));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello";
        byte[] buffer = toAsciiBytes(expected);

        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', '.'};

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2, 0, buffer2.length));

        assertFalse(ArchiveUtils.matchAsciiBuffer("hello", buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer("world", buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer("hello", buffer, 1, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer("hello", buffer1, 1, 3));
    }

    @Test
    public void testToAsciiBytesAndToString() {
        String expected = "hello";
        byte[] bytes = ArchiveUtils.toAsciiBytes(expected);
        String result = ArchiveUtils.toAsciiString(bytes);

        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o', '.'};

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, 0, buffer3.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, 0, buffer3.length + 1));

        byte[] buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer5 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer6 = new byte[]{'h', 'e', 'l', 'l', 'o', '.'};
        assertFalse(ArchiveUtils.isEqual(buffer4, buffer5, 1, 3));
        assertFalse(ArchiveUtils.isEqual(buffer4, buffer5, 1, 3 + 1));
        assertFalse(ArchiveUtils.isEqual(buffer4, buffer6, 1, 3));
        assertFalse(ArchiveUtils.isEqual(buffer4, buffer6, 1, 3 + 1));

        byte[] buffer7 = new byte[]{1, 2, 3};
        byte[] buffer8 = new byte[]{3, 2, 1};
        assertFalse(ArchiveUtils.isEqual(buffer7, buffer8, 0, buffer8.length));
        assertFalse(ArchiveUtils.isEqual(buffer7, buffer8, 0, buffer8.length + 1));

        byte[] buffer9 = new byte[]{0, 0, 0, 0, 0};
        assertTrue(ArchiveUtils.isEqual(buffer9, buffer9, 0, buffer9.length));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o', '.'};

        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer2.length));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer2.length + 1));

        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer3, 0, buffer3.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer3, 0, buffer3.length + 1));

        byte[] buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer5 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer6 = new byte[]{'h', 'e', 'l', 'l', 'o', '.'};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer4, buffer5, 1, 3));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer4, buffer5, 1, 3 + 1));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer4, buffer6, 1, 3));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer4, buffer6, 1, 3 + 1));
    }

    @Test
    public void testSanitize() {
        String input = "abc\u0001def\u0123\u0F00ghij\u01FF";
        String expectedOutput = "abc?def?\u0123?\u0F00ghij?";

        String result = ArchiveUtils.sanitize(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testIsArrayZeroWithSubArray() {
        byte[] a = new byte[5];
        assertFalse(ArchiveUtils.isArrayZero(a, 3));
        assertTrue(ArchiveUtils.isArrayZero(a, 3, 2));
    }
}