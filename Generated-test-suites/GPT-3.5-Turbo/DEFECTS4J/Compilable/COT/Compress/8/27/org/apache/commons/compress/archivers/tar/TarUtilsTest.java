package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) '1';
        buffer[1] = (byte) '2';
        buffer[2] = (byte) '3';
        buffer[3] = (byte) '4';

        long result = TarUtils.parseOctal(buffer, 0, 4);

        Assert.assertEquals(1234, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) 'a';
        buffer[1] = (byte) 'b';
        buffer[2] = (byte) 'c';
        buffer[3] = (byte) 'd';

        TarUtils.parseOctal(buffer, 0, 4);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[5];
        buffer[0] = (byte) 'h';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 'l';
        buffer[3] = (byte) 'l';
        buffer[4] = (byte) 'o';

        String result = TarUtils.parseName(buffer, 0, 5);

        Assert.assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[6];

        int result = TarUtils.formatNameBytes(name, buffer, 0, 6);

        Assert.assertArrayEquals(new byte[]{116, 101, 115, 116, 0, 0}, buffer);
        Assert.assertEquals(6, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];

        TarUtils.formatUnsignedOctalString(1234, buffer, 0, 5);

        Assert.assertArrayEquals(new byte[]{48, 52, 36, 12, 0}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueTooBig() {
        byte[] buffer = new byte[1];

        TarUtils.formatUnsignedOctalString(10000, buffer, 0, 1);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[5];

        int result = TarUtils.formatOctalBytes(1234, buffer, 0, 5);

        Assert.assertArrayEquals(new byte[]{48, 52, 36, 32, 0}, buffer);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[5];

        int result = TarUtils.formatLongOctalBytes(1234, buffer, 0, 5);

        Assert.assertArrayEquals(new byte[]{48, 52, 36, 32, 0}, buffer);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[5];

        int result = TarUtils.formatCheckSumOctalBytes(1234, buffer, 0, 5);

        Assert.assertArrayEquals(new byte[]{48, 52, 36, 0, 32}, buffer);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) '1';
        buffer[1] = (byte) '2';
        buffer[2] = (byte) '3';
        buffer[3] = (byte) '4';

        long result = TarUtils.computeCheckSum(buffer);

        Assert.assertEquals(394, result);
    }
}