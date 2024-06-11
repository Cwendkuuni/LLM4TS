package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidOctalNumber() {
        byte[] buffer = new byte[]{ '0', '2', '7', '0', '7', '5', '4', '5', '0', '7' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0270754507L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{ '0', '2', '7', '0', '7', '5', '4', 'h', '5', '0', '7' }; // 'h' is not a valid octal number
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctal_EmptyBuffer() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testParseOctal_NullTrailingByte() {
        byte[] buffer = new byte[]{ '0', '2', '7', '0', '7', '5', '4', '5', '0' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(027075450L, result);
    }

    @Test
    public void testParseOctal_LeadingSpace() {
        byte[] buffer = new byte[]{ ' ', '2', '7', '0', '7', '5', '4', '5', '0', '7' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(027075450L, result);
    }

    @Test
    public void testParseOctal_TrailingSpace() {
        byte[] buffer = new byte[]{ '0', '2', '7', '0', '7', '5', '4', '5', '0', ' ' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(027075450L, result);
    }

    @Test
    public void testParseName_ValidName() {
        byte[] buffer = new byte[]{ 't', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 'd', 'a', 't', 'a' };
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("tar.data", result);
    }

    @Test
    public void testParseName_EmptyBuffer() {
        byte[] buffer = new byte[0];
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("", result);
    }

    @Test
    public void testFormatNameBytes_ValidName() {
        String name = "tar.data";
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(12, offset);

        String name2 = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assert.assertEquals("tar.data", name2);
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "longernamethanbuffer";
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        // Check the output size
        Assert.assertEquals(5, offset);
        // Check the truncated name
        String name2 = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assert.assertEquals("longer", name2);
    }

    @Test
    public void testFormatNameBytes_NameShorterThanBuffer() {
        String name = "shortname";
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(name.length(), offset);

        String name2 = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assert.assertEquals("shortname", name2);
    }

    @Test
    public void testFormatNameBytes_NameEmpty() {
        String name = "";
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(0, offset);

        String name2 = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assert.assertEquals("", name2);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidNumber() {
        long value = 12345678L;
        byte[] buffer = new byte[10];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        String expected = "01234567543";
        for (int i = 0; i < buffer.length; i++) {
            Assert.assertEquals((byte) expected.charAt(i), buffer[i]);
        }
    }

    @Test
    public void testFormatOctalBytes_ValidNumber() {
        long value = 12345678L;

        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        Assert.assertEquals(offset, 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_NumberTooLong() {
        long value = Long.MAX_VALUE + 1; // Long.MAX_VALUE +1 is too long to convert to octal

        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidNumber() {
        long value = 12345678L;

        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        Assert.assertEquals(offset, 12);
    }

    @Test
    public void testComputeCheckSum_ValidBuffer() {
        byte[] buffer = new byte[]{ 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
        long checksum = TarUtils.computeChecksum(buffer);

        long expectedChecksum = 0x0102030405060708L ^ 0xFF; // Checksum of the buffer is the XOR of all bytes with 0xFF
        Assert.assertEquals(expectedChecksum, checksum);
    }

    @Test
    public void testComputeCheckSum_EmptyBuffer() {
        byte[] buffer = new byte[0];
        long checksum = TarUtils.computeChecksum(buffer);
        Assert.assertEquals(0L, checksum);
    }
}