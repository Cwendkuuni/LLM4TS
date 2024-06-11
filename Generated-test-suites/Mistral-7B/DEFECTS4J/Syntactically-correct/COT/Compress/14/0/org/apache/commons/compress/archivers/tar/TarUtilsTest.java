package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.apache.commons.compress.archivers.tar.TarConstants;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * TarUtils test class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @org.junit.Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0', ' ', '0'};
        long expected = 0177500L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid input.
     */
    @org.junit.Test
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0', '4', '2'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /**
     * Test parseOctalOrBinary method with valid binary input.
     */
    @org.junit.Test
    public void testParseOctalOrBinaryBinaryInput() {
        byte[] buffer = new byte[]{(byte) 0x40, (byte) 0x03, 0x00, 0x02};
        long expected = 0x03040002L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctalOrBinary method with valid octal input.
     */
    @org.junit.Test
    public void testParseOctalOrBinaryOctalInput() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0', ' ', '0'};
        long expected = 0177500L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid input.
     */
    @org.junit.Test
    public void testParseOctalOrBinaryInvalidInput() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0', '4', '2', '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @org.junit.Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, result);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @org.junit.Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean expected = false;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, result);
    }

    /**
     * Test parseName method with valid input.
     */
    @org.junit.Test
    public void testParseName() {
        byte[] buffer = new byte[]{75, 101, 115, 116, 101, 116, 101, 116, 0};
        String expected = "User.java";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @org.junit.Test
    public void testFormatNameBytes() {
        String name = "User.java";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String expected = "User.java";
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        assertEquals(expected, result);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @org.junit.Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567L;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "01234567";
        String result = new String(buffer, 0, 10, StandardCharsets.UTF_8);
        assertEquals(expected, result);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @org.junit.Test
    public void testFormatOctalBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567  ";
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalBytes method with valid input.
     */
    @org.junit.Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456789012345  ";
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input.
     */
    @org.junit.Test
    public void testFormatLongOctalOrBinaryBytesOctalInput() {
        long value = 123456789012345L;
        byte[] buffer = new byte[14];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String expected = "0123456789012345  ";
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with binary input.
     */
    @org.junit.Test
    public void testFormatLongOctalOrBinaryBytesBinaryInput() {
        long value = 123456789012345L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0x40, 0x2a, 0x2e, 0x2e, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x20};
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        byte[] result = new byte[11];
        bb.get(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Test formatCheckSumOctalBytes method with valid input.
     */
    @org.junit.Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456789012345  ";
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        assertEquals(expected, result);
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @org.junit.Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{75, 101, 115, 116, 101, 116, 101, 116};
        long expected = 1844851523149278500L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    /**
     * Test computeCheckSum method with empty buffer.
     */
    @org.junit.Test
    public void testComputeCheckSumEmptyBuffer() {
        byte[] buffer = new byte[0];
        long expected = 0L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}