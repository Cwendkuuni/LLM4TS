import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TarUtilsTest {

    // Test for parseOctal method
    @Test
    public void testParseOctal() {
        byte[] buffer = {0, '1', '2', '3', ' ', '4', '5', '6'};
        assertEquals(123, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    // Test for parseName method
    @Test
    public void testParseName() {
        byte[] buffer = {'h', 'e', 'l', 'l', 'o', 0, 'w', 'o', 'r', 'l', 'd'};
        assertEquals("hello", TarUtils.parseName(buffer, 0, 5));
    }

    // Test for formatNameBytes method
    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[10];
        String name = "test";
        assertEquals(10, TarUtils.formatNameBytes(name, buf, 0, 10));
    }

    // Test for formatUnsignedOctalString method
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("00123", new String(buffer));
    }

    // Test for formatOctalBytes method
    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("213  ", new String(buffer));
    }

    // Test for formatLongOctalBytes method
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("123 ", new String(buffer));
    }

    // Test for formatCheckSumOctalBytes method
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("213  ", new String(buffer));
    }

    // Test for computeCheckSum method
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4, 5};
        assertEquals(15, TarUtils.computeCheckSum(buffer));
    }
}