package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {0, 0, 0};
        int offset = 0;
        int length = 3;
        long result = TarUtils.parseOctal(buffer, offset, length);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {97, 98, 99};
        int offset = 0;
        int length = 3;
        String result = TarUtils.parseName(buffer, offset, length);
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        Assert.assertEquals(3, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 5;
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String result = new String(buffer, offset, length);
        Assert.assertEquals("005", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 5;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        Assert.assertEquals(3, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 5;
        byte[] buf = new byte[2];
        int offset = 0;
        int length = 2;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        Assert.assertEquals(2, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 5;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        Assert.assertEquals(3, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {0, 0, 0};
        long result = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(0, result);
    }
}