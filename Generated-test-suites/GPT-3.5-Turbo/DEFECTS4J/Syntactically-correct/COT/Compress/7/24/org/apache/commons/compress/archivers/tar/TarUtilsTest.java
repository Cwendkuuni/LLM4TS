import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        long expected = 12345678;
        buffer = Long.toOctalString(expected).getBytes();
        long actual = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        String expected = "testname";
        buffer = expected.getBytes();
        String actual = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testname";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = offset + length;
        int actual = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345678;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "0012345678";
        String actual = new String(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345678;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = offset + length;
        int actual = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345678;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = offset + length;
        int actual = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345678;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = offset + length;
        int actual = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        long expected = 123;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) expected;
        }
        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected * buf.length, actual);
    }

}