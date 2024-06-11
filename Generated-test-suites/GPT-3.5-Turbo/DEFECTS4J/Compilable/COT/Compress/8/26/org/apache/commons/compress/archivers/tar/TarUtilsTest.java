package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0, 0, 0};
        int offset = 0;
        int length = 3;
        long result = TarUtils.parseOctal(buffer, offset, length);
        Assert.assertEquals(0, result);

        buffer = new byte[]{49, 50, 51};
        result = TarUtils.parseOctal(buffer, offset, length);
        Assert.assertEquals(83, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{49, 50, 75};
        int offset = 0;
        int length = 3;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{116, 101, 115, 116, 46, 116, 120, 116, 0, 0};
        int offset = 0;
        int length = 10;
        String result = TarUtils.parseName(buffer, offset, length);
        Assert.assertEquals("test.txt", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test.txt";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        Assert.assertEquals(10, result);
        Assert.assertArrayEquals(new byte[]{116, 101, 115, 116, 46, 116, 120, 116, 0, 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 83;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 56, 55, 0}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 83;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        Assert.assertEquals(10, result);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 56, 55, 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 83;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        Assert.assertEquals(10, result);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 56, 55, 0}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 83;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        Assert.assertEquals(10, result);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 56, 55, 0}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{116, 101, 115, 116, 46, 116, 120, 116, 0, 0};
        long result = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(984, result);
    }

}