package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0, (byte) 075, (byte) 064, 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(067564L, result);

        byte[] invalidBuffer = new byte[]{0, (byte) 075, (byte) 064, 1};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] missingTrailermNullBuffer = new byte[]{0, (byte) 075, (byte) 064};
        try {
            TarUtils.parseOctal(missingTrailermNullBuffer, 0, missingTrailermNullBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] leadingNULBuffer = new byte[]{0, 0, (byte) 075, (byte) 064};
        long resultLeadingNUL = TarUtils.parseOctal(leadingNULBuffer, 0, leadingNULBuffer.length);
        assertEquals(0L, resultLeadingNUL);

        byte[] bufferWithInvalidByte = new byte[]{0, (byte) 075, (byte) 064, 9};
        try {
            TarUtils.parseOctal(bufferWithInvalidByte, 0, bufferWithInvalidByte.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] binaryBuffer = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
        long resultBinary = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        assertEquals(1 * (1L << 56) + 2 * (1L << 48) + 3 * (1L << 40) + 4 * (1L << 32) + 5 * (1L << 24) + 6 * (1L << 16) + 7 * (1L << 8) + 0x7f, resultBinary);

        byte[] octalBuffer = new byte[]{0, (byte) 075, 0, 0};
        long resultOctal = TarUtils.parseOctalOrBinary(octalBuffer, 0, octalBuffer.length);
        assertEquals(0675L, resultOctal);

        byte[] binaryWithInvalidByteBuffer = new byte[]{0, 1, 2, 3, 4, 9, 6, 7};
        try {
            TarUtils.parseOctalOrBinary(binaryWithInvalidByteBuffer, 0, binaryWithInvalidByteBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] binaryTooLongBuffer = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        try {
            TarUtils.parseOctalOrBinary(binaryTooLongBuffer, 0, binaryTooLongBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        boolean resultTrue = TarUtils.parseBoolean(bufferTrue, 0);
        assertEquals(true, resultTrue);

        byte[] bufferFalse = new byte[]{0};
        boolean resultFalse = TarUtils.parseBoolean(bufferFalse, 0);
        assertEquals(false, resultFalse);

        byte[] bufferInvalidByte = new byte[]{1, 9};
        try {
            TarUtils.parseBoolean(bufferInvalidByte, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{73, 79, 73, 74, 68, 65, 72, 65, 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Script", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Script";
        byte[] buffer = new byte[10];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{73, 79, 73, 74, 68, 65, 72, 65, 0, 0}, buffer);
        assertEquals(result, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 42;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, '0', '0', '0', '0', '0', '4', '2', '0', '0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 42;
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, '0', '0', '0', '0', '0', '4', '2', ' ', '0'}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 42;
        byte[] buffer = new byte[9];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, '0', '0', '0', '0', '0', '4', '2', ' '}, buffer);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1234567890123456L;
        byte[] buffer = new byte[13];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890123456L;
        byte[] buffer = new byte[11];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, '0', '0', '0', '0', '0', '1', '2', '3', '4', '5', ' ', '0'}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, computedChecksum);
    }
}