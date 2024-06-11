package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5'};
        assertEquals(01755454545454545L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferTrailingNul = new byte[]{'0', '7', '5', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', (byte) 0};
        assertEquals(01755454545454545L, TarUtils.parseOctal(bufferTrailingNul, 0, bufferTrailingNul.length));

        byte[] bufferLeadingNul = new byte[]{(byte) 0, '7', '5', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5'};
        assertEquals(0L, TarUtils.parseOctal(bufferLeadingNul, 0, bufferLeadingNul.length));

        byte[] bufferInvalid = new byte[]{'0', '7', '5', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', 'x'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length));

        byte[] bufferInvalidLength = new byte[]{'0', '7', '5'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferInvalidLength, 0, bufferInvalidLength.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] bufferOctal = new byte[]{'0', '7', '5', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5'};
        assertEquals(01755454545454545L, TarUtils.parseOctalOrBinary(bufferOctal, 0, bufferOctal.length));

        byte[] bufferBinary = new byte[]{(byte) 0x81, 0x2A, 0x97, 0x1E, 0x04, 0x97, 0x1E, 0x04, 0x97, 0x1E, 0x04, 0x97, 0x1E, 0x04, 0x97, 0x1E};
        assertEquals(12992866511576251551L, TarUtils.parseOctalOrBinary(bufferBinary, 0, bufferBinary.length));

        byte[] bufferInvalidOctal = new byte[]{'0', '7', '5', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', 'x'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(bufferInvalidOctal, 0, bufferInvalidOctal.length));

        byte[] bufferInvalidBinaryLength = new byte[]{(byte) 0x81, 0x2A};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(bufferInvalidBinaryLength, 0, bufferInvalidBinaryLength.length));

        byte[] bufferBinaryTooLarge = new byte[]{(byte) 0x81, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(bufferBinaryTooLarge, 0, bufferBinaryTooLarge.length));
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{(byte) 0x01};
        assertTrue(TarUtils.parseBoolean(bufferTrue, 0));

        byte[] bufferFalse = new byte[]{(byte) 0x00};
        assertFalse(TarUtils.parseBoolean(bufferFalse, 0));

        byte[] bufferInvalid = new byte[]{'x'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(bufferInvalid, 0));
    }

    @Test
    public void testParseName() {
        byte[] bufferName = new byte[]{'.', 'a', '.', 'b', '.', 'c', '.', 'd', 0};
        assertEquals("./abc./d", TarUtils.parseName(bufferName, 0, bufferName.length));

        byte[] bufferNameWithLength = new byte[]{'a', 'b', 'c', 'd', '.', 'e', '.', 'f', 0};
        assertEquals("abc.de./f", TarUtils.parseName(bufferNameWithLength, 0, bufferNameWithLength.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefg";
        byte[] buffer = new byte[7];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, 7);
        assertArrayEquals(new byte[]{'a', 'b', 'c', 'd', 'e', 'g', 'g'}, buffer);
        assertEquals(7, offset);

        name = "abc";
        offset = TarUtils.formatNameBytes(name, buffer, 0, 3);
        assertArrayEquals(new byte[]{'a', 'b', 'c'}, buffer);
        assertEquals(3, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01755454545454545L;
        byte[] buffer = new byte[11];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, 11);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '7', '5', '5', '4', '5', '4', '5'}, buffer);

        value = 0L;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 11);
        assertArrayEquals(new byte[]{}, buffer);

        value = 01777777777777777L;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, 0, 10));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 01755454545454545L;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatOctalBytes(value, buffer, 0, 10);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '7', '5', '5', '4', '5', '4'}, buffer);
        assertEquals(10, offset);

        value = 0L;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, 10);
        assertEquals(10, offset);

        value = 01111111111111111L;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, 9);
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, 0, 9));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 01755454545454545L;
        byte[] buffer = new byte[9];

        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, 9);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '7', '5', '5', '4', '5'}, buffer);
        assertEquals(9, offset);

        value = 0L;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, 9);
        assertEquals(9, offset);

        value = 01111111111111111L;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, 8);
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalBytes(value, buffer, 0, 8));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 01755454545454545L;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 10);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '7', '5', '5', '4', '5', '4'}, buffer);
        assertEquals(10, offset);

        value = 0111111111111111111111111111111111111111111111111111111111111111L;
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 14);
        assertArrayEquals(new byte[]{(byte) 0x80, 0x37, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77}, buffer);
        assertEquals(14, offset);

        value = 0x1234567890ABCDEF0L;
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 12);
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 12));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long checksum = 0x1234567890ABCDEF0L;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, 10);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, buffer);
        assertEquals(10, offset);

        checksum = 0L;
        offset = TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, 10);
        assertEquals(10, offset);

        checksum = 0x1234567890ABCDEF0L;
        offset = TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, 9);
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, 9));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[]{'T', 'a', 'r', ' ', 'h', 'e', 'a', 'd', 'e', 'r', ' ', 'f', 'i', 'l', 'e', ' ', '0', '7', '5', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5', '4', '5'};
        long checksum = TarUtils.computeCheckSum(header);
        assertEquals(01755454545454545L, checksum);

        header = new byte[]{'T', 'a', 'r', ' ', 'h', 'e', 'a', 'd', 'e', 'r', ' ', 'f', 'i', 'l', 'e', ' ', '0', '1'};
        checksum = TarUtils.computeCheckSum(header);
        assertEquals(0L, checksum);
    }
}