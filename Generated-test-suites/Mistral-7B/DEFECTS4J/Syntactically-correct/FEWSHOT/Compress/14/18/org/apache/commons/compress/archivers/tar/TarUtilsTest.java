package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '0'};
        assertEquals(0175540L, TarUtils.parseOctal(buffer, 0, buffer.length));

        // Test missing trailing space/NUL
        byte[] bufferMissingTrailer = new byte[]{'0', '7', '5', '5', '4', '0'};
        try {
            TarUtils.parseOctal(bufferMissingTrailer, 0, bufferMissingTrailer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test leading NUL
        byte[] bufferLeadingNul = new byte[]{0, '7', '5', '5', '4', '0'};
        assertEquals(0L, TarUtils.parseOctal(bufferLeadingNul, 0, bufferLeadingNul.length));

        // Test invalid byte
        byte[] bufferInvalidByte = new byte[]{'0', '7', '5', '5', '4', '8'};
        try {
            TarUtils.parseOctal(bufferInvalidByte, 0, bufferInvalidByte.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test binary number (no leading 0x)
        byte[] bufferBinaryNoLeading = new byte[]{(byte) 0x81, (byte) 0x42, (byte) 0x25};
        assertEquals(33070L, TarUtils.parseOctalOrBinary(bufferBinaryNoLeading, 0, bufferBinaryNoLeading.length));

        // Test binary number (with leading 0x)
        byte[] bufferBinaryWithLeading = new byte[]{(byte) 0x30, (byte) 0x81, (byte) 0x42, (byte) 0x25};
        assertEquals(33070L, TarUtils.parseOctalOrBinary(bufferBinaryWithLeading, 0, bufferBinaryWithLeading.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '0'};
        assertEquals(0175540L, TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));

        // Test binary number (no leading 0x)
        byte[] bufferBinaryNoLeading = new byte[]{(byte) 0x81, (byte) 0x42, (byte) 0x25};
        assertEquals(33070L, TarUtils.parseOctalOrBinary(bufferBinaryNoLeading, 0, bufferBinaryNoLeading.length));

        // Test binary number (with leading 0x)
        byte[] bufferBinaryWithLeading = new byte[]{(byte) 0x30, (byte) 0x81, (byte) 0x42, (byte) 0x25};
        assertEquals(33070L, TarUtils.parseOctalOrBinary(bufferBinaryWithLeading, 0, bufferBinaryWithLeading.length));

        // Test binary number exceeding long size
        byte[] bufferBinaryExceedingLong = new byte[]{
            (byte) 0x30, (byte) 0x81, (byte) 0x42, (byte) 0x25,
            (byte) 0x30, (byte) 0x81, (byte) 0x42, (byte) 0x25,
            (byte) 0x30, (byte) 0x81, (byte) 0x42, (byte) 0x25,
            (byte) 0x30, (byte) 0x81, (byte) 0x42, (byte) 0x25,
            (byte) 0x30, (byte) 0x81, (byte) 0x42, (byte) 0x25
        };
        try {
            TarUtils.parseOctalOrBinary(bufferBinaryExceedingLong, 0, bufferBinaryExceedingLong.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        assertTrue(TarUtils.parseBoolean(bufferTrue, 0));

        byte[] bufferFalse = new byte[]{0};
        assertFalse(TarUtils.parseBoolean(bufferFalse, 0));

        byte[] bufferInvalid = new byte[]{2};
        try {
            TarUtils.parseBoolean(bufferInvalid, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 0};
        assertEquals("tar.txt", TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test.tar.gz";
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertEquals("test.tar.gz", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0175540L;
        byte[] buffer = new byte[9];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '5', '5', '4', '0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0175540L;
        byte[] buffer = new byte[11];

        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '5', '5', '4', '0', ' ', 0, 0}, buffer);
        assertEquals(11, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0175540L;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '5', '5', '4', '0', ' ', 0}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1234567890123456789L;
        byte[] buffer = new byte[12];

        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x82, 0x85}, buffer);
        assertEquals(12, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890123456789L;
        byte[] buffer = new byte[13];

        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ' ', 0}, buffer);
        assertEquals(13, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 1, 2, 3, 4, 5};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expected = 0x123456789ababcdeL;
        assertEquals(expected, checksum);
    }
}