import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {48, 49, 50, 51, 52}; // "01234"
        long expected = 1234;

        long result = TarUtils.parseOctal(buffer, 0, buffer.length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {97, 98, 99, 100, 0}; // "abcd"
        String expected = "abcd";

        String result = TarUtils.parseName(buffer, 0, buffer.length);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] expected = new byte[] {116, 101, 115, 116};
        byte[] buf = new byte[4];

        int result = TarUtils.formatNameBytes(name, buf, 0, buf.length);

        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;
        byte[] expected = new byte[] {50, 48, 64, 48};
        byte[] buf = new byte[4];

        TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);

        assertArrayEquals(expected, buf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringThrowsException() {
        long value = 123456;
        byte[] buf = new byte[3];

        TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456;
        byte[] expected = new byte[] {50, 48, 32, 0};
        byte[] buf = new byte[4];

        int result = TarUtils.formatOctalBytes(value, buf, 0, buf.length);

        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456;
        byte[] expected = new byte[] {50, 48, 0};
        byte[] buf = new byte[3];

        int result = TarUtils.formatLongOctalBytes(value, buf, 0, buf.length);

        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456;
        byte[] expected = new byte[] {50, 48, 0, 32};
        byte[] buf = new byte[4];

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);

        assertArrayEquals(expected, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[] {49, 50, 51, 52}; // "1234"
        long expected = 49 + 50 + 51 + 52;

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(expected, result);
    }
}