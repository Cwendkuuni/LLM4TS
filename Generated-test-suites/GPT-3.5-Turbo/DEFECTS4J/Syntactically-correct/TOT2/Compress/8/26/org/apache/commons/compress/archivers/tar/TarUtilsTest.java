import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 55, 50, 57, 0}; // Representing an octal value of 729
        assertEquals(729, TarUtils.parseOctal(buffer, 0, 5));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 68, 0}; // Representing the name "ABCD"
        assertEquals("ABCD", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatNameBytes("EFG", buffer, 0, 5);
        assertEquals("EFG\u0000\u0000", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 3);
        assertEquals("123", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatOctalBytes(456, buffer, 0, 4);
        assertEquals("456 \u0000", new String(buffer));
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatLongOctalBytes(789, buffer, 0, 4);
        assertEquals("789 ", new String(buffer));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatCheckSumOctalBytes(987, buffer, 0, 4);
        assertEquals("987\u0000 ", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4, 5};
        assertEquals(15, TarUtils.computeCheckSum(buffer));
    }
    
}