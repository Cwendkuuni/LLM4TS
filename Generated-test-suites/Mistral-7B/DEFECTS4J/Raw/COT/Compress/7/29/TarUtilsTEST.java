import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        int offset = 0;
        int length = buffer.length;

        long expected = 0123456L;
        long actual = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '8'}; // '8' is an invalid octal digit
        int offset = 0;
        int length = buffer.length;

        long actual = TarUtils.parseOctal(buffer, offset, length);
        fail("Expected IllegalArgumentException but got: " + actual);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        int offset = 0;
        int length = buffer.length;

        String expected = "abcdefg";
        String actual = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefg";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 8;

        int actual = TarUtils.formatNameBytes(name, buf, offset, length);
        String result = new String(buf, 0, actual);
        assertEquals("abcdefg", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0123456;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String result = new String(buffer, 0, length);
        assertEquals("0123456", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0123456;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;

        int actual = TarUtils.formatOctalBytes(value, buf, offset, length);
        String result = new String(buf, 0, actual);
        assertEquals("0123456\u0020\u0000", result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{'a', 'b', 'c'};
        long expected = TarUtils.computeCheckSum(buf);

        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected, actual);
    }
}