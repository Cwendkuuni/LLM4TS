import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 49; // ascii value of '1'
        buffer[1] = 50; // ascii value of '2'
        buffer[2] = 51; // ascii value of '3'
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 65; // ascii value of 'A'
        buffer[1] = 66; // ascii value of 'B'
        buffer[2] = 67; // ascii value of 'C'
        String expected = "ABC";
        String result = TarUtils.parseName(buffer, 0, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "ABC";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 3;
        int expected = 3;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "00123";
        String result = new String(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 6;
        int expected = 6;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int expected = 5;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 6;
        int expected = 6;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = 49; // ascii value of '1'
        buf[1] = 50; // ascii value of '2'
        buf[2] = 51; // ascii value of '3'
        long expected = 222;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }
}