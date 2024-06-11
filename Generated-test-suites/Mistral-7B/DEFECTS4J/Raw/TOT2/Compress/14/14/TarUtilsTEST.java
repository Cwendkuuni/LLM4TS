package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctalOrBinary;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseBoolean;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.computeCheckSum;

/**
 * TarUtilsTest class for testing various methods in TarUtils class
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal string
     */
    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '1', '0', '0', ' '};
        long expected = 01755100L;
        long result = parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid octal string
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '1', '0', 'x', ' '};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid binary string
     */
    @Test
    public void testParseOctalOrBinaryValidBinary() {
        byte[] buffer = new byte[]{(byte) 0x81, 0x01, 0x02, 0x03, 0x04, 0x05};
        long expected = 2102030405L;
        long result = parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctalOrBinary method with valid octal string
     */
    @Test
    public void testParseOctalOrBinaryValidOctal() {
        byte[] buffer = new byte[]{'0', '1', '6', '4', '4', '4', '4', '4', '0'};
        long expected = 01164444440L;
        long result = parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalid() {
        byte[] buffer = new byte[]{'0', '1', '6', '4', '4', '4', '4', '4', '5'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method with valid boolean byte
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean result = parseBoolean(buffer, 0);
        assertTrue(result);
    }

    /**
     * Test parseBoolean method with invalid boolean byte
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test parseName method with valid name
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{'m', 'y', 'File', '.', 't', 'x', 't', 0};
        String expected = "myFile.txt";
        String result = parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test formatNameBytes method with valid name and buffer
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "myFile.txt";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = formatNameBytes(name, buffer, offset, buffer.length);
        assertEquals(name.length(), length);
        for (int i = 0; i < length; i++) {
            assertEquals(name.charAt(i), (char) buffer[i]);
        }
    }

    /**
     * Test formatUnsignedOctalString method with valid number
     */
    @Test
    public void testFormatUnsignedOctalStringValid() {
        long number = 12345L;
        byte[] buffer = new byte[11];
        int offset = 0;
        formatUnsignedOctalString(number, buffer, offset, buffer.length);
        String expected = "012345";
        String result = new String(buffer, 0, offset + 6);
        assertEquals(expected, result);
    }

    /**
     * Test formatOctalBytes method with valid number
     */
    @Test
    public void testFormatOctalBytesValid() {
        long number = 12345L;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = formatOctalBytes(number, buffer, offset, buffer.length);
        assertEquals(10, length);
        assertArrayEquals(new byte[]{0, '0', '1', '2', '3', '4', '5', ' ', '0'}, buffer);
    }

    /**
     * Test computeCheckSum method with valid header
     */
    @Test
    public void testComputeCheckSumValid() {
        byte[] header = new byte[]{0x12, 0x34, 0x56, 0x78, 0x9a, 0xba, 0xcc, 0xdd};
        long expected = 0x14c852c6L;
        long result = computeCheckSum(header);
        assertEquals(expected, result);
    }
}