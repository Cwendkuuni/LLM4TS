import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', 0};
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'T', 'e', 's', 't', 0, 'E', 'n', 'd'};
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("Test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "JUnit";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertArrayEquals(new byte[]{'J', 'U', 'n', 'i', 't'}, buf);
        assertEquals(5, newOffset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);
        assertArrayEquals(new byte[]{'0', '1', '7', 3, 5}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int newOffset = TarUtils.formatOctalBytes(123, buf, offset, length);
        assertArrayEquals(new byte[]{54, 31, 32, 0, ' '}, buf);
        assertEquals(5, newOffset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {'t', 'e', 's', 't'};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(448, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOctalDigitException() {
        byte[] buffer = {'a', 'b', 'c', 0};
        TarUtils.parseOctal(buffer, 0, 4);
    }

}