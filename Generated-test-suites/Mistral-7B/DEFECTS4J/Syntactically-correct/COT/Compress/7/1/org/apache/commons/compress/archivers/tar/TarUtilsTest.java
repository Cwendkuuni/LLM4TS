import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        long expected = 0123456L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', 'x', '5'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        String expected = "tar.txt";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.txt";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'t', 'a', 'r', '.', 't', 'x', 't', 0, 0, 0};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '0', '0'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringTooLarge() {
        long value = 123456789012345L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        long expected = TarUtils.computeCheckSum(buffer);
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertArrayEquals(expected, buffer, 0, offset);
    }
}