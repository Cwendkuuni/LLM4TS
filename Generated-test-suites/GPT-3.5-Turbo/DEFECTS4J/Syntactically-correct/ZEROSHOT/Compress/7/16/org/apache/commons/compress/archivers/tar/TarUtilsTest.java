import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = '1';
        buffer[1] = '2';
        buffer[2] = '3';
        buffer[3] = 0;
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 'T';
        buffer[1] = 'e';
        buffer[2] = 's';
        buffer[3] = 't';
        buffer[4] = 0;
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("Test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 4);
        assertEquals(4, offset);
        assertEquals('T', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 5);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, 5);
        assertEquals(5, offset);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals(' ', buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, 5);
        assertEquals(5, offset);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals(' ', buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 5);
        assertEquals(5, offset);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = 'T';
        buffer[1] = 'e';
        buffer[2] = 's';
        buffer[3] = 't';
        buffer[4] = 0;
        long sum = TarUtils.computeCheckSum(buffer);
        assertEquals(408, sum);
    }
}