import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {'1', '2', '3', '4', '5', 0};
        assertEquals(12345, TarUtils.parseOctal(buffer, 0, 6));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {'t', 'e', 's', 't', 0};
        assertEquals("test", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[5];
        assertEquals(5, TarUtils.formatNameBytes("test", buffer, 0, 5));
        assertArrayEquals(new byte[] {'t', 'e', 's', 't', 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(12345, buffer, 0, 5);
        assertArrayEquals(new byte[] {'1', '2', '3', '4', '5'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[6];
        assertEquals(6, TarUtils.formatOctalBytes(12345, buffer, 0, 6));
        assertArrayEquals(new byte[] {'1', '2', '3', '4', '5', ' '}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] {1, 2, 3, 4, 5};
        assertEquals(15, TarUtils.computeCheckSum(buffer));
    }
}