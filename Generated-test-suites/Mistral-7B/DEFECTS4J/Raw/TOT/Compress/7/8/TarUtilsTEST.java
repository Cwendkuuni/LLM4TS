package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '7', (byte) '6', (byte) '4', (byte) '2', (byte) '0'};
        long expected = 0175576420L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidDigit() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) 'X', (byte) '5', (byte) '7', (byte) '6', (byte) '4', (byte) '2', (byte) '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalWithTrailingSpace() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '7', (byte) '6', (byte) '4', (byte) '2', (byte) '0', (byte) ' '};
        long expected = 0175576420L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithTrailingNull() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '7', (byte) '6', (byte) '4', (byte) '2', (byte) '0', (byte) 0};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e'};
        String expected = "abcd e";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdef";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'a', 'b', 'c', 'd', 'e', ' ', 'f', 0, 0, 0}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '5', '7'}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithLongValue() {
        long value = 123456789012345L;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '5', '7', ' ', '0'}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x84, (byte) 0x63, (byte) 0x61, (byte) 0x6d, (byte) 0x70, (byte) 0x6f, (byte) 0x6b, (byte) 0x65, (byte) 0x00};
        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = 0x6785312800L ^ 0x758463616d706f6b65L; // bitwise XOR of the expected checksum and the given buffer
        assertEquals(computedChecksum, checksum);
    }
}