package org.apache.commons.compress.archivers.tar;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer1 = "     123  ".getBytes();
        long result1 = TarUtils.parseOctal(buffer1, 0, buffer1.length);
        Assert.assertEquals(83, result1);

        // Edge case: minimum length
        byte[] buffer2 = " 0".getBytes();
        long result2 = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        Assert.assertEquals(0, result2);

        // Error scenario: invalid byte
        try {
            byte[] buffer3 = "   abc   ".getBytes();
            long result3 = TarUtils.parseOctal(buffer3, 0, buffer3.length);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // As expected
        }
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer1 = "example.txt\0\0".getBytes();
        String result1 = TarUtils.parseName(buffer1, 0, buffer1.length);
        Assert.assertEquals("example.txt", result1);

        // Edge case: buffer length reached
        byte[] buffer2 = "example".getBytes();
        String result2 = TarUtils.parseName(buffer2, 0, buffer2.length);
        Assert.assertEquals("example", result2);

        // Edge case: NUL found
        byte[] buffer3 = "test\0filename".getBytes();
        String result3 = TarUtils.parseName(buffer3, 0, buffer3.length);
        Assert.assertEquals("test", result3);
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        String name1 = "example.txt";
        byte[] buf1 = new byte[20];
        int offset1 = TarUtils.formatNameBytes(name1, buf1, 0, 20);
        Assert.assertEquals(20, offset1);
        Assert.assertEquals("example.txt\0\0\0\0\0\0\0", new String(buf1));

        // Edge case: name longer than buffer
        String name2 = "thisfilenameislongerthanthebuffer";
        byte[] buf2 = new byte[10];
        int offset2 = TarUtils.formatNameBytes(name2, buf2, 0, 10);
        Assert.assertEquals(10, offset2);
        Assert.assertEquals("thisfilen\0", new String(buf2));

        // Edge case: buffer longer than name
        String name3 = "name";
        byte[] buf3 = new byte[10];
        int offset3 = TarUtils.formatNameBytes(name3, buf3, 0, 10);
        Assert.assertEquals(10, offset3);
        Assert.assertEquals("name\0\0\0\0\0", new String(buf3));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // Typical use case
        byte[] buf1 = new byte[10];
        TarUtils.formatUnsignedOctalString(123, buf1, 0, 10);
        Assert.assertEquals("        173", new String(buf1));

        // Edge case: value is 0
        byte[] buf2 = new byte[10];
        TarUtils.formatUnsignedOctalString(0, buf2, 0, 10);
        Assert.assertEquals("0000000000", new String(buf2));

        // Error scenario: value will not fit in buffer
        try {
            byte[] buf3 = new byte[2];
            TarUtils.formatUnsignedOctalString(123, buf3, 0, 2);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // As expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        // Typical use case
        byte[] buf1 = new byte[10];
        int offset1 = TarUtils.formatOctalBytes(123, buf1, 0, 10);
        Assert.assertEquals(10, offset1);
        Assert.assertEquals("        173 ", new String(buf1));

        // Edge case: value is 0
        byte[] buf2 = new byte[10];
        int offset2 = TarUtils.formatOctalBytes(0, buf2, 0, 10);
        Assert.assertEquals(10, offset2);
        Assert.assertEquals("0000000000 ", new String(buf2));

        // Error scenario: value and trailer will not fit in buffer
        try {
            byte[] buf3 = new byte[1];
            TarUtils.formatOctalBytes(123, buf3, 0, 1);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // As expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        // Typical use case
        byte[] buf1 = new byte[10];
        int offset1 = TarUtils.formatLongOctalBytes(123, buf1, 0, 10);
        Assert.assertEquals(10, offset1);
        Assert.assertEquals("       173 ", new String(buf1));

        // Edge case: value is 0
        byte[] buf2 = new byte[10];
        int offset2 = TarUtils.formatLongOctalBytes(0, buf2, 0, 10);
        Assert.assertEquals(10, offset2);
        Assert.assertEquals("0000000000 ", new String(buf2));

        // Error scenario: value and trailer will not fit in buffer
        try {
            byte[] buf3 = new byte[1];
            TarUtils.formatLongOctalBytes(123, buf3, 0, 1);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // As expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // Typical use case
        byte[] buf1 = new byte[10];
        int offset1 = TarUtils.formatCheckSumOctalBytes(123, buf1, 0, 10);
        Assert.assertEquals(10, offset1);
        Assert.assertEquals("       173 ", new String(buf1));

        // Edge case: value is 0
        byte[] buf2 = new byte[10];
        int offset2 = TarUtils.formatCheckSumOctalBytes(0, buf2, 0, 10);
        Assert.assertEquals(10, offset2);
        Assert.assertEquals("0000000000 ", new String(buf2));

        // Error scenario: value and trailer will not fit in buffer
        try {
            byte[] buf3 = new byte[1];
            TarUtils.formatCheckSumOctalBytes(123, buf3, 0, 1);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // As expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        // Typical use case
        byte[] buf1 = "This is a test".getBytes();
        long sum1 = TarUtils.computeCheckSum(buf1);
        Assert.assertEquals(1517, sum1);

        // Edge case: buffer is empty
        byte[] buf2 = new byte[0];
        long sum2 = TarUtils.computeCheckSum(buf2);
        Assert.assertEquals(0, sum2);

        // Edge case: buffer is all zeros
        byte[] buf3 = new byte[10];
        long sum3 = TarUtils.computeCheckSum(buf3);
        Assert.assertEquals(0, sum3);
    }
}