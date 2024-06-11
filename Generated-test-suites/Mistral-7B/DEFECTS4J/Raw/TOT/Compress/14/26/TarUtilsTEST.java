package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarConstants.MAXID;
import static org.apache.commons.compress.archivers.tar.TarConstants.MAXSIZE;

/**
 * This class contains unit tests for the utility methods in {@link TarUtils}.
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidOctalString() {
        byte[] buffer = "0755".getBytes();
        long expected = 0755L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseOctal_EmptyBuffer() {
        byte[] buffer = new byte[0];
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testParseOctal_ValidBinaryString() {
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0x80, 0};
        long expected = 1234567890L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte_Octal() {
        byte[] buffer = "075AB".getBytes();
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_MissingTrailingNul_Octal() {
        byte[] buffer = "0755 ".getBytes();
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_MissingTrailingNul_Binary() {
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0x80};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test
    public void testParseBoolean_True() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseBoolean_False() {
        byte[] buffer = new byte[]{0};
        boolean expected = false;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseName_ValidName() {
        byte[] buffer = "filename.tar.gz".getBytes();
        String expected = "filename.tar.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes_ValidName() {
        String name = "filename.tar.gz";
        byte[] buffer = new byte[TarConstants.NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        Assert.assertEquals(name, actual);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidNumber() {
        long value = 755;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0755";
        String actual = new String(buffer, 0, TarConstants.UIDLEN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalBytes_ValidNumber() {
        long value = 1234567890L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 1];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456755";
        String actual = new String(buffer, 0, TarConstants.UIDLEN + 1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidNumber() {
        long value = 1234567890123L;
        byte[] buffer = new byte[TarConstants.UIDLEN + TarConstants.UIDLEN];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0x80, 0, 0, 0, 0, 0, 0, 0, 0};
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test
    public void testComputeCheckSum_ValidBuffer() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long expected = 1298587120L;
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }
}