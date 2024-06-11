package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 48;
        buffer[1] = 51;
        buffer[2] = 55;
        buffer[3] = 56;
        buffer[4] = 53;
        long result = TarUtils.parseOctal(buffer, 0, 5);
        Assert.assertEquals(3785, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[10];
        buffer[0] = 48;
        buffer[1] = 51;
        buffer[2] = 55;
        buffer[3] = 88;
        buffer[4] = 53;
        TarUtils.parseOctal(buffer, 0, 5);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 65;
        buffer[1] = 66;
        buffer[2] = 67;
        String result = TarUtils.parseName(buffer, 0, 3);
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "XYZ";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        Assert.assertEquals(5, result);
        Assert.assertEquals(88, buf[0]);
        Assert.assertEquals(89, buf[1]);
        Assert.assertEquals(90, buf[2]);
        Assert.assertEquals(0, buf[3]);
        Assert.assertEquals(0, buf[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 3785;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        Assert.assertEquals(3, buffer[0]);
        Assert.assertEquals(7, buffer[1]);
        Assert.assertEquals(5, buffer[2]);
        Assert.assertEquals(0, buffer[3]);
        Assert.assertEquals(0, buffer[4]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalid() {
        long value = 9999;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 3785;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        Assert.assertEquals(5, result);
        Assert.assertEquals(3, buf[0]);
        Assert.assertEquals(7, buf[1]);
        Assert.assertEquals(5, buf[2]);
        Assert.assertEquals(32, buf[3]);
        Assert.assertEquals(0, buf[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 3785;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        Assert.assertEquals(5, result);
        Assert.assertEquals(3, buf[0]);
        Assert.assertEquals(7, buf[1]);
        Assert.assertEquals(5, buf[2]);
        Assert.assertEquals(32, buf[3]);
        Assert.assertEquals(0, buf[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 3785;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        Assert.assertEquals(5, result);
        Assert.assertEquals(3, buf[0]);
        Assert.assertEquals(7, buf[1]);
        Assert.assertEquals(5, buf[2]);
        Assert.assertEquals(0, buf[3]);
        Assert.assertEquals(32, buf[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = 65;
        buf[1] = 66;
        buf[2] = 67;
        long result = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(198, result);
    }
}