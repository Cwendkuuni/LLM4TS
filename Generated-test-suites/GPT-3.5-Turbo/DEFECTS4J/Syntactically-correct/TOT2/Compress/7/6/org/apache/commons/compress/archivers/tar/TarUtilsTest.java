import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {0, 1, 2, 3, 4};
        long result = TarUtils.parseOctal(buffer, 1, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0, 0, 0};
        String result = TarUtils.parseName(buffer, 0, 3);
        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buf = new byte[6];
        int offset = TarUtils.formatNameBytes(name, buf, 0, 4);
        assertEquals(4, offset);
        assertEquals('T', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
        assertEquals('t', buf[3]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);
        assertEquals('0', buffer[0]);
        assertEquals('1', buffer[1]);
        assertEquals('7', buffer[2]);
        assertEquals('3', buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatOctalBytes(123, buffer, 0, 5);
        assertEquals(5, offset);
        assertEquals('0', buffer[0]);
        assertEquals('1', buffer[1]);
        assertEquals('7', buffer[2]);
        assertEquals('3', buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatLongOctalBytes(123, buffer, 0, 5);
        assertEquals(5, offset);
        assertEquals('0', buffer[0]);
        assertEquals('1', buffer[1]);
        assertEquals('7', buffer[2]);
        assertEquals('3', buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 5);
        assertEquals(5, offset);
        assertEquals('0', buffer[0]);
        assertEquals('1', buffer[1]);
        assertEquals('7', buffer[2]);
        assertEquals('3', buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {(byte) 'a', (byte) 'b', (byte) 'c'};
        long sum = TarUtils.computeCheckSum(buffer);
        assertEquals(294, sum);
    }
}