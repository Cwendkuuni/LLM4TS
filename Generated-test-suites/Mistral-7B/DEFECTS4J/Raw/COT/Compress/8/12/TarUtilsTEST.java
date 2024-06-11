import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '1', '0', '0', ' '};
        long expected = 0177100;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '1', '8', '0', ' '}; // invalid byte '8'
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String expected = "tar.file.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.file.gz";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = TarUtils.formatNameBytes(name, buffer, offset, buffer.length);
        byte[] expected = new byte[]{'t', 'a', 'r', '.', 'f', 'i', 'l', 'e', '.', 'g', 'z', 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, buffer, 0, length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = TarUtils.formatUnsignedOctalString(value, buffer, offset, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', ' '};
        assertArrayEquals(expected, buffer, 0, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = TarUtils.formatOctalBytes(value, buffer, offset, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0', ' '};
        assertArrayEquals(expected, buffer, 0, length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '0', ' '};
        assertArrayEquals(expected, buffer, 0, length);
    }
}