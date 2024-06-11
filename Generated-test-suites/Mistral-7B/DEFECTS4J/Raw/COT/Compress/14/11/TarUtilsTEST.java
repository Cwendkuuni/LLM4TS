package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

/**
 * TarUtils Test Case.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '0', '1', '2'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567120L, result);
    }

    /**
     * Test parseOctal method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', 'x', '6', '7', '8', '9', ' ', '0', '1'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method.
     */
    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{(byte) 0x81, 0x01, 0x02, 0x03, 0x04, 0x05};
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(192105L, result1);

        byte[] buffer2 = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '0', '1'};
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(01234567120L, result2);
    }

    /**
     * Test parseOctalOrBinary method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', 'x', '6', '7', '8', '9', ' ', '0', '1'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method.
     */
    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{0x01};
        boolean result1 = TarUtils.parseBoolean(buffer1, 0);
        assertTrue(result1);

        byte[] buffer2 = new byte[]{0x00};
        boolean result2 = TarUtils.parseBoolean(buffer2, 0);
        assertFalse(result2);
    }

    /**
     * Test parseName method.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcde", result);
    }

    /**
     * Test formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "filename";
        byte[] buffer = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'f', 'i', 'l', 'e', 'n', 'a', 'm', 'e'}, buffer, 0, 8);
        assertEquals(8, offset);
    }

    /**
     * Test formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', ' ', ' '}, buffer, 0, 10);
    }

    /**
     * Test formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', ' ', ' ', '0', ' '}, buffer, 0, 12);
        assertEquals(12, offset);
    }

    /**
     * Test formatLongOctalBytes method.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5'}, buffer, 0, 20);
        assertEquals(20, offset);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1234567890123456789L;
        byte[] buffer = new byte[25];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0x80, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f, 0x7f};
        assertArrayEquals(expected, buffer, 0, 25);
        assertEquals(25, offset);
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x67, 0x72, 0x65, 0x74, 0x50, 0x61, 0x73, 0x73, 0x53, 0x79, 0x61, 0x6e, 0x64, 0x6f, 0x72, 0x74, 0x65, 0x64, 0x00};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, computedChecksum);
    }
}