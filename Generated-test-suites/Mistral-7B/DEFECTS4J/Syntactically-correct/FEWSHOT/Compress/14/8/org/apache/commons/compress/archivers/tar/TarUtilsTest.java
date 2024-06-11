package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 064, (byte) 062, (byte) 066, (byte) 075, (byte) 064};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(1019981, result);

        byte[] buffer2 = new byte[]{(byte) 0};
        result = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        assertEquals(0L, result);

        byte[] buffer3 = new byte[]{(byte) 077};
        result = TarUtils.parseOctal(buffer3, 0, buffer3.length);
        assertEquals(127L, result);

        byte[] buffer4 = new byte[]{(byte) 077, (byte) 077};
        result = TarUtils.parseOctal(buffer4, 0, buffer4.length);
        assertEquals(65535L, result);

        byte[] buffer5 = new byte[]{(byte) 0, (byte) 0};
        try {
            result = TarUtils.parseOctal(buffer5, 0, buffer5.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer6 = new byte[]{(byte) 0, (byte) 0, (byte) 077};
        try {
            result = TarUtils.parseOctal(buffer6, 0, buffer6.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer7 = new byte[]{(byte) 077, (byte) 0};
        try {
            result = TarUtils.parseOctal(buffer7, 0, buffer7.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{(byte) 064, (byte) 062, (byte) 066, (byte) 075, (byte) 064};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(1019981, result);

        byte[] buffer2 = new byte[]{(byte) 0};
        result = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(0L, result);

        byte[] buffer3 = new byte[]{(byte) 077};
        result = TarUtils.parseOctalOrBinary(buffer3, 0, buffer3.length);
        assertEquals(127L, result);

        byte[] buffer4 = new byte[]{(byte) 077, (byte) 077};
        result = TarUtils.parseOctalOrBinary(buffer4, 0, buffer4.length);
        assertEquals(65535L, result);

        byte[] buffer5 = new byte[]{(byte) 0, (byte) 0};
        result = TarUtils.parseOctalOrBinary(buffer5, 0, buffer5.length);
        assertEquals(0L, result);

        byte[] buffer6 = new byte[]{(byte) 0, (byte) 0, (byte) 077};
        long binaryResult = 0;
        for (byte b : buffer6) {
            binaryResult = (binaryResult << 8) + (b & 0xFF);
        }
        result = binaryResult;
        assertEquals(2147483648L, result);

        byte[] buffer7 = new byte[]{(byte) 0x80, (byte) 0x01, (byte) 0x02};
        long binaryResult2 = 0x800102;
        result = TarUtils.parseOctalOrBinary(buffer7, 0, buffer7.length);
        assertEquals(binaryResult2, result);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{(byte) 1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);

        byte[] buffer2 = new byte[]{(byte) 0};
        result = TarUtils.parseBoolean(buffer2, 0);
        assertFalse(result);

        byte[] buffer3 = new byte[]{(byte) 0x80};
        result = TarUtils.parseBoolean(buffer3, 0);
        assertFalse(result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "MyTestName";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        String result = new String(buffer, 0, offset);
        assertEquals("MyTestName", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1019981;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        String result = new String(buffer, 0, offset);
        assertEquals("0123456octal1", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1019981;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        String result = new String(buffer, 0, offset);
        assertEquals("0123456octal1  ", result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1019981;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(9, offset);
        String result = new String(buffer, 0, offset);
        assertEquals("0123456", result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1019981;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        String result = new String(buffer, 0, offset);
        assertEquals("0123456octal1", result);

        value = 10235555121L; // Too large for octal representation, should use binary
        byte[] buffer2 = new byte[10];
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer2, 0, buffer2.length);
        assertEquals(10, offset);
        String result2 = new String(buffer2, 0, offset);
        String expected = "0000111101101111101011100101110111101111";
        assertEquals(expected, result2);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1019981;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        String result = new String(buffer, 0, offset);
        assertEquals("0123456octal1  ", result);
    }
}