package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '4', '5', '6', '7', '7', '7', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175456775L, result);

        byte[] invalidBuffer = new byte[]{'0', '0', '7', '5', '4', '5', '6', '7', '7', '7', '8'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length));

        byte[] trailingNulBuffer = new byte[]{'0', '0', '7', '5', '4', '5', '6', '7', '7', '7'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(trailingNulBuffer, 0, trailingNulBuffer.length - 1));

        byte[] leadingNulBuffer = new byte[]{0, '0', '7', '5', '4', '5', '6', '7', '7', '7', '0'};
        assertEquals(0L, TarUtils.parseOctal(leadingNulBuffer, 0, leadingNulBuffer.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] binaryBuffer = new byte[]{0, 0, 0, 1, 0, 1, 1, 1};
        long result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        assertEquals(1845L, result);

        byte[] octalBuffer = new byte[]{'0', '1', '1', '1', '0', '1', '1', '1', '0'};
        result = TarUtils.parseOctalOrBinary(octalBuffer, 0, octalBuffer.length);
        assertEquals(1845L, result);

        byte[] invalidBuffer = new byte[]{'0', '0', '7', '5', '4', '5', '6', '7', '7', '7', '8'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(invalidBuffer, 0, invalidBuffer.length));

        byte[] binaryTooLargeBuffer = new byte[]{0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(binaryTooLargeBuffer, 0, binaryTooLargeBuffer.length));
    }

    @Test
    public void testParseBoolean() {
        byte[] trueBuffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(trueBuffer, 0);
        assertEquals(true, result);

        byte[] falseBuffer = new byte[]{0};
        result = TarUtils.parseBoolean(falseBuffer, 0);
        assertEquals(false, result);

        byte[] invalidBuffer = new byte[]{2};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(invalidBuffer, 0));
    }

    @Test
    public void testParseName() {
        byte[] nameBuffer = new byte[]{'t', 'a', 'r', ' ', 'n', 'a', 'm', 'e', '.', 't', 'a', 'r', '\0'};
        String result = TarUtils.parseName(nameBuffer, 0, nameBuffer.length);
        assertEquals("tar.name", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.name";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        for (int i = 0; i < buffer.length; ++i) {
            if (i < name.length()) {
                assertEquals((byte) name.charAt(i), buffer[i]);
            } else {
                assertEquals((byte) 0, buffer[i]);
            }
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("01234567755", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(13, offset);
        assertEquals("01234567755\n", new String(buffer));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        assertEquals("0123456755\n", new String(buffer));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals(13, offset);
        assertEquals("01234567755\n", new String(buffer));

        byte[] binaryBuffer = new byte[13];
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, binaryBuffer, 0, buffer.length);
        assertEquals(13, offset);
        assertEquals("00011111111101111101111011001111\n", new String(binaryBuffer));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(13, offset);
        assertEquals("000000007655\n", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(1474534865L, checksum);
    }
}