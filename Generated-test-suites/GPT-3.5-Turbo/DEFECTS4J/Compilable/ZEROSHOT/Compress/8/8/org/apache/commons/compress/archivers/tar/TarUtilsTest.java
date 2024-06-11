package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 49, 50, 51};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(123, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = {48, 49, 50, 65};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {97, 98, 99, 100, 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("abcd", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(4, offset);
        Assert.assertEquals(116, buffer[0]);
        Assert.assertEquals(101, buffer[1]);
        Assert.assertEquals(115, buffer[2]);
        Assert.assertEquals(116, buffer[3]);
        Assert.assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[4];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.assertEquals(49, buffer[0]);
        Assert.assertEquals(50, buffer[1]);
        Assert.assertEquals(51, buffer[2]);
        Assert.assertEquals(0, buffer[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithInvalidValue() {
        long value = 1234567890;
        byte[] buffer = new byte[2];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(4, offset);
        Assert.assertEquals(49, buffer[0]);
        Assert.assertEquals(50, buffer[1]);
        Assert.assertEquals(51, buffer[2]);
        Assert.assertEquals(32, buffer[3]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(5, offset);
        Assert.assertEquals(49, buffer[0]);
        Assert.assertEquals(50, buffer[1]);
        Assert.assertEquals(51, buffer[2]);
        Assert.assertEquals(52, buffer[3]);
        Assert.assertEquals(32, buffer[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(4, offset);
        Assert.assertEquals(49, buffer[0]);
        Assert.assertEquals(50, buffer[1]);
        Assert.assertEquals(51, buffer[2]);
        Assert.assertEquals(0, buffer[3]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {49, 50, 51, 52};
        long sum = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(202, sum);
    }
}