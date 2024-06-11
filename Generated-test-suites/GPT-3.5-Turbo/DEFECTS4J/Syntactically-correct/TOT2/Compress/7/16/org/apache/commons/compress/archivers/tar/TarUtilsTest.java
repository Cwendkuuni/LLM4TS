import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal_basic() {
        byte[] buffer = { '1', '2', '3' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseOctal_withLeadingSpaces() {
        byte[] buffer = { ' ', ' ', '1', '2', '3' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidInput() {
        byte[] buffer = { '1', '2', 'x', '4' };
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_basic() {
        byte[] buffer = { 'H', 'e', 'l', 'l', 'o', 0, 'W', 'o', 'r', 'l', 'd' };
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Testing";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, name.length());
        String result = new String(buffer, 0, name.length());
        assertEquals("Testing\0\0\0\0", result);
    }

    @Test
    public void testFormatUnsignedOctalString_smallValue() {
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(7, buffer, 0, buffer.length);
        String result = new String(buffer).trim();
        assertEquals("7", result);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(123, buffer, 0, 8);
        String result = new String(buffer, 0, 8);
        assertEquals("00000123 ", result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = { 'T', 'e', 's', 't' };
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(448, result);
    }

}