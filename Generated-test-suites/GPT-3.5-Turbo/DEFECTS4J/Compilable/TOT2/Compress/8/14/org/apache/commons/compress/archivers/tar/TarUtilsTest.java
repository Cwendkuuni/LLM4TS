package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import org.junit.Assert;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {50, 51, 0};
        long result = TarUtils.parseOctal(buffer, 0, 3);
        Assert.assertEquals(43, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0, 0};
        String result = TarUtils.parseName(buffer, 0, 3);
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes("Test", buf, 0, 4);
        Assert.assertArrayEquals(new byte[] {'T', 'e', 's', 't', 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(8, buf, 0, 5);
        Assert.assertArrayEquals(new byte[] {48, 48, 48, 56, 0}, buf);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[4];
        int offset = TarUtils.formatOctalBytes(10, buf, 0, 3);
        Assert.assertArrayEquals(new byte[] {'1', '0', 32, 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[4];
        int offset = TarUtils.formatLongOctalBytes(15, buf, 0, 3);
        Assert.assertArrayEquals(new byte[] {'1', '5', 32, 0}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[4];
        int offset = TarUtils.formatCheckSumOctalBytes(20, buf, 0, 3);
        Assert.assertArrayEquals(new byte[] {'2', '0', 0, 32}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {10, 20, 30, 40};
        long result = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(100, result);
    }

}