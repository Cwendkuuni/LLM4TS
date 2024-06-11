
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51, 0, 52, 53};
        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {84, 101, 115, 116, 0, 0, 0};
        String result = TarUtils.parseName(buffer, 0, 7);
        assertEquals("Test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buffer = new byte[10];
        int result = TarUtils.formatNameBytes(name, buffer, 0, 4);
        assertEquals(4, result);
        assertEquals(84, buffer[0]);
        assertEquals(101, buffer[1]);
        assertEquals(115, buffer[2]);
        assertEquals(116, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(12, buffer, 0, 3);
        assertEquals(49, buffer[0]);
        assertEquals(50, buffer[1]);
        assertEquals(52, buffer[2]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[4];
        int result = TarUtils.formatOctalBytes(12, buffer, 0, 4);
        assertEquals(4, result);
        assertEquals(49, buffer[0]);
        assertEquals(50, buffer[1]);
        assertEquals(52, buffer[2]);
        assertEquals(32, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[3];
        int result = TarUtils.formatLongOctalBytes(12, buffer, 0, 3);
        assertEquals(3, result);
        assertEquals(49, buffer[0]);
        assertEquals(50, buffer[1]);
        assertEquals(32, buffer[2]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[4];
        int result = TarUtils.formatCheckSumOctalBytes(12, buffer, 0, 4);
        assertEquals(4, result);
        assertEquals(49, buffer[0]);
        assertEquals(50, buffer[1]);
        assertEquals(32, buffer[2]);
        assertEquals(0, buffer[3]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {49, 50, 51, 52, 53};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(255, result);
    }
}
