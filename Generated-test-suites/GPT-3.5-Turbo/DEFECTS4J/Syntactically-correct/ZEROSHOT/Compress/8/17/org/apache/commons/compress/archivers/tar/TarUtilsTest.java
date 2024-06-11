import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {0x31, 0x32, 0x33}; // ASCII values of '1', '2', '3'
        long expected = 123;

        long result = TarUtils.parseOctal(buffer, 0, 3);

        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = {0x31, 0x34, 0x33}; // ASCII values of '1', '4', '3'

        TarUtils.parseOctal(buffer, 0, 3);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {0x61, 0x62, 0x63}; // ASCII values of 'a', 'b', 'c'
        String expected = "abc";

        String result = TarUtils.parseName(buffer, 0, 3);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buffer = new byte[5];
        buffer[3] = 0x20; // ASCII value of space
        buffer[4] = 0x00; // ASCII value of null
        int offset = 0;
        int length = 5;
        byte[] expected = {0x61, 0x62, 0x63, 0x20, 0x00};

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(offset + length, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        byte[] expected = {0x30, 0x30, 0x31, 0x32, 0x33}; // ASCII values of '0', '0', '1', '2', '3'

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        Assert.assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithOverflow() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        byte[] expected = {0x30, 0x30, 0x31, 0x32, 0x20}; // ASCII values of '0', '0', '1', '2', ' '

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(offset + length, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;
        byte[] expected = {0x30, 0x30, 0x31, 0x20}; // ASCII values of '0', '0', '1', ' '

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);

        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(offset + length, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        byte[] expected = {0x30, 0x30, 0x31, 0x20, 0x00}; // ASCII values of '0', '0', '1', ' ', '\0'

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(offset + length, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {0x61, 0x62, 0x63}; // ASCII values of 'a', 'b', 'c'
        long expected = 294;

        long result = TarUtils.computeCheckSum(buffer);

        Assert.assertEquals(expected, result);
    }

}