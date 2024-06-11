import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {65, 66, 67, 68, 69, 70};
        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(1094861634, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 68, 69, 70};
        String result = TarUtils.parseName(buffer, 0, 6);
        assertEquals("ABCDEF", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "ABCDEF";
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        int newOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(6, newOffset);
        assertArrayEquals(new byte[]{65, 66, 67, 68, 69, 70}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1094861634;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{48, 48, 48, 48, 52, 52, 66, 50, 48, 0}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1094861634;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        int newOffset = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(8, newOffset);
        assertArrayEquals(new byte[]{48, 48, 48, 52, 52, 66, 50, 0}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1094861634;
        byte[] buffer = new byte[7];
        int offset = 0;
        int length = 7;
        int newOffset = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(7, newOffset);
        assertArrayEquals(new byte[]{48, 48, 48, 52, 52, 66, 0}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1094861634;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        int newOffset = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(8, newOffset);
        assertArrayEquals(new byte[]{48, 48, 48, 52, 52, 66, 0, 32}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {65, 66, 67, 68, 69, 70};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(390, result);
    }
}


