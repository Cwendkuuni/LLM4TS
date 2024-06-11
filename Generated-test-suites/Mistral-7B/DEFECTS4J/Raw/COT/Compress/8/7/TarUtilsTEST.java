import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '2', '0', '0'};
        long expected = 0175420L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '2', 'x', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingNull() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '2'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'m', 'y', ' ','f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String expected = "my file.tar.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my file.tar.gz";
        byte[] buffer = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'m', 'y', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', 0, 0};
        Assert.assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '0', '1', '2', '0', '1', '4', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0'};
        Assert.assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678;
        byte[] buffer = new byte[11];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '1', '0', '1', '0', '1', '2', '0', '1', '4', '0', '1', '1', '1', '1', '1', '1', '1', '1', '0', ' '};
        Assert.assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x123456789abcdef0;
        byte[] buffer = new byte[12];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0'};
        Assert.assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x123456789abcdef0;
        byte[] buffer = new byte[13];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0', ' '};
        Assert.assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'m', 'y', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        long expected = 1022312084232998624L;
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }
}