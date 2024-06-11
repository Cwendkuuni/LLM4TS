import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', '4', '5', 0};
        assertEquals(12345L, TarUtils.parseOctal(buffer, 0, 6));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', '_', 'f', 'i', 'l', 'e', 0};
        assertEquals("test_file", TarUtils.parseName(buffer, 0, 10));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[10];
        String name = "test_data";
        TarUtils.formatNameBytes(name, buf, 0, 10);
        assertEquals("test_data", new String(buf).trim());
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);
        assertEquals("00123", new String(buffer).trim());
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[5];
        TarUtils.formatOctalBytes(50, buf, 0, 5);
        assertEquals("32   ", new String(buf).trim());
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[5];
        TarUtils.formatLongOctalBytes(100, buf, 0, 5);
        assertEquals("144  ", new String(buf).trim());
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[5];
        TarUtils.formatCheckSumOctalBytes(250, buf, 0, 5);
        assertEquals("372  ", new String(buf).trim());
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {'t', 'e', 's', 't'};
        assertEquals(448, TarUtils.computeCheckSum(buffer));
    }
}