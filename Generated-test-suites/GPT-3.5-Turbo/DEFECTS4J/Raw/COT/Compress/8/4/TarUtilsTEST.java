
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51, 52}; // ASCII representation of the number 1234
        int offset = 0;
        int length = 4;

        long result = TarUtils.parseOctal(buffer, offset, length);

        Assert.assertEquals(1234, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0}; // ASCII representation of the string "ABC"
        int offset = 0;
        int length = 4;

        String result = TarUtils.parseName(buffer, offset, length);

        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "ABC";
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        Assert.assertArrayEquals(new byte[]{65, 66, 67, 0}, buf);
        Assert.assertEquals(4, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        Assert.assertArrayEquals(new byte[]{49, 50, 51, 52}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_Exception() {
        long value = 123456789; // Value too big to fit in a buffer of length 4
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        Assert.assertArrayEquals(new byte[]{49, 50, 51, 52, 32, 0}, buf);
        Assert.assertEquals(6, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        Assert.assertArrayEquals(new byte[]{49, 50, 51, 52, 32}, buf);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        Assert.assertArrayEquals(new byte[]{49, 50, 51, 52, 32}, buf);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {49, 50, 51, 52}; // ASCII representation of the number 1234

        long result = TarUtils.computeCheckSum(buf);

        Assert.assertEquals(10, result);
    }
}

