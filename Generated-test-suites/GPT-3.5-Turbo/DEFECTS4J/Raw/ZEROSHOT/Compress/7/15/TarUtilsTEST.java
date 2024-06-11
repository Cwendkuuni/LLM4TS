import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 0;
        buffer[1] = (byte) '1';
        buffer[2] = (byte) '2';
        buffer[3] = (byte) '3';
        buffer[4] = (byte) '4';
        buffer[5] = (byte) '5';
        buffer[6] = (byte) '6';
        buffer[7] = (byte) '7';
        buffer[8] = (byte) '8';
        buffer[9] = (byte) '9';

        long expected = 123456789;
        long result = TarUtils.parseOctal(buffer, 1, 9);

        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) 'h';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 'l';
        buffer[3] = (byte) 'l';
        buffer[4] = (byte) 'o';
        buffer[5] = 0;
        buffer[6] = (byte) 'w';
        buffer[7] = (byte) 'o';
        buffer[8] = (byte) 'r';
        buffer[9] = (byte) 'l';

        String expected = "hello";
        String result = TarUtils.parseName(buffer, 0, 5);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buf = new byte[10];
        buf[0] = (byte) 'w';
        buf[1] = (byte) 'o';
        buf[2] = (byte) 'r';
        buf[3] = (byte) 'l';
        buf[4] = (byte) 'd';

        int offset = TarUtils.formatNameBytes(name, buf, 5, 5);

        assertEquals(10, offset);
        assertEquals((byte) 'h', buf[5]);
        assertEquals((byte) 'e', buf[6]);
        assertEquals((byte) 'l', buf[7]);
        assertEquals((byte) 'l', buf[8]);
        assertEquals((byte) 'o', buf[9]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789;
        byte[] buf = new byte[20];

        TarUtils.formatUnsignedOctalString(value, buf, 0, 20);

        String expected = "0000000000017715631545";
        String result = new String(buf, 0, 20);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[12];

        int offset = TarUtils.formatOctalBytes(value, buf, 0, 12);

        assertEquals(12, offset);
        assertEquals((byte) '0', buf[0]);
        assertEquals((byte) '0', buf[1]);
        assertEquals((byte) '0', buf[2]);
        assertEquals((byte) '0', buf[3]);
        assertEquals((byte) '0', buf[4]);
        assertEquals((byte) '0', buf[5]);
        assertEquals((byte) '0', buf[6]);
        assertEquals((byte) '1', buf[7]);
        assertEquals((byte) '7', buf[8]);
        assertEquals((byte) '7', buf[9]);
        assertEquals((byte) '6', buf[10]);
        assertEquals((byte) '3', buf[11]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[12];

        int offset = TarUtils.formatLongOctalBytes(value, buf, 0, 12);

        assertEquals(12, offset);
        assertEquals((byte) '0', buf[0]);
        assertEquals((byte) '0', buf[1]);
        assertEquals((byte) '0', buf[2]);
        assertEquals((byte) '0', buf[3]);
        assertEquals((byte) '0', buf[4]);
        assertEquals((byte) '0', buf[5]);
        assertEquals((byte) '0', buf[6]);
        assertEquals((byte) '1', buf[7]);
        assertEquals((byte) '2', buf[8]);
        assertEquals((byte) '3', buf[9]);
        assertEquals((byte) '4', buf[10]);
        assertEquals((byte) '5', buf[11]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[12];

        int offset = TarUtils.formatCheckSumOctalBytes(value, buf, 0, 12);

        assertEquals(12, offset);
        assertEquals((byte) '0', buf[0]);
        assertEquals((byte) '0', buf[1]);
        assertEquals((byte) '0', buf[2]);
        assertEquals((byte) '0', buf[3]);
        assertEquals((byte) '0', buf[4]);
        assertEquals((byte) '0', buf[5]);
        assertEquals((byte) '0', buf[6]);
        assertEquals((byte) '1', buf[7]);
        assertEquals((byte) '2', buf[8]);
        assertEquals((byte) '3', buf[9]);
        assertEquals((byte) '4', buf[10]);
        assertEquals((byte) ' ', buf[11]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = (byte) 'h';
        buf[1] = (byte) 'e';
        buf[2] = (byte) 'l';
        buf[3] = (byte) 'l';
        buf[4] = (byte) 'o';
        buf[5] = 0;
        buf[6] = (byte) 'w';
        buf[7] = (byte) 'o';
        buf[8] = (byte) 'r';
        buf[9] = (byte) 'l';

        long expected = 736;
        long result = TarUtils.computeCheckSum(buf);

        assertEquals(expected, result);
    }
}