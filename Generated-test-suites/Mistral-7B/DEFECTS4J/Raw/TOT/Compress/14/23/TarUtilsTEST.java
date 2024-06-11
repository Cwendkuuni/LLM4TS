package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TarUtilsTest.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid inputs.
     */
    @org.junit.Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456L, result);
    }

    /**
     * Test parseOctal method with invalid inputs.
     */
    @org.junit.Test
    public void testParseOctalInvalid() {
        byte[] buffer1 = new byte[]{'0', '1', '2', '3', '4', '5', '8'};
        byte[] buffer2 = new byte[]{'0', '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer1, 0, buffer1.length));
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer2, 0, buffer2.length));
    }

    /**
     * Test parseOctalOrBinary method with valid inputs.
     */
    @org.junit.Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0123456L, result);

        byte[] binaryBuffer = new byte[]{(byte) 0x81, 0, 0, 0, 0, 0, 0, 0x7F};
        result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        assertEquals(0x81000000L | 0x0000007F, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid inputs.
     */
    @org.junit.Test
    public void testParseOctalOrBinaryInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '9'};
        assertThrows(IllegalArgumentException.class,
                () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));

        byte[] tooShortBuffer = new byte[]{'0', '1'};
        assertThrows(IllegalArgumentException.class,
                () -> TarUtils.parseOctalOrBinary(tooShortBuffer, 0, tooShortBuffer.length));
    }

    /**
     * Test parseBoolean method with valid inputs.
     */
    @org.junit.Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{(byte) 0x01};
        byte[] buffer2 = new byte[]{(byte) 0x00};
        boolean result1 = TarUtils.parseBoolean(buffer1, 0);
        assertTrue(result1);
        boolean result2 = TarUtils.parseBoolean(buffer2, 0);
        assertFalse(result2);
    }

    /**
     * Test parseBoolean method with invalid inputs.
     */
    @org.junit.Test
    public void testParseBooleanInvalid() {
        byte[] buffer = new byte[]{'0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(buffer, 0));
    }

    /**
     * Test parseName method with valid inputs.
     */
    @org.junit.Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', (byte) 0x00};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcdef", result);
    }

    /**
     * Test parseName method with invalid inputs.
     */
    @org.junit.Test
    public void testParseNameInvalid() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 0};
        assertThrows(IllegalArgumentException.class,
                () -> TarUtils.parseName(buffer, 0, buffer.length));
    }

    /**
     * Test formatNameBytes method with valid inputs.
     */
    @org.junit.Test
    public void testFormatNameBytes() {
        String name = "myfile.txt";
        byte[] buffer = new byte[TarUtils.TAR_NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(TarUtils.TAR_NAME_LENGTH, offset);
        byte[] result = new byte[TarUtils.TAR_NAME_LENGTH];
        System.arraycopy(buffer, 0, result, 0, TarUtils.TAR_NAME_LENGTH);
        assertEquals("myfile.txt", new String(result, 0, TarUtils.TAR_NAME_LENGTH));
    }

    /**
     * Test formatNameBytes method with invalid inputs.
     */
    @org.junit.Test
    public void testFormatNameBytesInvalid() {
        String name = "myfile.txt";
        byte[] buffer = new byte[1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(0, offset);
    }

    /**
     * Test formatUnsignedOctalString method with valid inputs.
     */
    @org.junit.Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0123456";
        assertEquals(expected, new String(buffer, 0, 7));
    }

    /**
     * Test formatUnsignedOctalString method with invalid inputs.
     */
    @org.junit.Test
    public void testFormatUnsignedOctalStringInvalid() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertThrows(IllegalArgumentException.class, () ->
                TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length));
    }

    /**
     * Test formatOctalBytes method with valid inputs.
     */
    @org.junit.Test
    public void testFormatOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);

        byte[] expected = new byte[]{0, '0', '1', '2', '3', '4', '5', '6', ' ', '0', '0'};
        assertArrayEquals(expected, buffer, 0, 11);
    }

    /**
     * Test formatOctalBytes method with invalid inputs.
     */
    @org.junit.Test
    public void testFormatOctalBytesInvalid() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(0, offset);
        assertThrows(IllegalArgumentException.class, () ->
                TarUtils.formatOctalBytes(value, buffer, 0, buffer.length));
    }

    /**
     * Test formatCheckSumOctalBytes method with valid inputs.
     */
    @org.junit.Test
    public void testFormatCheckSumOctalBytes() {
        long value = TarUtils.computeCheckSum(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);

        byte[] expected = new byte[]{0, '0', '1', '2', '3', '4', '5', '6', ' ', '0', '0'};
        assertArrayEquals(expected, buffer, 0, 11);
    }

    /**
     * Test formatCheckSumOctalBytes method with invalid inputs.
     */
    @org.junit.Test
    public void testFormatCheckSumOctalBytesInvalid() {
        byte[] buffer = new byte[12];
        long value = TarUtils.computeCheckSum(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(0, offset);
        assertThrows(IllegalArgumentException.class, () ->
                TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length));
    }

    /**
     * Test parseOctal method with NUL in input.
     */
    @org.junit.Test
    public void testParseOctalWithNUL() {
        byte[] buffer = new byte[]{'0', '1', '2', (byte) 0x00, '3', '4', '5', '6', (byte) 0x00};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456L, result);
    }

    /**
     * Test parseOctalOrBinary method with NUL in input.
     */
    @org.junit.Test
    public void testParseOctalOrBinaryWithNUL() {
        byte[] buffer = new byte[]{'0', '1', '2', (byte) 0x00, '3', '4', '5', '6', (byte) 0x00};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0123456L, result);

        byte[] binaryBuffer = new byte[]{(byte) 0x81, 0x00, 0, 0, 0, 0, 0, 0x7F};
        result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        assertEquals(0x81000000L, result);
    }
}