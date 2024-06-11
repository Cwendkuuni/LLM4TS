package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 48, 52, 48}; // "0040"
        long expected = 40L;
        long actual = TarUtils.parseOctal(buffer, 0, 4);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = {48, 48, 52, 65}; // "004A"
        TarUtils.parseOctal(buffer, 0, 4);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {116, 101, 115, 116, 46, 116, 120, 116}; // "test.txt"
        String expected = "test.txt";
        String actual = TarUtils.parseName(buffer, 0, 8);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test.txt";
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        int expected = 8;
        int actual = TarUtils.formatNameBytes(name, buf, offset, length);
        Assert.assertEquals(expected, actual);
        byte[] expectedBuf = {116, 101, 115, 116, 46, 116, 120, 116}; // "test.txt"
        Assert.assertArrayEquals(expectedBuf, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 40L;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = {48, 48, 52, 48}; // "0040"
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidLength() {
        long value = 100L;
        byte[] buffer = new byte[2];
        int offset = 0;
        int length = 2;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 40L;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        int expected = 4;
        int actual = TarUtils.formatOctalBytes(value, buf, offset, length);
        Assert.assertEquals(expected, actual);
        byte[] expectedBuf = {48, 48, 52, 32}; // "004 "
        Assert.assertArrayEquals(expectedBuf, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 40L;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        int expected = 4;
        int actual = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        Assert.assertEquals(expected, actual);
        byte[] expectedBuf = {48, 48, 52, 32}; // "004 "
        Assert.assertArrayEquals(expectedBuf, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 40L;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        int expected = 4;
        int actual = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        Assert.assertEquals(expected, actual);
        byte[] expectedBuf = {48, 48, 52, 0}; // "004 "
        Assert.assertArrayEquals(expectedBuf, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {116, 101, 115, 116}; // "test"
        long expected = 448L;
        long actual = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(expected, actual);
    }

}