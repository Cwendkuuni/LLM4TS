package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 49, 50};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(4160, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = {48, 49, 50, 51};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {97, 98, 99, 0, 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testParseNameWithTrailingNull() {
        byte[] buffer = {97, 98, 99, 0, 102};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];
        String name = "abc";
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        Assert.assertEquals(5, result);
        for (int i = 0; i < length; i++) {
            if (i < name.length()) {
                Assert.assertEquals((byte) name.charAt(i), buf[i]);
            } else {
                Assert.assertEquals(0, buf[i]);
            }
        }
    }

    @Test
    public void testFormatNameBytesWithNameLongerThanBuffer() {
        byte[] buf = new byte[2];
        String name = "abc";
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        Assert.assertEquals(2, result);
        Assert.assertEquals((byte) 'a', buf[0]);
        Assert.assertEquals((byte) 'b', buf[1]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        long value = 123;
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        for (int i = offset; i < length - 1; i++) {
            Assert.assertEquals((byte) '0', buffer[i]);
        }
        Assert.assertEquals((byte) '1', buffer[length - 1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueTooLarge() {
        byte[] buffer = new byte[2];
        long value = 123;
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[6];
        long value = 123;
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        Assert.assertEquals(6, result);
        for (int i = offset; i < result - 2; i++) {
            Assert.assertEquals((byte) '0', buf[i]);
        }
        Assert.assertEquals((byte) '1', buf[result - 2]);
        Assert.assertEquals((byte) ' ', buf[result - 1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesValueTooLarge() {
        byte[] buf = new byte[2];
        long value = 123;
        int offset = 0;
        int length = buf.length;
        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[5];
        long value = 123;
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        Assert.assertEquals(5, result);
        for (int i = offset; i < result - 1; i++) {
            Assert.assertEquals((byte) '0', buf[i]);
        }
        Assert.assertEquals((byte) ' ', buf[result - 1]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[6];
        long value = 123;
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        Assert.assertEquals(6, result);
        for (int i = offset; i < result - 2; i++) {
            Assert.assertEquals((byte) '0', buf[i]);
        }
        Assert.assertEquals((byte) 0, buf[result - 2]);
        Assert.assertEquals((byte) ' ', buf[result - 1]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {97, 98, 99};
        long result = TarUtils.computeCheckSum(buf);
        Assert.assertEquals(294, result);
    }
}