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
        ArchiveEntry entry = new MockArchiveEntry("test.txt", true, 10);
        assertEquals("-       10 test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = "test".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBuffer_false() {
        byte[] buffer = "test".getBytes(CharsetNames.UTF_8);
        assertFalse(ArchiveUtils.matchAsciiBuffer("test", buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer("test", buffer, 0, buffer.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls_true() {
        byte[] buffer1 = "test\0".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls_false() {
        byte[] buffer1 = "test\0".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_notEqual() {
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test2".getBytes(CharsetNames.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_offset() {
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test2".getBytes(CharsetNames.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, 1, buffer1.length - 1, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqual_length() {
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test2".getBytes(CharsetNames.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length - 1, false));
    }

    @Test
    public void testIsEqual_equal() {
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testSanitize() {
        assertEquals("test\000\u0002\u001A\u000E?\u0000?\u0003?\u0001?\u0005?\u0004?\u0019", ArchiveUtils.sanitize("test\u0002\u001A\u000E\u0000\u0003\u0001\u0005\u0004\u0019"));
    }

}

class MockArchiveEntry extends ArchiveEntry {
    public MockArchiveEntry(String name, boolean isDirectory, long size) {
        super(name, isDirectory, size);
    }
}