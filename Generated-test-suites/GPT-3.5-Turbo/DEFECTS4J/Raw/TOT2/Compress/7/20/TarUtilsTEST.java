import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', '4', '5', '6', 0, 0};
        assertEquals(123456L, TarUtils.parseOctal(buffer, 0, 7));
    }

    @Test
    public void testParseOctalInvalidDigit() {
        byte[] buffer = {'1', '2', '8', '4', '5', '6', 0, 0};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer, 0, 7);
        });
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0, 'n', 'a', 'm', 'e', 0, 0};
        assertEquals("test", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        assertEquals(10, TarUtils.formatNameBytes("test", buffer, 0, 10));
        assertEquals("test\0\0\0\0\0", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {'1', '2', '3', '4', '5', '6', 0, 0};
        assertEquals(321, TarUtils.computeCheckSum(buffer));
    }

    // Add more test cases for other methods as needed

}