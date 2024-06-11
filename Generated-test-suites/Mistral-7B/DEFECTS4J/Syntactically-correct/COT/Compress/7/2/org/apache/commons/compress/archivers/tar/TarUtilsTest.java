import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '4', '4', '4', '0', '0'};
        long expected = 014440L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '4', '4', '4', 'x', '0'}; // 'x' is an invalid octal digit
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', ' ', 'b', '.', 'c', '\u0000'};
        String expected = "a b.c";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myfile.txt";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'m', 'y', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '0', '1', '2', '3', '4', '5'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '0', '1', '2', '3', '4', '5', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'b', 'c'};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = 0; // write test code here to compute actual checksum
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesInvalidLength() {
        long value = 12345;
        byte[] buffer = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '0', '1', '2', '3', '4', '5', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, offset);
    }
}