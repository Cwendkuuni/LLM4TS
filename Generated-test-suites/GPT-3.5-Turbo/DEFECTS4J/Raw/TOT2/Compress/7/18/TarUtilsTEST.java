import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "123456".getBytes();
        long expected = 123456;
        assertEquals(expected, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = "testfile.tar".getBytes();
        String expected = "testfile.tar";
        assertEquals(expected, TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testfile.tar";
        byte[] buffer = new byte[20];
        int offset = 0;
        int length = 11;
        assertEquals(11, TarUtils.formatNameBytes(name, buffer, offset, length));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 6;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals("123456", new String(buffer, offset, length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        assertEquals(8, TarUtils.formatOctalBytes(value, buffer, offset, length));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        assertEquals(8, TarUtils.formatLongOctalBytes(value, buffer, offset, length));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        assertEquals(8, TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "testfile.tar".getBytes();
        long expected = 1143;
        assertEquals(expected, TarUtils.computeCheckSum(buffer));
    }
}