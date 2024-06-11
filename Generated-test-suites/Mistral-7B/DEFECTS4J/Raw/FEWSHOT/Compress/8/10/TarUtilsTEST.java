package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '7', '0'};
        assertEquals(0175577L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] invalidBuffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '7'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] nullBuffer = null;
        try {
            TarUtils.parseOctal(nullBuffer, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] shortBuffer = new byte[]{'0'};
        try {
            TarUtils.parseOctal(shortBuffer, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] nameBuffer = new byte[]{'h', 'e', 'l', 'l', 'o', '\0'};
        assertEquals("hello", TarUtils.parseName(nameBuffer, 0, nameBuffer.length));

        byte[] emptyBuffer = new byte[]{' '};
        assertNull(TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));

        byte[] nullBuffer = null;
        assertNull(TarUtils.parseName(nullBuffer, 0, 1));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(offset, 12);
        assertArrayEquals(new byte[]{68, 101, 108, 108, 108, 101, 32, 0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, '0', '2', '1', '1', '4', '5', ' ', '0', '\0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 12);
        assertArrayEquals(new byte[]{0, '0', '2', '1', '1', '4', '5', ' ', '\0', '\0'}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[14];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 14);
        assertArrayEquals(new byte[]{0, '0', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '\0'}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[14];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 14);
        assertArrayEquals(new byte[]{0, '0', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expectedChecksum = ((5 & 0xFF) << 24 | (4 & 0xFF) << 16 | (3 & 0xFF) << 8 | (2 & 0xFF) << 0) ^ (~0);
        assertEquals(expectedChecksum, checksum);

        byte[] emptyBuffer = new byte[0];
        long emptyChecksum = TarUtils.computeCheckSum(emptyBuffer);
        assertEquals(0, emptyChecksum);
    }
}