package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new MockArchiveEntry("file.txt");
        assertEquals("-       0 file.txt\n", ArchiveUtils.toString(archiveEntry));
        ArchiveEntry archiveEntryDirectory = new MockArchiveEntry("dir/");
        assertEquals("d       0 dir/\n", ArchiveUtils.toString(archiveEntryDirectory));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferNull() {
        String expected = "Hello World";
        byte[] buffer = null;
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        byte[] buffer3 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer4 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', 'x'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));

        byte[] buffer5 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer6 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\u0000'};
        assertTrue(ArchiveUtils.isEqual(buffer5, 0, buffer5.length, buffer6, 0, buffer6.length, true));
    }

    @Test
    public void testIsEqualIgnoringTrailingNulls() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        byte[] buffer3 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer4 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\u0000'};
        assertTrue(ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length, true));

        byte[] buffer5 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer6 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', 'x'};
        assertFalse(ArchiveUtils.isEqual(buffer5, 0, buffer5.length, buffer6, 0, buffer6.length, true));
    }

    @Test
    public void testIsEqualNull() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = null;
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, 0, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, 0, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));

        byte[] a1 = new byte[]{'H'};
        assertFalse(ArchiveUtils.isArrayZero(a1, 0));

        byte[] a2 = new byte[]{'H', 'e', 'l', 'l', 'o'};
        assertFalse(ArchiveUtils.isArrayZero(a2, 0));

        byte[] a3 = new byte[]{'H', 'e', 'l', 'l', 'o', '\u0000'};
        assertTrue(ArchiveUtils.isArrayZero(a3, 5));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001string\u0012with\u0013special\u0014characters\u0015and\u0016long\u0017names";
        String expected = "test?string??with??special??characters??and??long??names";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }
}

class MockArchiveEntry implements ArchiveEntry {
    private final String name;

    public MockArchiveEntry(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public OutputStream setOutputStream(OutputStream outstream) throws IOException {
        throw new UnsupportedOperationException();
    }
}