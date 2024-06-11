package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0x3, (byte) 0x3, 0x7, 0x6, 0x5, 0x4, 0x3, 0, 0};
        long expected = 0176430L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{0x3, 0x3, 0x7, 0x6, 0x5, 0x4, 0x3, (byte) 0xC, 0};
        expected = 0176437L;
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{0};
        assertNull(TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{' ', 0x3, 0x3, 0x7, 0x6, 0x5, 0x4, 0x3, 0, 0};
        assertNull(TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{'a', 0x3, 0x3, 0x7, 0x6, 0x5, 0x4, 0x3, 0, 0};
        assertNull(TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', 0};
        String expected = "hello";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 0};
        expected = "hello world";
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{'h', 'e', 'l', 'l', 0};
        assertNull(TarUtils.parseName(buffer, 0, buffer.length));

        buffer = new byte[]{'a', 'b', 'c', 0};
        assertNull(TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0, 0, 0, 0}, buffer, 0, offset);

        name = "hello world";
        buffer = new byte[15];
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', 0, 0, 0, 0}, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '1', '2', '3', '4', '5', '6', '7', '8'}, buffer, 0, buffer.length);

        value = 0xFFFF000000000000L;
        buffer = new byte[20];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '5', '6', '7', '8'}, buffer, 0, offset);

        value = 0xFFFF000000000000L;
        buffer = new byte[22];
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer, 0, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '5', '6', '7'}, buffer, 0, offset);

        value = 0xFFFF000000000000L;
        buffer = new byte[21];
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer, 0, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '5', '6', '7', '0', ' '}, buffer, 0, offset);

        value = 0xFFFF000000000000L;
        buffer = new byte[23];
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '0'}, buffer, 0, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[]{0x12, 0x34, 0x56, 0x78, 0x90, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88};
        long expected = TarUtils.computeCheckSum(header);
        long result = TarUtils.computeCheckSum(header);
        assertEquals(expected, result);
    }
}