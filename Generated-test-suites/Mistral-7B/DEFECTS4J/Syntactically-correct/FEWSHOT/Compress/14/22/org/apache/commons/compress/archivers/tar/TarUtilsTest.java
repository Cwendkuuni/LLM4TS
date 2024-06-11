package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 0777, (byte) 0644, (byte) 0444};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0177700444L, result);

        buffer = new byte[]{0};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);

        buffer = new byte[]{(byte) 0};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{(byte) 0x81, (byte) 0x01};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{(byte) 0x81, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(10485761235L, result);

        buffer = new byte[]{(byte) 0777, (byte) 0644, (byte) 0444};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0177700444L, result);

        buffer = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(28147497672325L, result);

        buffer = new byte[]{(byte) 0};
        try {
            result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{(byte) 0x01, (byte) 0x80};
        try {
            result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);

        buffer = new byte[]{0};
        result = TarUtils.parseBoolean(buffer, 0);
        assertFalse(result);

        buffer = new byte[]{0x80};
        result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);

        buffer = new byte[]{1, 0x80};
        try {
            result = TarUtils.parseBoolean(buffer, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abc", result);

        buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 0, (byte) 'd'};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abc", result);

        buffer = new byte[]{(byte) 0};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertNull(result);

        buffer = new byte[]{1};
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{0x80};
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "name";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(name.length(), offset);
        assertArrayEquals(name.getBytes(), buffer, 0, name.length());
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0177700444L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '7', (byte) '7', (byte) '0', (byte) '0', (byte) '4', (byte) '4', (byte) '4'}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0177700444L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 12);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '7', (byte) '7', (byte) '0', (byte) '0', (byte) '4', (byte) '4', (byte) '4', (byte) ' ', (byte) ' '}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0177700444L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '7', (byte) '7', (byte) '0', (byte) '0', (byte) '4', (byte) '4', (byte) '4'}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 10485761235L;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 5);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1'}, buffer, 0, buffer.length);

        value = 28147497672325L;
        buffer = new byte[11];
        try {
            offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[3];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 3);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '0'}, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(1972325177L, checksum);

        buffer = new byte[]{0};
        checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(0L, checksum);

        buffer = new byte[]{(byte) 0x80};
        try {
            checksum = TarUtils.computeCheckSum(buffer);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}