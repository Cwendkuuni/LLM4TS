import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = { '1', '2', '3', ' ', '\0' };
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(123L, result);
    }

    @Test
    public void testParseOctal_InvalidInput() {
        byte[] buffer = { 'A', 'B', 'C', ' ', '\0' };

        try {
            TarUtils.parseOctal(buffer, 0, 4);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = { 't', 'e', 's', 't', '\0', 'e', 'x', 't', 'r', 'a' };
        String result = TarUtils.parseName(buffer, 0, 9);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes("test", buffer, 0, 10);
        assertEquals("test\0\0\0\0\0", new String(buffer, 0, 10));
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(123L, buffer, 0, 5);
        assertEquals("00123", new String(buffer, 0, 5));
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatOctalBytes(123L, buffer, 0, 4);
        assertEquals("00123 ", new String(buffer, 0, 5));
    }

    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = { 't', 'e', 's', 't' };
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(448, result);
    }
}