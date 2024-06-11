package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '0'};
        assertEquals(01777770L, TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7'};
        assertEquals(0177770L, TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{(byte) '0'};
        assertNull(TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{(byte) '0', (byte) '0', (byte) '7', (byte) '7'};
        assertEquals(010077L, TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{(byte) '0'};
        assertNull(TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{(byte) '7'};
        assertEquals(0100001L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] invalid = new byte[]{(byte) '8'};
        try {
            TarUtils.parseOctal(invalid, 0, invalid.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) 's'};
        assertEquals("hello files", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] empty = new byte[0];
        assertEquals("", TarUtils.parseName(empty, 0, empty.length));

        byte[] tooShort = new byte[]{(byte) 'h', (byte) 'e'};
        assertNull(TarUtils.parseName(tooShort, 0, tooShort.length));

        byte[] trailingNull = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) 's', (byte) 0};
        assertEquals("hello files", TarUtils.parseName(trailingNull, 0, trailingNull.length));

        byte[] leadingNull = new byte[]{(byte) 0, (byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) 's'};
        assertEquals("hello files", TarUtils.parseName(leadingNull, 0, leadingNull.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello files";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("hello files", new String(buffer, 0, offset));

        byte[] wrongLength = new byte[10];
        try {
            TarUtils.formatNameBytes(name, wrongLength, 0, wrongLength.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];

        long value = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0'}, buffer);

        value = 1;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1'}, buffer);

        value = 7;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1'}, buffer);

        value = 12345;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '7', (byte) '7', (byte) '1', (byte) '4', (byte) '5'}, buffer);

        value = Long.MAX_VALUE;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7'}, buffer);

        value = Long.MIN_VALUE;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[0], buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];

        long value = 0;
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '0'}, buffer, 0, 2);

        value = 1;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1'}, buffer, 0, 2);

        value = 7;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1'}, buffer, 0, 3);

        value = 12345;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '7', (byte) '7', (byte) '1', (byte) '4', (byte) '5'}, buffer, 0, 7);

        value = Long.MAX_VALUE;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 13);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7'}, buffer, 0, 12);

        value = Long.MIN_VALUE;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 1);
        assertTrue(buffer[0] == 0);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];

        long value = 0;
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0'}, buffer, 0, 1);

        value = 1;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1'}, buffer, 0, 2);

        value = 7;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1'}, buffer, 0, 3);

        value = 12345;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '7', (byte) '7', (byte) '1', (byte) '4', (byte) '5'}, buffer, 0, 7);

        value = Long.MAX_VALUE;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 13);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7'}, buffer, 0, 12);

        value = Long.MIN_VALUE;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 1);
        assertTrue(buffer[0] == 0);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];

        long value = 0;
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 12);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '0', (byte) '0'}, buffer, 0, 3);

        value = 1;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 12);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '0', (byte) '1'}, buffer, 0, 3);

        value = 7;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 12);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1'}, buffer, 0, 3);

        value = 12345;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 12);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '7', (byte) '7', (byte) '1', (byte) '4', (byte) '5'}, buffer, 0, 7);

        value = Long.MAX_VALUE;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 15);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7'}, buffer, 0, 14);

        value = Long.MIN_VALUE;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 1);
        assertTrue(buffer[0] == 0);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] data = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09};
        long checksum = TarUtils.computeCheckSum(data);
        long expected = 0x83167C5D8D9B6764L;
        assertEquals(expected, checksum);
    }
}