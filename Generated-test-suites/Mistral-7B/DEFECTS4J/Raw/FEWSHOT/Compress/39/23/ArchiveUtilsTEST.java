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
        expected = "d 100 testfiles";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "HelloWorld";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);

        // Test case with different buffer and expected string
        String differentExpected = "HelloThere";
        byte[] differentBuffer = toAsciiBytes(expected);

        result = ArchiveUtils.matchAsciiBuffer(expected, differentBuffer);
        assertFalse(result);

        // Test case with empty string
        String emptyExpected = "";
        assertFalse(ArchiveUtils.matchAsciiBuffer(emptyExpected, buffer));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = toAsciiBytes("HelloWorld");
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "HelloWorld";
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertTrue(result);

        // Test case with different buffers
        byte[] differentBuffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] differentBuffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'D'};

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, differentBuffer2, 0, differentBuffer2.length, false);
        assertFalse(result);

        // Test case with ignored trailing nulls
        byte[] buffer1WithTrailingNull = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', 0};
        byte[] buffer2WithTrailingNull = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};

        result = ArchiveUtils.isEqualWithNull(buffer1WithTrailingNull, 0, buffer1WithTrailingNull.length, buffer2WithTrailingNull, 0, buffer2WithTrailingNull.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = null;

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, 0, false));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, 0, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];

        assertTrue(ArchiveUtils.isArrayZero(array, 5));
        assertFalse(ArchiveUtils.isArrayZero(array, 6));
    }

    @Test
    public void testSanitize() {
        String input = "HelloWorld! ＡＢＣ";
        String expected = "HelloWorld! ？？？";
        String result = ArchiveUtils.sanitize(input);

        assertEquals(expected, result);
    }
}

class MockArchiveEntry implements ArchiveEntry {
    private String name;
    private long size;
    private boolean isDirectory;

    public MockArchiveEntry(String name) {
        this.name = name;
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

    public void setSize(long size) {
        this.size = size;
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }
}