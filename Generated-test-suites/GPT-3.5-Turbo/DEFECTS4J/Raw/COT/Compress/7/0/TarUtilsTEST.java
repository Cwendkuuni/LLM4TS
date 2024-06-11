import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51}; // ASCII values of '1', '2', '3'
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {116, 101, 115, 116}; // ASCII values of 't', 'e', 's', 't'
        String expected = "test";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] expected = {116, 101, 115, 116}; // ASCII values of 't', 'e', 's', 't'
        byte[] result = new byte[4];
        TarUtils.formatNameBytes(name, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] expected = {49, 50, 51}; // ASCII values of '1', '2', '3'
        byte[] result = new byte[3];
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] expected = {49, 50, 51, 32, 0}; // ASCII values of '1', '2', '3', ' ', 'null'
        byte[] result = new byte[5];
        TarUtils.formatOctalBytes(value, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] expected = {49, 50, 51, 32}; // ASCII values of '1', '2', '3', ' '
        byte[] result = new byte[4];
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] expected = {49, 50, 51, 0, 32}; // ASCII values of '1', '2', '3', 'null', ' '
        byte[] result = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {49, 50, 51}; // ASCII values of '1', '2', '3'
        long expected = 150;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }
}