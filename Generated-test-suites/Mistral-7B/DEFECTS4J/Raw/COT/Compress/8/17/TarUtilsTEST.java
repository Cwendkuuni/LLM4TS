package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.computeCheckSum;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatCheckSumOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;

/**
 * TarUtils Test
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        long result = TarUtils.parseOctal(buffer, 0, 7);
        Assert.assertEquals(0123456L, result);
    }

    /**
     * Test parseOctal with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '8', '5', '6'}; // byte 5 is 8
        TarUtils.parseOctal(buffer, 0, 8);
    }

    /**
     * Test parseName method.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', 0};
        String result = TarUtils.parseName(buffer, 0, 6);
        Assert.assertEquals("hello", result);
    }

    /**
     * Test parseName with invalid input.
     */
    @Test
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) 'a', 0};
        String result = TarUtils.parseName(buffer, 0, 7);
        Assert.assertEquals("hello", result);

        byte[] buffer2 = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) 'a', (byte) 'b', 0};
        String result2 = TarUtils.parseName(buffer2, 0, 8);
        Assert.assertEquals("hello", result2.substring(0, 5)); // the name is truncated
    }

    /**
     * Test formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 20);

        String formattedName = new String(ByteUtils.toBytes(buffer, 0, offset), "US-ASCII");
        Assert.assertEquals("testName", formattedName);
    }

    /**
     * Test formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
        Assert.assertEquals(10, offset);

        String formattedValue = new String(ByteUtils.toBytes(buffer, 0, 10), "US-ASCII");
        Assert.assertEquals("0123456", formattedValue);
    }

    /**
     * Test formatLongOctalBytes method.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[11];

        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, 11);
        Assert.assertEquals(11, offset);

        String formattedValue = new String(ByteUtils.toBytes(buffer, 0, 11), "US-ASCII");
        Assert.assertEquals("0123456 ", formattedValue);
    }

    /**
     * Test formatCheckSumOctalBytes method.
     */
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890;
        byte[] buffer = new byte[13];

        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 13);
        Assert.assertEquals(13, offset);

        String formattedValue = new String(ByteUtils.toBytes(buffer, 0, 13), "US-ASCII");
        Assert.assertEquals("01234567654321", formattedValue.substring(0, 11)); // trailing null and space are not checked
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};

        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += BYTE_MASK & buffer[i];
        }

        Assert.assertEquals(computedChecksum, checksum);
    }
}