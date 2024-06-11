package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new MockArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        assertEquals("- 2000 main.c", ArchiveUtils.toString(entry));

        ArchiveEntry directoryEntry = new MockArchiveEntry("testfiles/");
        directoryEntry.setDirectory(true);
        directoryEntry.setSize(100);

        assertEquals("d 100 testfiles/", ArchiveUtils.toString(directoryEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "foo";
        byte[] buffer = "foo".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        buffer = new byte[3];
        System.arraycopy(expected.getBytes(), 0, buffer, 0, 3);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        expected += " bar";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferNull() {
        assertFalse(ArchiveUtils.matchAsciiBuffer(null, null));
        assertFalse(ArchiveUtils.matchAsciiBuffer(null, new byte[0]));
        assertFalse(ArchiveUtils.matchAsciiBuffer("foo", null));
    }

    @Test
    public void testToAsciiBytes() {
        String expected = "foo";
        byte[] bytes = ArchiveUtils.toAsciiBytes(expected);

        assertArrayEquals(expected.getBytes(), bytes);
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
    public void testToAsciiStringNull() {
        assertNull(ArchiveUtils.toAsciiString(null));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { 'f', 'o', 'o' };
        byte[] buffer2 = new byte[] { 'f', 'o', 'o' };

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3, false));

        byte[] buffer3 = new byte[] { 'f', 'o', 'o', 'b' };

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 3, buffer3, 0, 4, false));
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[] { 'f', 'o', 'o', 0 };
        byte[] buffer2 = new byte[] { 'f', 'o', 'o' };

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 3, false));

        byte[] buffer3 = new byte[] { 'f', 'o', 'o', 'b' };

        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer3, 0, 4, false));

        byte[] buffer4 = new byte[] { 'f', 'o', 'o', 'b', 0 };

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer4, 0, 5, false));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));

        byte[] a1 = new byte[] { 0 };
        assertTrue(ArchiveUtils.isArrayZero(a1, 1));

        byte[] a2 = new byte[] { 1, 1, 1 };
        assertFalse(ArchiveUtils.isArrayZero(a2, 3));
    }

    @Test
    public void testSanitize() {
        String input = "foo\u0001bar\u007f";
        String output = ArchiveUtils.sanitize(input);

        assertEquals("foo?bar??", output);
    }

    @Test
    public void testSanitizeEmptyString() {
        String input = "";
        String output = ArchiveUtils.sanitize(input);

        assertEquals("", output);
    }

}

class MockArchiveEntry implements ArchiveEntry {

    private final String name;
    private final boolean isDirectory;
    private final long size;

    public MockArchiveEntry(String name) {
        this.name = name;
        this.isDirectory = false;
        this.size = -1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean isDirectory() {
        return isDirectory;
    }

}