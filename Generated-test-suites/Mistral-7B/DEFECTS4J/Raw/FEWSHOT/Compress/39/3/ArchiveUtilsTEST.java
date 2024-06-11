package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        final ArchiveEntry entry = new DummyArchiveEntry("test.txt", "testContent", false, 123);
        final String expected = "-- 123 test.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testToStringWithDirectoryEntry() {
        final ArchiveEntry entry = new DummyArchiveEntry("test/", null, true, 0);
        final String expected = "d-- 0 test/";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        final String expected = "expected";
        final byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferFalse() {
        final String expected = "expected";
        final byte[] buffer = new byte[]{'a', 'b', 'c'};
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testIsEqual() {
        final byte[] buffer1 = toAsciiBytes("expected1");
        final byte[] buffer2 = toAsciiBytes("expected2");
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, false));

        final byte[] buffer3 = Arrays.copyOfRange(buffer1, 0, 3);
        final byte[] buffer4 = Arrays.copyOfRange(buffer2, 0, 3);
        assertTrue(ArchiveUtils.isEqual(buffer3, buffer4, false));

        final byte[] buffer5 = Arrays.copyOfRange(buffer1, 0, 4);
        final byte[] buffer6 = Arrays.copyOfRange(buffer2, 3, 7);
        assertFalse(ArchiveUtils.isEqual(buffer5, buffer6, false));

        final byte[] buffer7 = Arrays.copyOfRange(buffer1, 0, 4);
        final byte[] buffer8 = Arrays.copyOfRange(buffer2, 0, 4);
        assertTrue(ArchiveUtils.isEqual(buffer7, buffer8, false));

        final byte[] buffer9 = Arrays.copyOfRange(buffer1, 0, 4);
        final byte[] buffer10 = Arrays.copyOfRange(buffer2, 0, 4);
        assertTrue(ArchiveUtils.isEqual(buffer9, buffer10, true));
    }

    @Test
    public void testIsEqualWithNull() {
        final byte[] buffer1 = new byte[0];
        final byte[] buffer2 = new byte[0];
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer2, 0, buffer2.length));

        final byte[] buffer3 = new byte[0];
        final byte[] buffer4 = new byte[1];
        assertFalse(ArchiveUtils.isEqualWithNull(buffer3, buffer4, 0, buffer4, 0, buffer4.length));

        final byte[] buffer5 = new byte[1];
        final byte[] buffer6 = new byte[0];
        assertFalse(ArchiveUtils.isEqualWithNull(buffer5, buffer6, 0, buffer6, 0, buffer6.length));

        final byte[] buffer7 = new byte[]{'a'};
        final byte[] buffer8 = new byte[0];
        assertFalse(ArchiveUtils.isEqualWithNull(buffer7, buffer8, 0, buffer8, 0, buffer8.length));

        final byte[] buffer9 = new byte[]{'a'};
        final byte[] buffer10 = new byte[]{'b'};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer9, buffer10, 0, buffer10, 0, buffer10.length));

        final byte[] buffer11 = new byte[]{'a', 'b'};
        final byte[] buffer12 = new byte[]{'a', 'b'};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer11, buffer12, 0, buffer12, 0, buffer12.length));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        final byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        final byte[] buffer2 = new byte[]{'a', 'b', 'c'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 1, 2, buffer2, 1, 2, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 2, buffer2, 1, 2, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 2, 1, buffer2, 1, 2, false));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length - 1, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length - 2, false));

        final byte[] buffer3 = new byte[]{'a', 'b', 'c'};
        final byte[] buffer4 = new byte[]{'a', 'b', 'd'};
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length - 1, false));
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 1, buffer4.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 1, buffer4.length - 1, false));
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 1, buffer4.length - 2, false));
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 2, buffer4.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer3, 1, buffer3.length - 1, buffer4, 0, buffer4.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer3, 1, buffer3.length - 1, buffer4, 1, buffer4.length - 1, false));
        assertFalse(ArchiveUtils.isEqual(buffer3, 1, buffer3.length - 1, buffer4, 1, buffer4.length - 2, false));
    }

    @Test
    public void testIsEqualWithNullOffsetOrLength() {
        final byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        final byte[] buffer2 = new byte[]{'a', 'b', 'c'};

        assertTrue(ArchiveUtils.isEqual(buffer1, -1, buffer1.length, buffer2, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, -1, buffer2, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, -1, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, -1, false));

        assertFalse(ArchiveUtils.isEqual(buffer1, -1, buffer1.length, buffer2, 0, buffer2.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, -1, buffer2.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, -1, true));
    }

    @Test
    public void testIsArrayZero() {
        final byte[] buffer = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(buffer, 0));
        assertTrue(ArchiveUtils.isArrayZero(buffer, buffer.length));

        final byte[] buffer2 = new byte[]{'a'};
        assertFalse(ArchiveUtils.isArrayZero(buffer2, 0));
        assertFalse(ArchiveUtils.isArrayZero(buffer2, buffer2.length));

        final byte[] buffer3 = new byte[]{'a', 'b', 'c'};
        assertFalse(ArchiveUtils.isArrayZero(buffer3, 0));
        assertFalse(ArchiveUtils.isArrayZero(buffer3, buffer3.length));

        final byte[] buffer4 = new byte[]{'a', 'b', 'c', 0};
        assertFalse(ArchiveUtils.isArrayZero(buffer4, 0));
        assertFalse(ArchiveUtils.isArrayZero(buffer4, buffer4.length));

        final byte[] buffer5 = new byte[]{'a', 'b', 'c', 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(buffer5, 2));
        assertTrue(ArchiveUtils.isArrayZero(buffer5, buffer5.length));
    }

    @Test
    public void testSanitize() {
        final String input = "test\u0000string";
        final String expected = "test????string";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitizeWithNull() {
        assertNull(ArchiveUtils.sanitize(null));
    }
}

class DummyArchiveEntry implements ArchiveEntry {

    private final String name;
    private final byte[] content;
    private final boolean isDirectory;
    private final long size;

    public DummyArchiveEntry(String name, String content, boolean isDirectory, long size) {
        this.name = name;
        this.content = toAsciiBytes(content);
        this.isDirectory = isDirectory;
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] getInputStream() throws IOException {
        throw new UnsupportedOperationException();
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