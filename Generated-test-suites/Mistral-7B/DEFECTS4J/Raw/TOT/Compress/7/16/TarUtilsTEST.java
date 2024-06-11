package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 075, (byte) 064, (byte) 062, (byte) ' ', (byte) 0, (byte) ' '};
        long expected = 0114320;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals("parseOctal test failed", expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 0};
        String expected = "abcd";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("parseName test failed", expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        Assert.assertEquals("formatNameBytes test failed", name, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "012345";
        String actual = new String(buffer, 0, 7); // exclude trailing null
        Assert.assertEquals("formatUnsignedOctalString test failed", expected, actual);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "012345  ";
        String actual = new String(buffer, 0, offset); // exclude trailing null
        Assert.assertEquals("formatOctalBytes test failed", expected, actual);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] input = new byte[]{(byte) 0x1A, (byte) 0x2B, (byte) 0x3C, (byte) 0x4D, (byte) 0x5E};
        long expected = TarUtils.computeCheckSum(input);
        long actual = TarUtils.computeCheckSum(input);
        Assert.assertEquals("computeCheckSum test failed", expected, actual);
    }
}