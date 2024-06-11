package org.apache.commons.compress.archivers.tar.test;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarUtils;

import org.apache.commons.compress.utils.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test class for TarUtils methods.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctalValid() {
        byte[] testData = new byte[]{0x2, 0x3, 0x7, 0x4, 0x5, 0x6};
        long result = TarUtils.parseOctal(testData, 0, testData.length);
        assertEquals(0x172456, result);
    }

    /**
     * Test parseOctal method with invalid data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] testData = new byte[]{0x2, 0x3, 0x7, 0xA, 0x4, 0x5, 0x6};
        TarUtils.parseOctal(testData, 0, testData.length);
    }

    /**
     * Test parseName method with valid data.
     */
    @Test
    public void testParseNameValid() {
        byte[] testData = new byte[]{'t', 'a', 'r', 0, '.', 't', 'e', 's', 't', 0};
        String result = TarUtils.parseName(testData, 0, testData.length);
        assertEquals("tar.test", result);
    }

    /**
     * Test formatNameBytes method with valid data.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "my_test_file";
        byte[] testData = new byte[256];
        int offset = TarUtils.formatNameBytes(name, testData, 0, testData.length);
        assertArrayEquals(name.getBytes(StandardCharsets.UTF_8), testData, 0, offset);
    }

    /**
     * Test parseOctal and formatOctalBytes methods with the same data.
     */
    @Test
    public void testParseOctalFormatOctalBytes() {
        long value = 0x172456;
        byte[] testData = new byte[10];
        int offset = 0;

        TarUtils.formatOctalBytes(value, testData, offset, testData.length);
        long parsedValue = TarUtils.parseOctal(testData, 0, testData.length);

        assertEquals(value, parsedValue);
    }

    /**
     * Test formatUnsignedOctalString method with long value.
     */
    @Test
    public void testFormatUnsignedOctalStringLong() {
        long value = 0x172456;
        byte[] testData = new byte[256];
        int offset = 0;

        TarUtils.formatUnsignedOctalString(value, testData, offset, testData.length);

        String hexValue = Long.toHexString(value);
        StringBuilder expectedOctal = new StringBuilder();
        for (int i = 0; i < hexValue.length(); ++i) {
            int hex = Integer.parseUnsignedInt(hexValue.substring(i, i + 1), 16);
            expectedOctal.append((char) ('0' + (hex >>> 3)));
            expectedOctal.append((char) ('0' + (hex & 0x7)));
        }

        String result = new String(testData, 0, offset + expectedOctal.length());
        assertEquals(expectedOctal.toString(), result);
    }

    /**
     * Test formatOctalBytes method with long value.
     */
    @Test
    public void testFormatOctalBytesLong() {
        long value = 0x172456;
        byte[] testData = new byte[30];
        int offset = 0;

        TarUtils.formatOctalBytes(value, testData, offset, testData.length);

        String hexValue = Long.toHexString(value);
        StringBuilder expectedOctal = new StringBuilder();
        for (int i = 0; i < hexValue.length(); ++i) {
            int hex = Integer.parseUnsignedInt(hexValue.substring(i, i + 1), 16);
            expectedOctal.append((char) ('0' + (hex >>> 3)));
            expectedOctal.append((char) ('0' + (hex & 0x7)));
        }

        expectedOctal.append(" ");
        expectedOctal.append(0);

        String result = new String(testData, 0, offset + expectedOctal.length());
        assertEquals(expectedOctal.toString(), result);
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] testData = "TarUtils Testing".getBytes(StandardCharsets.UTF_8);
        long checksum = TarUtils.computeCheckSum(testData);

        byte[] expectedChecksumBytes = {"0", "67", "3", "7", "74", "75", "72", "65", "73", "74", "61", "72", "73", "65", "72", "0"};
        byte[] computedChecksumBytes = TarUtils.intToOctalBytes((int) checksum & 0xFFFFFFFFL);

        assertArrayEquals(expectedChecksumBytes, computedChecksumBytes);
    }

    /**
     * Test intToOctalBytes method.
     */
    @Test
    public void testIntToOctalBytes() {
        int value = 0x12345678;
        byte[] result = TarUtils.intToOctalBytes(value);

        byte[] expected = new byte[]{"0", "1", "0", "1", "0", "1", "0", "1", "1", "0", "1", "0", "1", "1", "0", "1", "1", "0", "1", "1", "1", "0", "1", "1", "1", "1"};

        assertArrayEquals(expected, result);
    }

    /**
     * Test parseLongOctalBytes method.
     */
    @Test
    public void testParseLongOctalBytes() {
        byte[] testData = new byte[]{"0", "1", "0", "1", "0", "1", "0", "1", "1", "0", "1", "0", "1", "1", "0", "1", "1", "0", "1", "1", "1", "0", "1", "1", "1", "1"};
        long result = TarUtils.parseLongOctalBytes(testData, 0, testData.length);

        assertEquals(0x12345678, result);
    }

    /**
     * Test formatCheckSumOctalBytes method.
     */
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x12345678;
        byte[] testData = new byte[32];
        int offset = 0;

        TarUtils.formatCheckSumOctalBytes(value, testData, offset, testData.length);

        byte[] expected = new byte[]{"0", "1", "0", "1", "0", "1", "0", "1", "1", "0", "1", "0", "1", "1", "0", "1", "1", "0", "1", "1", "1", "0", "1", "1", "1", "1", "0", "0", "0", "0"};

        assertArrayEquals(expected, testData);
    }

    /**
     * Test parseOctal and formatOctalBytes methods with the same data but reversed.
     */
    @Test
    public void testParseOctalFormatOctalBytesReversed() {
        long value = 0x123456;
        byte[] testData = new byte[10];
        int offset = 0;

        TarUtils.formatOctalBytes(value, testData, offset, testData.length);
        long parsedValue = TarUtils.parseOctal(testData, 0, testData.length);

        assertEquals(value, parsedValue);
    }

    /**
     * Test parseOctal and formatOctalBytes methods with empty input.
     */
    @Test
    public void testParseOctalFormatOctalBytesEmpty() {
        byte[] testData = new byte[0];
        long parsedValue = TarUtils.parseOctal(testData, 0, testData.length);

        assertEquals(0, parsedValue);
    }

    /**
     * Test parseName method with empty input.
     */
    @Test
    public void testParseNameEmpty() {
        byte[] testData = new byte[0];
        String result = TarUtils.parseName(testData, 0, testData.length);

        assertEquals("", result);
    }
}