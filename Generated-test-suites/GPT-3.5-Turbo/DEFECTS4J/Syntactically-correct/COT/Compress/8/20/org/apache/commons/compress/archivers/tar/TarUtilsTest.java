import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'0', '4', '2', 0};
        int offset = 0;
        int length = 3;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(42, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = {'0', '8', '2', 0};
        int offset = 0;
        int length = 3;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'h', 'e', 'l', 'l', 'o', 0};
        int offset = 0;
        int length = 5;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(5, result);
        assertEquals('h', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('l', buf[2]);
        assertEquals('l', buf[3]);
        assertEquals('o', buf[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 42;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals('0', buffer[0]);
        assertEquals('0', buffer[1]);
        assertEquals('0', buffer[2]);
        assertEquals('4', buffer[3]);
        assertEquals('2', buffer[4]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 42;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(5, result);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('0', buf[2]);
        assertEquals('4', buf[3]);
        assertEquals('2', buf[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 42;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(5, result);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('0', buf[2]);
        assertEquals('4', buf[3]);
        assertEquals('2', buf[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 42;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(5, result);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('0', buf[2]);
        assertEquals('4', buf[3]);
        assertEquals('2', buf[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {'h', 'e', 'l', 'l', 'o'};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(532, result);
    }
    
    // Additional test cases for potential edge cases and exception handling scenarios can be added here
    
}