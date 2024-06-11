package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{-64, 48, 33, 45, 52, 0};
        assertEquals(0127101, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] invalidBuffer = new byte[]{-64, 48, 33, 45, 'A', 0};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] emptyBuffer = new byte[0];
        assertNull(TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'c', 'm', 'e', 0};
        assertEquals("ame", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] emptyBuffer = new byte[0];
        assertNull(TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("test", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "1", "2", "3", "7", "7", "7", "7"}, buffer);

        long valueTooLarge = Long.parseUnsignedLong("123456789012345678901234567890", 8);
        try {
            TarUtils.formatUnsignedOctalString(valueTooLarge, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "1", "2", "3", " ", "0", "0", "0", "0", "0"}, buffer);

        long valueTooLarge = Long.parseUnsignedLong("123456789012345678901234567890123", 10);
        try {
            TarUtils.formatOctalBytes(valueTooLarge, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "1", "2", "3", " ", "0", "0", "0"}, buffer);

        long valueTooLarge = Long.parseUnsignedLong("123456789012345678901234567890123", 9);
        try {
            TarUtils.formatLongOctalBytes(valueTooLarge, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "1", "2", "3", "7", "7", "7", "7", "0", " ", " "}, buffer);

        long valueTooLarge = Long.parseUnsignedLong("1234567890123456789012345678901234567890", 11);
        try {
            TarUtils.formatCheckSumOctalBytes(valueTooLarge, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{-64, 65, 66, 67};
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(0127256, checksum);
    }
}