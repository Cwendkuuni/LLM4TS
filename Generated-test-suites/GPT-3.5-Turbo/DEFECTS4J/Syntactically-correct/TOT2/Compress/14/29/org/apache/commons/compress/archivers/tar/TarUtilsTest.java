import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {49, 0, 53, 56}; // Corresponds to "1 58"
        assertEquals(58, TarUtils.parseOctal(buffer, 0, 4));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[] {127};
        assertEquals(127, TarUtils.parseOctalOrBinary(buffer1, 0, 1));

        byte[] buffer2 = new byte[] {-128, 0, 0, 1};
        assertEquals(2147483649L, TarUtils.parseOctalOrBinary(buffer2, 0, 4));
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[] {1};
        assertTrue(TarUtils.parseBoolean(buffer1, 0));

        byte[] buffer2 = new byte[] {0};
        assertFalse(TarUtils.parseBoolean(buffer2, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {97, 98, 99, 0, 0}; // Corresponds to "abc"
        assertEquals("abc", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];
        int offset = 0;
        assertEquals(5, TarUtils.formatNameBytes("abcd", buf, offset, 5));
        assertArrayEquals(new byte[] {97, 98, 99, 100, 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[3];
        TarUtils.formatUnsignedOctalString(123, buf, 0, 3);
        assertArrayEquals(new byte[] {49, 55, 51}, buf);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[3];
        assertEquals(3, TarUtils.formatOctalBytes(123, buf, 0, 3));
        assertArrayEquals(new byte[] {49, 50, 32}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[3];
        assertEquals(3, TarUtils.formatLongOctalBytes(520, buf, 0, 3));
        assertArrayEquals(new byte[] {50, 49, 32}, buf);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        byte[] buf = new byte[4];
        assertEquals(4, TarUtils.formatLongOctalOrBinaryBytes(545, buf, 0, 4));
        assertArrayEquals(new byte[] {0, 0, 2, 33}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[4];
        assertEquals(4, TarUtils.formatCheckSumOctalBytes(678, buf, 0, 4));
        assertArrayEquals(new byte[] {54, 55, 32, 0}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] {49, 50, 51};
        assertEquals(150, TarUtils.computeCheckSum(buffer));
    }
}