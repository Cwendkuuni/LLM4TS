package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '5', '1', '2', '0', ' ', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175545120L, result);

        buffer = new byte[]{'0', '\u0000'};
        assertNull(TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{'1', '\u0001'};
        assertNull(TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{'5', '\u0002'};
        assertNull(TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{'7', '\u0003', '3', '\u0004', '6', '\u0005', '2', '\u0006', '1', '\u0007'};
        long result2 = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(012345617L, result2);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{'0'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0L, result);

        buffer = new byte[]{'0', '1'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(1L, result);

        buffer = new byte[]{'0', '1', '0'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(1L, result);

        buffer = new byte[]{'0', '1', '0', '1'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(5L, result);

        buffer = new byte[]{'0', '1', '0', '1', '1'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(6L, result);

        buffer = new byte[]{'0', '1', '0', '1', '1', '1'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(7L, result);

        buffer = new byte[]{'0', '1', '0', '1', '1', '1', '1'};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(127L, result);

        buffer = new byte[]{'1', '2'};
        assertNull(TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));

        buffer = new byte[]{'1', '\u0001', '2'};
        assertNull(TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));

        buffer = new byte[]{'1', '\u0001', '\u0001', '\u0001'};
        long result3 = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(255L, result3);

        buffer = new byte[]{'1', '\u0001', '\u0001', '\u0001', '\u0001'};
        result3 = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(65535L, result3);

        buffer = new byte[]{'1', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001'};
        result3 = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(16777215L, result3);

        buffer = new byte[]{'1', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001'};
        long result4 = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(4294967295L, result4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryException1() {
        byte[] buffer = new byte[]{'1', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001', '\u0001'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryException2() {
        byte[] buffer = new byte[]{'0', '1', '0', '1', '1'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length + 1);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "filename.txt";
        byte[] buffer = new byte[50];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(name.length(), result);

        for (int i = 0; i < name.length(); ++i) {
            assertEquals((byte) name.charAt(i), buffer[i]);
        }

        for (int i = name.length(); i < result; ++i) {
            assertEquals(0, buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '1', '2'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];

        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '1', '2', ' ', '0'}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[12];

        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'}, buffer);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[13];

        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '\u0000'}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        long result = TarUtils.computeCheckSum(buffer);
        long expected = 30650L;
        assertEquals(expected, result);
    }
}