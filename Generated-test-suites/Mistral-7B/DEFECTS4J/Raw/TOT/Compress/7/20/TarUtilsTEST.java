package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        for (int i = 0; i < 16; ++i) {
            long expected = (long) i;
            long result = TarUtils.parseOctal(buffer, 0, buffer.length);
            assertEquals(expected, result);
        }

        byte[] bufferWithTrailingNull = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', 0};
        long resultWithTrailingNull = TarUtils.parseOctal(bufferWithTrailingNull, 0, bufferWithTrailingNull.length);
        assertEquals(Long.parseLong("01234567", 8), resultWithTrailingNull);
    }

    /**
     * Test parseOctal method with invalid octal strings.
     */
    @Test
    public void testParseOctalInvalid() {
        byte[] bufferWithInvalidDigit = new byte[]{'0', '1', '2', '9', '4', '5', '6', '7'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferWithInvalidDigit, 0, bufferWithInvalidDigit.length));

        byte[] bufferWithLengthExceeded = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferWithLengthExceeded, 0, bufferWithLengthExceeded.length));
    }

    /**
     * Test parseName method with valid entry names.
     */
    @Test
    public void testParseName() {
        byte[] bufferWithValidName = new byte[]{'name', ' ', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
        String expectedResult = "name.tar.gz";
        String result = TarUtils.parseName(bufferWithValidName, 0, bufferWithValidName.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseName method with invalid input (lack of name).
     */
    @Test
    public void testParseNameInvalid() {
        byte[] bufferWithInvalidInput = new byte[]{'.'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(bufferWithInvalidInput, 0, bufferWithInvalidInput.length));

        byte[] bufferWithInvalidInputLengthExceeded = new byte[]{'n', 'a', 'm', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', '.'};
        String resultWithInvalidInputLengthExceeded = TarUtils.parseName(bufferWithInvalidInputLengthExceeded, 0, bufferWithInvalidInputLengthExceeded.length);
        assertEquals("name.tar.gz", resultWithInvalidInputLengthExceeded);
    }

    /**
     * Test formatNameBytes method with valid names.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "name.tar.gz";
        byte[] expectedBuffer = new byte[]{'n', 'a', 'm', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
        byte[] buffer = new byte[expectedBuffer.length];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertArrayEquals(expectedBuffer, buffer);
        assertEquals(offset, buffer.length);
    }

    /**
     * Test formatNameBytes method with invalid input (lack of name).
     */
    @Test
    public void testFormatNameBytesInvalid() {
        String name = "";
        byte[] buffer = new byte[1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(-1, offset);
    }

    /**
     * Test formatUnsignedOctalString method with valid octal numbers.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{'0', '0', '1', '2', '3', '4', '5', '7', '7', '7'};
        Assert.assertArrayEquals(expectedBuffer, buffer);
        assertEquals(offset, buffer.length);
    }

    /**
     * Test formatUnsignedOctalString method with invalid input (value is negative).
     */
    @Test
    public void testFormatUnsignedOctalStringInvalid() {
        long value = -12345;
        byte[] buffer = new byte[10];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length));
    }

    /**
     * Test formatOctalBytes method with valid octal numbers.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{'0', '0', '1', '2', '3', '4', '5', '7', '7', '7', '0', ' '};
        Assert.assertArrayEquals(expectedBuffer, buffer);
        assertEquals(offset, buffer.length);
    }

    /**
     * Test formatOctalBytes method with invalid input (value is negative).
     */
    @Test
    public void testFormatOctalBytesInvalid() {
        long value = -12345;
        byte[] buffer = new byte[12];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, 0, buffer.length));
    }

    /**
     * Test parseOctal method with valid checksums.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[]{'0', '0', '2', '0', '7', '5', '4', '3', '1', '2', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0'};
        long expectedChecksum = 131255;
        long checksum = TarUtils.computeCheckSum(header);
        assertEquals(expectedChecksum, checksum);
    }

    /**
     * Test parseOctal method with invalid input (checksum length is incorrect).
     */
    @Test
    public void testComputeCheckSumInvalid() {
        byte[] header = new byte[]{'0', '0', '2', '0', '7', '5', '4', '3', '1', '2', '1', '1', '0', '0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.computeCheckSum(header));
    }
}