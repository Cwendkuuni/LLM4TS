package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '1', '0'};
        long expected = 0175501L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '1', '8'}; // invalid byte '8'
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String expected = "tar.file.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.file.gz";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assert.assertEquals(name, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "012345";
        String result = new String(buffer, 0, 6, StandardCharsets.UTF_8);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "012345 \0";
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456789012345 \0";
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1112233344455566L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0111223334445556 \0";
        String result = new String(buffer, 0, offset, StandardCharsets.UTF_8);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{
                0x75, 0x72, 0x61, 0x73, 0x73, 0x61, 0x63, 0x68, 0x65, 0x20, 0x74, 0x65, 0x73, 0x74,
                0x32, 0x20, 0x61, 0x6c, 0x65, 0x65, 0x63, 0x74, 0x31, 0x20, 0x63, 0x6f, 0x6d};
        long expected = 0x42c002e2e803b7bcL;
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }
}