package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testBytes = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};

        long[] expectedValues = new long[]{0144, 0155, 0176, 0204, 0225, 0246, 0277, 030444};

        for (int i = 0; i < expectedValues.length; ++i) {
            long result = TarUtils.parseOctal(testBytes, 0, testBytes.length);
            assertEquals(expectedValues[i], result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidInput() {
        byte[] testBytes = new byte[]{(byte) '0', (byte) '8', (byte) '3'};
        TarUtils.parseOctal(testBytes, 0, testBytes.length);
    }

    @Test
    public void testParseName() {
        byte[] testBytes = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p'};

        String expectedName = "tar.gz.zip.tar.zip";
        String resultName = TarUtils.parseName(testBytes, 0, testBytes.length);
        assertEquals(expectedName, resultName);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myfile.tar.gz";
        byte[] testBytes = new byte[20];

        int resultOffset = TarUtils.formatNameBytes(name, testBytes, 0, testBytes.length);

        byte[] expectedBytes = new byte[]{
                (byte) 'm', (byte) 'y', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z',
                (byte) '\u0000', (byte) '\u0000', (byte) '\u0000', (byte) '\u0000', (byte) '\u0000', (byte) '\u0000',
                (byte) '\u0000', (byte) '\u0000', (byte) '\u0000'};

        assertArrayEquals(expectedBytes, testBytes, 0, testBytes.length);
        assertEquals(resultOffset, testBytes.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long testValue = 012345;
        byte[] testBytes = new byte[10];

        TarUtils.formatUnsignedOctalString(testValue, testBytes, 0, testBytes.length);

        byte[] expectedBytes = new byte[]{
                (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5'};

        assertArrayEquals(expectedBytes, testBytes, 0, testBytes.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithLengthTooShort() {
        long testValue = 012345;
        byte[] testBytes = new byte[3];

        TarUtils.formatUnsignedOctalString(testValue, testBytes, 0, testBytes.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long testValue = 0123456;
        byte[] testBytes = new byte[12];

        int resultOffset = TarUtils.formatOctalBytes(testValue, testBytes, 0, testBytes.length);

        byte[] expectedBytes = new byte[]{
                (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', '\u0020', '\u0000'};

        assertArrayEquals(expectedBytes, testBytes, 0, testBytes.length);
        assertEquals(resultOffset, testBytes.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesWithLengthTooShort() {
        long testValue = 0123456;
        byte[] testBytes = new byte[5];

        TarUtils.formatOctalBytes(testValue, testBytes, 0, testBytes.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long testValue = 0123456;
        byte[] testBytes = new byte[12];

        int resultOffset = TarUtils.formatCheckSumOctalBytes(testValue, testBytes, 0, testBytes.length);

        byte[] expectedBytes = new byte[]{
                (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', '\u0000', '\u0020'};

        assertArrayEquals(expectedBytes, testBytes, 0, testBytes.length);
        assertEquals(resultOffset, testBytes.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesWithLengthTooShort() {
        long testValue = 0123456;
        byte[] testBytes = new byte[4];

        TarUtils.formatCheckSumOctalBytes(testValue, testBytes, 0, testBytes.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] testBytes = new byte[]{(byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8, (byte) 0x9};

        long expectedChecksum = 0x1c3;
        long computedChecksum = TarUtils.computeChecksum(testBytes);

        assertEquals(expectedChecksum, computedChecksum);
    }
}