import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 49, 50}; // ASCII values for '0', '1', '2'
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        long expected = 012; // Octal value for 10
        Assert.assertEquals("parseOctal method failed", expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {72, 101, 108, 108, 111}; // ASCII values for 'H', 'e', 'l', 'l', 'o'
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        String expected = "Hello";
        Assert.assertEquals("parseName method failed", expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Hello";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        int expected = 5;
        Assert.assertEquals("formatNameBytes method failed", expected, result);
        Assert.assertArrayEquals("formatNameBytes method failed", new byte[] {72, 101, 108, 108, 111, 0, 0, 0, 0, 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 10;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = {48, 50, 0, 0, 0}; // ASCII values for '0', '2'
        Assert.assertArrayEquals("formatUnsignedOctalString method failed", expected, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 10;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        int expected = 5;
        Assert.assertEquals("formatOctalBytes method failed", expected, result);
        Assert.assertArrayEquals("formatOctalBytes method failed", new byte[] {48, 50, 32, 0, 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 10;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        int expected = 5;
        Assert.assertEquals("formatLongOctalBytes method failed", expected, result);
        Assert.assertArrayEquals("formatLongOctalBytes method failed", new byte[] {48, 49, 0, 0, 0}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 10;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        int expected = 5;
        Assert.assertEquals("formatCheckSumOctalBytes method failed", expected, result);
        Assert.assertArrayEquals("formatCheckSumOctalBytes method failed", new byte[] {48, 49, 0, 32, 0}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {72, 101, 108, 108, 111}; // ASCII values for 'H', 'e', 'l', 'l', 'o'
        long result = TarUtils.computeCheckSum(buf);
        long expected = 532; // Sum of ASCII values
        Assert.assertEquals("computeCheckSum method failed", expected, result);
    }

}