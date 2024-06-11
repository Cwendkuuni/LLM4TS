package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 0;
        buffer[1] = '1';
        buffer[2] = '2';
        buffer[3] = '3';
        buffer[4] = '4';
        buffer[5] = '5';
        buffer[6] = '6';
        buffer[7] = '7';
        buffer[8] = '8';
        buffer[9] = '9';

        long result = TarUtils.parseOctal(buffer, 0, 10);

        Assert.assertEquals(123456789L, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[10];

        int updatedOffset = TarUtils.formatOctalBytes(value, buffer, 0, 10);

        Assert.assertEquals(10, updatedOffset);
        Assert.assertEquals('1', buffer[0]);
        Assert.assertEquals('2', buffer[1]);
        Assert.assertEquals('3', buffer[2]);
        Assert.assertEquals('4', buffer[3]);
        Assert.assertEquals('5', buffer[4]);
        Assert.assertEquals('6', buffer[5]);
        Assert.assertEquals('7', buffer[6]);
        Assert.assertEquals('8', buffer[7]);
        Assert.assertEquals('9', buffer[8]);
        Assert.assertEquals(' ', buffer[9]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = 1;
        buffer[1] = 2;
        buffer[2] = 3;
        buffer[3] = 4;
        buffer[4] = 5;
        buffer[5] = 6;
        buffer[6] = 7;
        buffer[7] = 8;
        buffer[8] = 9;
        buffer[9] = 10;

        long sum = TarUtils.computeCheckSum(buffer);

        Assert.assertEquals(55, sum);
    }

}