package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 48;
        buffer[1] = 49;
        buffer[2] = 50;
        buffer[3] = 51;

        long result = TarUtils.parseOctal(buffer, 0, 4);

        Assert.assertEquals(123, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidDigit() {
        byte[] buffer = new byte[10];
        buffer[0] = 48;
        buffer[1] = 51;
        buffer[2] = 52;
        buffer[3] = 55;

        TarUtils.parseOctal(buffer, 0, 4);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 116;
        buffer[1] = 101;
        buffer[2] = 115;
        buffer[3] = 116;

        String result = TarUtils.parseName(buffer, 0, 4);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[10];

        int result = TarUtils.formatNameBytes(name, buffer, 0, 4);

        Assert.assertEquals(4, result);
        Assert.assertEquals(116, buffer[0]);
        Assert.assertEquals(101, buffer[1]);
        Assert.assertEquals(115, buffer[2]);
        Assert.assertEquals(116, buffer[3]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];

        int result = TarUtils.formatOctalBytes(123, buffer, 0, 8);

        Assert.assertEquals(8, result);
        Assert.assertEquals(48, buffer[0]);
        Assert.assertEquals(48, buffer[1]);
        Assert.assertEquals(48, buffer[2]);
        Assert.assertEquals(49, buffer[3]);
        Assert.assertEquals(50, buffer[4]);
        Assert.assertEquals(51, buffer[5]);
        Assert.assertEquals(32, buffer[6]);
        Assert.assertEquals(0, buffer[7]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];

        int result = TarUtils.formatLongOctalBytes(123, buffer, 0, 7);

        Assert.assertEquals(7, result);
        Assert.assertEquals(48, buffer[0]);
        Assert.assertEquals(48, buffer[1]);
        Assert.assertEquals(48, buffer[2]);
        Assert.assertEquals(49, buffer[3]);
        Assert.assertEquals(50, buffer[4]);
        Assert.assertEquals(51, buffer[5]);
        Assert.assertEquals(32, buffer[6]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];

        int result = TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 8);

        Assert.assertEquals(8, result);
        Assert.assertEquals(48, buffer[0]);
        Assert.assertEquals(48, buffer[1]);
        Assert.assertEquals(48, buffer[2]);
        Assert.assertEquals(49, buffer[3]);
        Assert.assertEquals(50, buffer[4]);
        Assert.assertEquals(51, buffer[5]);
        Assert.assertEquals(0, buffer[6]);
        Assert.assertEquals(32, buffer[7]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = 116;
        buffer[1] = 101;
        buffer[2] = 115;
        buffer[3] = 116;

        long result = TarUtils.computeCheckSum(buffer);

        Assert.assertEquals(412, result);
    }
}