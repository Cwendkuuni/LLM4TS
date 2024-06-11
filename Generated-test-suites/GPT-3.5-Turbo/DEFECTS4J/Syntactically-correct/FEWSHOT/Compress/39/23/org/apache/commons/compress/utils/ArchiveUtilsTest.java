package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntryImpl("file.txt", false, 1000);
        assertEquals("-    1000 file.txt", ArchiveUtils.toString(entry));

        entry = new ArchiveEntryImpl("dir", true, 0);
        assertEquals("d          0 dir", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        assertTrue(ArchiveUtils.matchAsciiBuffer("foo", "foo".getBytes()));
        assertTrue(ArchiveUtils.matchAsciiBuffer("foo", "foobar".getBytes(), 0, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer("foo", "foobar".getBytes(), 0, 4));
    }

    @Test
    public void testToAsciiBytes() {
        try {
            byte[] expected = "foo".getBytes("US-ASCII");
            byte[] result = ArchiveUtils.toAsciiBytes("foo");
            assertArrayEquals(expected, result);
        } catch (UnsupportedEncodingException e) {
            fail();
        }
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = { 102, 111, 111 };
        String expected = "foo";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = { 102, 111, 111 };
        byte[] buffer2 = { 102, 111, 111 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        buffer2 = new byte[] { 98, 97, 114 };
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = { 102, 111, 111, 0, 0, 0 };
        byte[] buffer2 = { 102, 111, 111 };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3, true));

        buffer2 = new byte[] { 102, 111, 111 };
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 6, buffer2, 0, 3, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = { 0, 0, 0 };
        assertTrue(ArchiveUtils.isArrayZero(array, 3));

        array = new byte[] { 0, 0, 1 };
        assertFalse(ArchiveUtils.isArrayZero(array, 3));
    }

    @Test
    public void testSanitize() {
        String s = "abcd";
        assertEquals("abcd", ArchiveUtils.sanitize(s));

        s = "abc\ndef";
        assertEquals("abc?def", ArchiveUtils.sanitize(s));
    }

    private static class ArchiveEntryImpl implements ArchiveEntry {
        private final String name;
        private final boolean directory;
        private final long size;

        public ArchiveEntryImpl(String name, boolean directory, long size) {
            this.name = name;
            this.directory = directory;
            this.size = size;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isDirectory() {
            return directory;
        }

        @Override
        public long getSize() {
            return size;
        }
    }
}