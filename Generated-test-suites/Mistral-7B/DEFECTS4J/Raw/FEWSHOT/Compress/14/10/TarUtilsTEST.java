package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 0777, (byte) 0644, (byte) 0600, (byte) 0, (byte) 0, (byte) 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0777644600L, result);

        buffer = new byte[]{0, 0, 0, 0, 0, 0};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);

        buffer = new byte[]{(byte) 0, (byte) 0, (byte) 0x80, (byte) 0};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{(byte) 077, (byte) 064};
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(3187L, result1);

        byte[] buffer2 = new byte[]{(byte) 0x81, (byte) 0x7f};
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(3221225291L, result2);

        byte[] buffer3 = new byte[]{(byte) 0};
        try {
            result3 = TarUtils.parseOctalOrBinary(buffer3, 0, buffer3.length);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] buffer4 = new byte[]{(byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80};
        try {
            result4 = TarUtils.parseOctalOrBinary(buffer4, 0, buffer4.length);
            assertFalse(true);
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

        buffer = new byte[]{0, 1};
        try {
            result = TarUtils.parseBoolean(buffer, 0);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'f', (byte) 'o', (byte) 'o', (byte) '.', (byte) 'b', (byte) 'a', (byte) 'r'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("foo.bar", result);

        buffer = new byte[]{0};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertNull(result);

        buffer = new byte[]{(byte) 0};
        try {
            result = TarUtils.parseName(buffer, 0, buffer.length);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo.bar";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{(byte) 'f', (byte) 'o', (byte) 'o', (byte) '.', (byte) 'b', (byte) 'a', (byte) 'r', (byte) 0, (byte) 0, (byte) 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) ' ', (byte) '0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[11];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) ' ', (byte) ' ', (byte) '0'}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '0', (byte) '1', (byte) '2', (byte) '3'}, buffer);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = Long.parseUnsignedLong("123456789012345", 8);
        byte[] buffer = new byte[9];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8'}, buffer);

        value = Long.parseUnsignedLong("123456789012346", 8);
        buffer = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) 0x80, (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9'}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[3];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2'}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 1, 2, 3, 4};
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(193, checksum);
    }
}