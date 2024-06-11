package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new MockArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, result);

        ArchiveEntry directoryEntry = new MockArchiveEntry("testfiles");
        directoryEntry.setSize(100);
        directoryEntry.setDirectory(true);

        result = ArchiveUtils.toString(directoryEntry);
        expected = "d 100 testfiles/";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expected";
        byte[] buffer = toAsciiBytes(expected);

        byte[] buffer1 = Arrays.copyOfRange(buffer, 0, 5);
        byte[] buffer2 = Arrays.copyOfRange(buffer, 5, buffer.length);

        // test with different lengths
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1, 0, buffer1.length));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer1, 0, buffer2.length));

        // test with offset
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2, 5, buffer2.length));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer2, 0, buffer1.length));

        // test with null buffer
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, null));
    }

    @Test
    public void testToAsciiString() {
        String input = "input";
        byte[] inputBytes = toAsciiBytes(input);

        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(input, result);

        result = ArchiveUtils.toAsciiString(inputBytes, 5, input.length() - 5);
        assertEquals(input.substring(5), result);

        // test with null input bytes
        assertNull(ArchiveUtils.toAsciiString(null));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', 'D'};
        byte[] buffer2 = new byte[]{'A', 'B', 'C', 'D'};
        byte[] buffer3 = new byte[]{'A', 'B', 'C', 'E'};

        // test with same buffers and same offsets and lengths
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        // test with different buffers and same contents
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertTrue(ArchiveUtils.isEqual(buffer2, buffer1));

        // test with different buffers and different contents
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
        assertFalse(ArchiveUtils.isEqual(buffer3, buffer1));

        // test with null buffers
        assertFalse(ArchiveUtils.isEqual(null, null));
        assertFalse(ArchiveUtils.isEqual(null, buffer1));
        assertFalse(ArchiveUtils.isEqual(buffer1, null));

        // test with ignoreTrailingNulls = true
        byte[] buffer4 = new byte[]{'A', 'B', 'C', 0, 0, 0};
        byte[] buffer5 = new byte[]{'A', 'B', 'C'};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer4, 0, buffer4.length, buffer5, 0, buffer5.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        byte[] b = new byte[10];
        Arrays.fill(b, (byte) 0);
        assertTrue(ArchiveUtils.isArrayZero(a, 0));
        assertTrue(ArchiveUtils.isArrayZero(a, a.length));
        assertTrue(ArchiveUtils.isArrayZero(b, 0));
        assertTrue(ArchiveUtils.isArrayZero(b, b.length));
        assertFalse(ArchiveUtils.isArrayZero(b, 5));
    }

    @Test
    public void testSanitize() {
        String s1 = "This is a \u0001test\u0002string\u0003with\nnon-printable\u001fcharacters.";
        String s2 = ArchiveUtils.sanitize(s1);
        String expected = "This is a ?test?string?with?non-printable?characters.";
        assertEquals(expected, s2);
    }
}

class MockArchiveEntry implements ArchiveEntry {
    private final String name;
    private final boolean isDirectory;
    private final long size;

    public MockArchiveEntry(String name, boolean isDirectory, long size) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public long getSize() {
        return size;
    }
}