package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)}
     */
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        assertEquals("-       0 test.txt", result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = "Hello World".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer("Hello World", buffer, 0, buffer.length));
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}
     */
    @Test
    public void testMatchAsciiBufferStringByteArray() {
        byte[] buffer = "Hello World".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer("Hello World", buffer));
    }

    /**
     * Test method for {@link ArchiveUtils#toString(byte[], int, int)}
     */
    @Test
    public void testToStringByteArrayIntInt() {
        byte[] bytes = "Hello World".getBytes();
        assertEquals("Hello World", ArchiveUtils.toAsciiString(bytes, 0, bytes.length));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}
     */
    @Test
    public void testIsEqual() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[])}
     */
    @Test
    public void testIsEqualByteArrayByteArray() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[], boolean)}
     */
    @Test
    public void testIsEqualByteArrayByteArrayBoolean() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, false));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqualWithNull(byte[], int, int, byte[], int, int)}
     */
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = new byte[buffer1.length + 1];
        Arrays.fill(buffer2, (byte) 0);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}
     */
    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(buffer, 5));
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}
     */
    @Test
    public void testSanitize() {
        String result = ArchiveUtils.sanitize("test\u0001\u007FHello World");
        assertEquals("test???????Hello World", result);
    }
}