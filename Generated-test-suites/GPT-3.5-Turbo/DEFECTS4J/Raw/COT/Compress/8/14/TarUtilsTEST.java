import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {49, 50, 51, 0}; // ASCII values for '1', '2', '3', NULL
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {'h', 'e', 'l', 'l', 'o', 0}; // ASCII values for 'h', 'e', 'l', 'l', 'o', NULL
        String expected = "hello";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] expected = new byte[] {'h', 'e', 'l', 'l', 'o'};
        byte[] result = new byte[5];
        TarUtils.formatNameBytes(name, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] expected = new byte[] {49, 50, 51}; // ASCII values for '1', '2', '3'
        byte[] result = new byte[3];
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] expected = new byte[] {49, 50, 51, 32, 0}; // ASCII values for '1', '2', '3', ' ', NULL
        byte[] result = new byte[5];
        TarUtils.formatOctalBytes(value, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] expected = new byte[] {49, 50, 51, 32}; // ASCII values for '1', '2', '3', ' '
        byte[] result = new byte[4];
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] expected = new byte[] {49, 50, 51, 0, 32}; // ASCII values for '1', '2', '3', NULL, ' '
        byte[] result = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] {49, 50, 51}; // ASCII values for '1', '2', '3'
        long expected = 150;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

}