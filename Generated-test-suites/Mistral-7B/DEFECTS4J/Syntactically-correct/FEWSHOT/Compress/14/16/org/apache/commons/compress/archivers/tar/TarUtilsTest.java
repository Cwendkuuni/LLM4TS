package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 0777, (byte) 0666, (byte) 0444};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01777666444L, result);

        buffer = new byte[]{0, 0};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{0, (byte) 08};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{0, (byte) 0x5B};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x0102030405L << 24 | 0x0607, result);

        buffer = new byte[]{0x80, 0x12, 0x34, 0x56, 0x78, 0x9A, 0xBc, 0xD1};
        long result2 = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x12345678L << 24 | 0x9ABCD1, result2);

        byte[] buffer2 = new byte[]{0};
        try {
            result = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);

        byte[] buffer2 = new byte[]{0};
        result = TarUtils.parseBoolean(buffer2, 0);
        assertFalse(result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "testName".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("testName", result);

        byte[] buffer2 = new byte[]{0};
        try {
            result = TarUtils.parseName(buffer2, 0, buffer2.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        assertEquals("testName", result);

        name = "testName\0";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        result = new String(buffer, 0, offset);
        assertEquals("testName", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01777666444L;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String result = new String(buffer);
        assertEquals("010111111111110111111101111111", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 01777666444L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        assertEquals("010111111111110111111101111111   ", result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 01777666444L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        assertEquals("01011111111111", result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0xFFFF_FFFFFF_FFFFFFL;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        assertEquals("--------FFFFFFFF", result);

        value = 0x1234567890ABCDEF0L;
        buffer = new byte[11];
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, offset);
        assertEquals("012345677554321015ABCDEF0", result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x1234567890ABCDEF0L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        assertEquals("012345677554321015ABCDEF0   ", result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += (long) (buffer[i] & 0xFF);
        }
        assertEquals(computedChecksum, checksum);
    }
}