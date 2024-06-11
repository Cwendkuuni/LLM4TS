package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ByteUtils;
import org.junit.Test;

/**
 * TarUtilsTest class contains test cases for TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test method for {@link TarUtils#parseOctal(byte[], int, int)}.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '5', '5', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017755555L, result);
    }

    /**
     * Test method for {@link TarUtils#parseOctal(byte[], int, int)}.
     * Verify that an IllegalArgumentException is thrown when the length is less than 2.
     */
    @Test
    public void testParseOctalInvalidLength() {
        byte[] buffer = new byte[]{'0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /**
     * Test method for {@link TarUtils#parseOctalOrBinary(byte[], int, int)}.
     */
    @Test
    public void testParseOctalOrBinaryBinary() {
        byte[] buffer = new byte[]{0x80, (byte) 0x7F, (byte) 0x80, 0x01, 0x02, 0x03};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x807F80010203L, result);
    }

    /**
     * Test method for {@link TarUtils#parseBoolean(byte[], int)}.
     */
    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1};
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    /**
     * Test method for {@link TarUtils#parseBoolean(byte[], int)}.
     * Verify that an IllegalArgumentException is thrown when the input is not 1.
     */
    @Test
    public void testParseBooleanInvalidInput() {
        byte[] buffer = new byte[]{0};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(buffer, 0));
    }

    /**
     * Test method for {@link TarUtils#parseName(byte[], int, int)}.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar file", result);
    }

    /**
     * Test method for {@link TarUtils#formatNameBytes(String, byte[], int, int)}.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "test.txt";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'t', 'e', 's', 't', '.', 't', 'x', 't', 0, 0};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    /**
     * Test method for {@link TarUtils#formatUnsignedOctalString(long, byte[], int, int)}.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0123456L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', 0};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    /**
     * Test method for {@link TarUtils#formatOctalBytes(long, byte[], int, int)}.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 0123456L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', ' ', '0', 0};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    /**
     * Test method for {@link TarUtils#parseOctal(byte[], int, int)}.
     * Verify that an IllegalArgumentException is thrown when the trailing space/NUL is missing.
     */
    @Test
    public void testParseOctalMissingTrailing() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '5', '5', '5', '5'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /**
     * Test method for {@link TarUtils#parseOctal(byte[], int, int)}.
     * Verify that an IllegalArgumentException is thrown when an invalid byte is detected.
     */
    @Test
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '5', '5', '5', 'X', '5'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /**
     * Test method for {@link TarUtils#computeCheckSum(byte[])}.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.'};
        long checksum = TarUtils.computeCheckSum(buffer);
        // Checksum for this test case is 18221
        assertEquals(18221, checksum);
    }
}