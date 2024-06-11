package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarConstants.MAXID;
import static org.apache.commons.compress.archivers.tar.TarConstants.MAXSIZE;

/**
 * Test cases for the {@link TarUtils} class.
 */
public class TarUtilsTest {

    /**
     * Test parsing of valid octal strings.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = "0755\0".getBytes(); // leading NUL for test
        long expected = 0755L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        buffer = "0644\0".getBytes();
        expected = 0644L;
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        buffer = "755\0".getBytes(); // no leading NUL
        expected = 755L;
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parsing of invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = "07xx\0".getBytes(); // invalid byte
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parsing of empty and null octal strings.
     */
    @Test
    public void testParseOctalEmptyAndNull() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0L, result);

        buffer = new byte[]{(byte) 0x00};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0L, result);
    }

    /**
     * Test parsing of binary numbers.
     */
    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{(byte) 0x81, 0x01}; // binary number
        long expected = 2051L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        buffer = "0755".getBytes(); // octal string
        expected = 0755L;
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parsing of invalid binary numbers.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalid() {
        byte[] buffer = new byte[]{(byte) 0x82, (byte) 0x80}; // binary number too large
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parsing of boolean values.
     */
    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{(byte) 0x01}; // true
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, result);

        buffer = new byte[]{(byte) 0x00}; // false
        expected = false;
        result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parsing of invalid boolean values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalid() {
        byte[] buffer = new byte[]{(byte) 0x02}; // invalid byte for boolean
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test parsing of entry names.
     */
    @Test
    public void testParseName() {
        byte[] buffer = "filename".getBytes();
        String expected = "filename";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatting of name into a buffer.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "filename";
        byte[] buffer = new byte[10];
        int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        String result = new String(buffer, 0, length);
        String expected = name;
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatting of unsigned octal strings.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0755L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        String result = new String(buffer, 0, buffer.length);
        String expected = "0755";
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatting of long integers as octal strings if they fit,
     * or as binary numbers otherwise.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long valueID = 1234567; // fits as octal string
        byte[] bufferID = new byte[TarConstants.UIDLEN];
        TarUtils.formatLongOctalOrBinaryBytes(valueID, bufferID, 0, bufferID.length);

        String resultID = new String(bufferID, 0, bufferID.length);
        String expectedID = "01234567";
        Assert.assertEquals(expectedID, resultID);

        long valueSize = 789123456789L; // too large for octal string, binary number instead
        byte[] bufferSize = new byte[TarConstants.MAXSIZE + 1];
        TarUtils.formatLongOctalOrBinaryBytes(valueSize, bufferSize, 0, bufferSize.length);

        String resultSize = new String(bufferSize, 0, bufferSize.length);
        String expectedSize = Long.toBinaryString(valueSize); // leading '0x' is not included in comparison
        Assert.assertEquals(expectedSize.substring(2), resultSize);
    }

    /**
     * Test writing of octal long integers into a buffer.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 01234567L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        String result = new String(buffer, 0, buffer.length);
        String expected = "01234567 ";
        Assert.assertEquals(expected, result);
    }

    /**
     * Test writing of binary long integers into a buffer.
     */
    @Test
    public void testFormatLongBinaryBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[TarConstants.MAXSIZE + 1];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length); // will write as binary number

        String result = new String(buffer, 0, buffer.length);
        String expected = Long.toBinaryString(value); // leading '0x' is not included in comparison
        Assert.assertEquals(expected.substring(2), result);
    }
}