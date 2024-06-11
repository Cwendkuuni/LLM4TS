package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteArrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for TarUtils class.
 */
public class TarUtilsTest {

    private static final byte[] TEST_BYTE_ARRAY = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', ' ', 'N', 'a', 'm', 'e', 0};

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0'};
        long expected = 01234567110L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        buffer = new byte[]{'0', '1', '2'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0', '1', '2', '3', '9'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8', '9'}; // invalid byte 8
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{ByteArrays.BYTES_TO_HEX_BYTE[(byte) 0xCA]};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', 0, 0};
        String expected = "Tar Header";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        buffer = new byte[]{'T', 'a', 'r', 0, 0};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("Tar", result);

        buffer = new byte[]{'T', 'a', 0, 0};
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'T', 'a', 'r', 'H', 'e', 'a', 'd', 'e', 'r', ' ', 'A', 'p', 'p', 'l', 'e', 0, 0};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("Tar Header Apple", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Tar Header";
        byte[] buffer = new byte[15];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 15);
        Assert.assertArrayEquals(TEST_BYTE_ARRAY, buffer);

        name = "Tar";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 7);
        Assert.assertArrayEquals(new byte[]{'T', 'a', 'r', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buffer);

        name = "Apple";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 8);
        Assert.assertArrayEquals(new byte[]{'A', 'p', 'p', 'l', 'e', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x4321;
        byte[] buffer = new byte[11];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '7', '5', '5', '1', '0', '1', '1', '0'}, buffer);

        value = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.assertArrayEquals(new byte[]{'0'}, buffer);

        value = 0xFFFFFFFFFFFFFFFFL;
        try {
            TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 01234567110L;
        byte[] buffer = new byte[11];

        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 11);
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '1', '1', '0'}, buffer);

        value = 0;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 11);
        Assert.assertArrayEquals(new byte[]{'0', '0'}, buffer);

        value = 0xFFFFFFFFFFFFFFFFL;
        try {
            TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 01234567110L;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 10);
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '1', '1'}, buffer);

        value = 0;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 10);
        Assert.assertArrayEquals(new byte[]{'0'}, buffer);

        value = 0xFFFFFFFFFFFFFFFFL;
        try {
            TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0xABCDEF1234567890L;
        byte[] buffer = new byte[13];

        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 13);
        Assert.assertArrayEquals(new byte[]{'0', '1', '7', '7', '6', '7', '5', '6', '2', '3', '4', '5', '0'}, buffer);

        value = 0;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 13);
        Assert.assertArrayEquals(new byte[]{'0', '0', '0'}, buffer);

        value = 0xFFFFFFFFFFFFFFFFL;
        try {
            TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long checksum = TarUtils.computeCheckSum(buffer);

        // Checksum should be the one-complement of the sum of all bytes
        long sum = 0;
        for (byte b : buffer) {
            sum += Byte.toUnsignedLong(b) & 0xFF;
        }
        checksum = ~sum & 0xFFFFFFFFFFFFL;

        Assert.assertEquals(checksum, TarUtils.computeCheckSum(buffer));

        byte[] emptyBuffer = new byte[0];
        checksum = TarUtils.computeCheckSum(emptyBuffer);
        Assert.assertEquals(checksum, 0);
    }
}