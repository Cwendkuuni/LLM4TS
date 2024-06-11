package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

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
        String expected = "-       0 test.txt";
        assertEquals(expected, result);

        ArchiveEntry directoryEntry = new ArchiveEntry("test_dir/");
        result = ArchiveUtils.toString(directoryEntry);
        expected = "d       0 test_dir/";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBufferStringByteArrayIntInt() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}.
     */
    @Test
    public void testMatchAsciiBufferStringByte() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiBytes(String)}
     */
    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = Arrays.copyOfRange(CharsetNames.US_ASCII.encode(inputString), 0, CharsetNames.US_ASCII.encode(inputString).length);
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[])}
     */
    @Test
    public void testToAsciiStringByteArray() {
        byte[] inputBytes = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[], int, int)}
     */
    @Test
    public void testToAsciiStringByteArrayIntInt() {
        byte[] inputBytes = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}
     */
    @Test
    public void testIsEqualByteArrayIntIntByteArrayIntIntBoolean() {
        byte[] buffer1 = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        byte[] buffer2 = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int)}
     */
    @Test
    public void testIsEqualByteArrayIntIntByteArrayIntInt() {
        byte[] buffer1 = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        byte[] buffer2 = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[])}
     */
    @Test
    public void testIsEqualByteArrayByteArray() {
        byte[] buffer1 = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        byte[] buffer2 = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[], boolean)}
     */
    @Test
    public void testIsEqualByteArrayByteArrayBoolean() {
        byte[] buffer1 = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        byte[] buffer2 = Arrays.copyOfRange(CharsetNames.US_ASCII.encode("test".getBytes()), 0, CharsetNames.US_ASCII.encode("test".getBytes()).length);
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}
     */
    @Test
    public void testIsArrayZeroByteArrayInt() {
        byte[] buffer = new byte[10];
        boolean result = ArchiveUtils.isArrayZero(buffer, 5);
        assertFalse(result);

        buffer = new byte[10];
        Arrays.fill(buffer, (byte) 0);
        result = ArchiveUtils.isArrayZero(buffer, 10);
        assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}
     */
    @Test
    public void testSanitize() {
        String s = "\u0000test\u0001";
        String result = ArchiveUtils.sanitize(s);
        String expected = "??test???";
        assertEquals(expected, result);
    }
}