import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 48;
        buffer[1] = 55;
        buffer[2] = 52;

        long result = TarUtils.parseOctal(buffer, 0, 3);

        assertEquals(724L, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 104;
        buffer[1] = 101;
        buffer[2] = 108;
        buffer[3] = 108;
        buffer[4] = 111;

        String result = TarUtils.parseName(buffer, 0, 5);

        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buffer = new byte[10];

        int result = TarUtils.formatNameBytes(name, buffer, 0, 5);

        assertEquals(5, result);
        assertEquals(104, buffer[0]);
        assertEquals(101, buffer[1]);
        assertEquals(108, buffer[2]);
        assertEquals(108, buffer[3]);
        assertEquals(111, buffer[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 724L;
        byte[] buffer = new byte[10];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, 5);

        assertEquals(48, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(52, buffer[2]);
        assertEquals(48, buffer[3]);
        assertEquals(48, buffer[4]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 724L;
        byte[] buffer = new byte[10];

        int result = TarUtils.formatOctalBytes(value, buffer, 0, 6);

        assertEquals(6, result);
        assertEquals(48, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(52, buffer[2]);
        assertEquals(32, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(0, buffer[5]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 724L;
        byte[] buffer = new byte[10];

        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, 6);

        assertEquals(6, result);
        assertEquals(48, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(52, buffer[2]);
        assertEquals(32, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(0, buffer[5]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 724L;
        byte[] buffer = new byte[10];

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 6);

        assertEquals(6, result);
        assertEquals(48, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(52, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(32, buffer[4]);
        assertEquals(0, buffer[5]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = 104;
        buffer[1] = 101;
        buffer[2] = 108;
        buffer[3] = 108;
        buffer[4] = 111;

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(532, result);
    }
}