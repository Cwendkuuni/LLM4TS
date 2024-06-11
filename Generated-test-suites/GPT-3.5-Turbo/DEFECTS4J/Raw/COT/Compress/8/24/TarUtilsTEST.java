import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 49, 50}; // '0', '1', '2'
        int offset = 0;
        int length = 3;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(012, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67}; // 'A', 'B', 'C'
        int offset = 0;
        int length = 3;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "DEF";
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(3, newOffset);
        String result = new String(buf);
        assertEquals("DEF", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01234;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String result = new String(buffer);
        assertEquals("001234", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 01234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int newOffset = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(6, newOffset);
        String result = new String(buf);
        assertEquals("001234 ", result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 01234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int newOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(6, newOffset);
        String result = new String(buf);
        assertEquals("001234 ", result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 01234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int newOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(6, newOffset);
        String result = new String(buf);
        assertEquals("001234\0 ", result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {65, 66, 67}; // 'A', 'B', 'C'
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(198, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_Exception() {
        byte[] buffer = {65, 66, 67}; // 'A', 'B', 'C'
        int offset = 0;
        int length = 3;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_Exception() {
        long value = 123456789;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }
}