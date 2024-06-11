import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        long expected = 0123456L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', 'a', '5'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r'};
        String expected = "tar.tar";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "mytarfile.tar";
        byte[] buffer = new byte[20];
        int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'m', 'y', 't', 'a', 'r', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, buffer, 0, length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890123456L;
        byte[] buffer = new byte[14];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890123456L;
        byte[] buffer = new byte[14];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'h', 'e', 'a', 'd', 'e', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r'};
        long expected = 1577827615L;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}