package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', ' '};
        long expectedResult = 01234567L;
        long actualResult = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseOctal method with invalid input (missing trailing space).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_MissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctal method with invalid input (invalid byte).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid binary input.
     */
    @Test
    public void testParseOctalOrBinary_ValidBinaryInput() {
        byte[] buffer = new byte[]{0x81, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        long expectedResult = 123456L;
        long actualResult = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseOctalOrBinary method with valid octal input.
     */
    @Test
    public void testParseOctalOrBinary_ValidOctalInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long expectedResult = 0123456L;
        long actualResult = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBoolean_ValidInput() {
        byte[] buffer = new byte[]{1};
        boolean expectedResult = true;
        boolean actualResult = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBoolean_InvalidInput() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 0};
        String expectedResult = "abcdef";
        String actualResult = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "abcdef";
        byte[] buffer = new byte[10];
        int expectedLength = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(expectedLength, 10);
        String actualResult = new String(buffer, 0, expectedLength);
        Assert.assertEquals(name, actualResult);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 123456789L;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expectedResult = "012345671";
        String actualResult = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 123456789L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, buffer.length);
        String expectedResult = "012345671 ";
        String actualResult = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test formatLongOctalBytes method with valid input.
     */
    @Test
    public void testFormatLongOctalBytes_ValidInput() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, buffer.length);
        String expectedResult = "01234567012345";
        String actualResult = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input (octal).
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidOctalInput() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, buffer.length);
        String expectedResult = "01234567012345";
        String actualResult = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input (binary).
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidBinaryInput() {
        long value = 123456789012345L;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, buffer.length);
        String expectedResult = "000111110111011001111101111111";
        String actualResult = ByteUtils.byteArrayToHexString(buffer);
        Assert.assertEquals(expectedResult, actualResult);
    }

    
}