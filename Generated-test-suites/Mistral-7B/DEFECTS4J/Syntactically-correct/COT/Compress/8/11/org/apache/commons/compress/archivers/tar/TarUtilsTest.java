import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long expected = 0175436L; // Octal representation of the byte array
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8', 'X'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'f', 'o', 'o', 'b', 'a', 'r', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String expected = "foo.tar.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo.tar.gz";
        byte[] buffer = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'f', 'o', 'o', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '7', '7', '6', '5'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '7', '7', '6', '5', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '7', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '7', '7', '6', '5', ' ', '0', ' '};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 't', 'a', 'r', ' ', 'h', 'e', 'a', 'd', 'e', 'r', ' ', '5', 'b', '0'};
        long expected = 3386686379L;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}