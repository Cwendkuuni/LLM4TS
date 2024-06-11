import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5, 6, 0};
        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(123456L, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{97, 98, 99, 0};
        String result = TarUtils.parseName(buffer, 0, 3);
        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[6];
        int updatedOffset = TarUtils.formatNameBytes(name, buffer, 0, 6);
        assertEquals(6, updatedOffset);
        assertEquals("test\0\0", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[6];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 6);
        assertEquals("000123", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[4];
        int updatedOffset = TarUtils.formatOctalBytes(123, buffer, 0, 4);
        assertEquals(4, updatedOffset);
        assertEquals("123 ", new String(buffer));
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[5];
        int updatedOffset = TarUtils.formatLongOctalBytes(1234, buffer, 0, 5);
        assertEquals(5, updatedOffset);
        assertEquals("1234 ", new String(buffer));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[4];
        int updatedOffset = TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 4);
        assertEquals(4, updatedOffset);
        assertEquals("123 ", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{1, 2, 3, 4, 5, 6};
        long sum = TarUtils.computeCheckSum(buf);
        assertEquals(21, sum);
    }
}