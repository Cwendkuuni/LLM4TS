package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(1355L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '8'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("my file.tar.gz", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myfile.tar.gz";
        byte[] buffer = new byte[256];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(256, result);
        // Add assertions to check the content of buffer here
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1355L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0201111111";
        Assert.assertArrayEquals(expected.getBytes(), buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1355L;
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0201111111 ";
        Assert.assertArrayEquals(expected.getBytes(), buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x65, (byte) 0x6e, (byte) 0x73, (byte) 0x73, (byte) 0x68, (byte) 0x65, (byte) 0x78, 0x0, (byte) 0x66, (byte) 0x6f, (byte) 0x72, (byte) 0x6d, (byte) 0x61, (byte) 0x74, 0x69, 0x6f, 0x6e};
        long result = TarUtils.computeCheckSum(buffer);
        long expected = 1868385129839581871L;
        Assert.assertEquals(expected, result);
    }
}