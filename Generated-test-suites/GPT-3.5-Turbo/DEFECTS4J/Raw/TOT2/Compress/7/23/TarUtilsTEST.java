import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {0, 1, 2, 3};
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0, 0, 0};
        String result = TarUtils.parseName(buffer, 0, 6);
        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes("Hello", buf, 0, 5);
        assertEquals(5, offset);
        assertArrayEquals(new byte[]{72, 101, 108, 108, 111}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buf, 0, 5);
        assertArrayEquals(new byte[]{49, 54, 3, 0, 0}, buf);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatOctalBytes(123, buf, 0, 5);
        assertEquals(5, offset);
        assertArrayEquals(new byte[]{49, 54, 3, 32, 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatLongOctalBytes(123, buf, 0, 5);
        assertEquals(5, offset);
        assertArrayEquals(new byte[]{49, 54, 3, 32, 0}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatCheckSumOctalBytes(123, buf, 0, 5);
        assertEquals(5, offset);
        assertArrayEquals(new byte[]{49, 54, 3, 0, 32}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(10, result);
    }

}