package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0, (byte) 0};
        long expected = 0x755L << 12 | 0x644L; // 0755 in octal is 0177511 in binary, which is 0111 0101 1111 1101 in hexadecimal, so the shift values for octal digits are 12, 8, 3, and 0
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(expected, result);

        byte[] invalidBuffer = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0x8};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 4);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] trailingZeroBuffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0, (byte) 0};
        long resultWithTrailingZero = TarUtils.parseOctal(trailingZeroBuffer, 0, 5);
        assertEquals(expected, resultWithTrailingZero);

        byte[] leadingZeroBuffer = new byte[]{0, (byte) 0755, (byte) 0644, (byte) 0};
        try {
            TarUtils.parseOctal(leadingZeroBuffer, 0, 4);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] missingTrailingBuffer = new byte[]{(byte) 0755, (byte) 0644};
        try {
            TarUtils.parseOctal(missingTrailingBuffer, 0, 2);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] binaryBuffer = new byte[]{(byte) 0x55, (byte) 0x44, (byte) 0x33, (byte) 0x22};
        long expected = 0x55443322L;
        long result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, 4);
        assertEquals(expected, result);

        byte[] validOctalBuffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0, (byte) 0};
        long resultFromOctal = TarUtils.parseOctalOrBinary(validOctalBuffer, 0, 4);
        assertEquals(expected, resultFromOctal);

        byte[] invalidOctalBuffer = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0x8};
        try {
            TarUtils.parseOctalOrBinary(invalidOctalBuffer, 0, 4);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] binaryTooLargeBuffer = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        try {
            TarUtils.parseOctalOrBinary(binaryTooLargeBuffer, 0, 8);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] trueBuffer = new byte[]{0x1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(trueBuffer, 0);
        assertEquals(expected, result);

        byte[] falseBuffer = new byte[]{0};
        boolean resultFromFalse = TarUtils.parseBoolean(falseBuffer, 0);
        assertFalse(resultFromFalse);
    }

    @Test
    public void testParseName() {
        byte[] nameBuffer = new byte[]{0x6E, 0x61, 0x6D, 0x65, 0x73, 0x73, 0x61, 0x72, 0x79, 0x5C, 0x73, 0x75, 0x72, 0x76, 0x65, 0x72, 0x00};
        String expected = "javalib/java.jar";
        String result = TarUtils.parseName(nameBuffer, 0, nameBuffer.length);
        assertEquals(expected, result);

        byte[] emptyBuffer = new byte[]{0};
        String resultFromEmpty = TarUtils.parseName(emptyBuffer, 0, 0);
        assertEquals("", resultFromEmpty);

        byte[] missingTrailingNullBuffer = new byte[]{0x6E, 0x61, 0x6D, 0x65, 0x73, 0x73, 0x61, 0x72, 0x79, 0x5C, 0x73, 0x75, 0x72, 0x65, 0x72};
        try {
            TarUtils.parseName(missingTrailingNullBuffer, 0, nameBuffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "javalib/java.jar";
        byte[] buffer = new byte[name.length() + 1];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0x6E, 0x61, 0x6D, 0x65, 0x73, 0x73, 0x61, 0x72, 0x79, 0x5C, 0x73, 0x75, 0x72, 0x65, 0x72, (byte) 0};
        assertArrayEquals(expected, buffer, 0, result);

        String nameWithLeadingSlash = "/javalib/java.jar";
        name = nameWithLeadingSlash.substring(1);
        buffer = new byte[name.length() + 1];
        result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 1, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0x2, 0x3, 0x4, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        assertArrayEquals(expected, buffer, 0, 10);

        value = 0;
        buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedWhenZero = new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        assertArrayEquals(expectedWhenZero, buffer, 0, 10);

        long tooLongValue = Long.MAX_VALUE + 1;
        try {
            TarUtils.formatUnsignedOctalString(tooLongValue, buffer, 0, buffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0x2, 0x3, 0x4, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2, 0x0};
        assertArrayEquals(expected, buffer, 0, result);

        long valueWithSpace = 0x12345;
        int resultWithSpace = TarUtils.formatOctalBytes(valueWithSpace, buffer, 0, buffer.length + 1);
        byte[] expectedWithSpace = new byte[]{0x2, 0x3, 0x4, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2, 0x0, 0x20};
        assertArrayEquals(expectedWithSpace, buffer, 0, resultWithSpace);

        long valueWithLeadingZero = 0x012345;
        byte[] bufferWithLeadingZero = new byte[12];
        int resultWithLeadingZero = TarUtils.formatOctalBytes(valueWithLeadingZero, bufferWithLeadingZero, 0, buffer.length);
        byte[] expectedWithLeadingZero = new byte[]{0x0, 0x2, 0x3, 0x4, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2, 0x0};
        assertArrayEquals(expectedWithLeadingZero, bufferWithLeadingZero, 0, resultWithLeadingZero);

        long valueTooLarge = Long.MAX_VALUE + 1;
        try {
            TarUtils.formatOctalBytes(valueTooLarge, buffer, 0, buffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x12345;
        byte[] buffer = new byte[11];
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0x2, 0x3, 0x4, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2};
        assertArrayEquals(expected, buffer, 0, result);

        long valueWithSpace = 0x12345;
        int resultWithSpace = TarUtils.formatLongOctalBytes(valueWithSpace, buffer, 0, buffer.length + 1);
        byte[] expectedWithSpace = new byte[]{0x2, 0x3, 0x4, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2, 0x20};
        assertArrayEquals(expectedWithSpace, buffer, 0, resultWithSpace);

        long valueWithLeadingZero = 0x012345;
        byte[] bufferWithLeadingZero = new byte[11];
        int resultWithLeadingZero = TarUtils.formatLongOctalBytes(valueWithLeadingZero, bufferWithLeadingZero, 0, buffer.length);
        byte[] expectedWithLeadingZero = new byte[]{0x0, 0x2, 0x3, 0x4, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2};
        assertArrayEquals(expectedWithLeadingZero, bufferWithLeadingZero, 0, resultWithLeadingZero);

        long valueTooLarge = Long.MAX_VALUE + 1;
        try {
            TarUtils.formatLongOctalBytes(valueTooLarge, buffer, 0, buffer.length);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0x12345;
        byte[] buffer = new byte[9];
        int result = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0x34, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2};
        assertArrayEquals(expected, buffer, 0, result);

        long valueWithLeadingZero = 0x012345;
        byte[] bufferWithLeadingZero = new byte[9];
        int resultWithLeadingZero = TarUtils.formatLongOctalOrBinaryBytes(valueWithLeadingZero, bufferWithLeadingZero, 0, buffer.length);
        byte[] expectedWithLeadingZero = new byte[]{0x0, 0x34, 0x5, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2};
        assertArrayEquals(expectedWithLeadingZero, bufferWithLeadingZero, 0, resultWithLeadingZero);

        long valueTooLargeForOctal = Long.MAX_VALUE + 1;
        byte[] bufferTooLargeForOctal = new byte[9];
        int resultTooLargeForOctal = TarUtils.formatLongOctalOrBinaryBytes(valueTooLargeForOctal, bufferTooLargeForOctal, 0, buffer.length);
        byte[] expectedTooLargeForOctal = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x0, 0x0, 0x0, 0x0, 0x2};
        assertArrayEquals(expectedTooLargeForOctal, bufferTooLargeForOctal, 0, resultTooLargeForOctal);

        long valueTooLargeForBinary = Long.MAX_VALUE + 1L << 8;
        byte[] bufferTooLargeForBinary = new byte[9];
        int resultTooLargeForBinary = TarUtils.formatLongOctalOrBinaryBytes(valueTooLargeForBinary, bufferTooLargeForBinary, 0, buffer.length);
        byte[] expectedTooLargeForBinary = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x0, 0x0, 0x0, 0x2};
        assertArrayEquals(expectedTooLargeForBinary, bufferTooLargeForBinary, 0, resultTooLargeForBinary);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expected = 0x9B312C3F45D48B75L;
        assertEquals(expected, checksum);
    }
}