import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 49; // ASCII value for '1'
        buffer[1] = 50; // ASCII value for '2'
        buffer[2] = 51; // ASCII value for '3'
        int offset = 0;
        int length = 3;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 72; // ASCII value for 'H'
        buffer[1] = 101; // ASCII value for 'e'
        buffer[2] = 108; // ASCII value for 'l'
        buffer[3] = 108; // ASCII value for 'l'
        buffer[4] = 111; // ASCII value for 'o'
        int offset = 0;
        int length = 5;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("Hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Hello";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int updatedOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertEquals(5, updatedOffset);
        assertEquals(72, buf[0]); // ASCII value for 'H'
        assertEquals(101, buf[1]); // ASCII value for 'e'
        assertEquals(108, buf[2]); // ASCII value for 'l'
        assertEquals(108, buf[3]); // ASCII value for 'l'
        assertEquals(111, buf[4]); // ASCII value for 'o'
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertEquals(48, buffer[0]); // ASCII value for '0'
        assertEquals(48, buffer[1]); // ASCII value for '0'
        assertEquals(48, buffer[2]); // ASCII value for '0'
        assertEquals(49, buffer[3]); // ASCII value for '1'
        assertEquals(50, buffer[4]); // ASCII value for '2'
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int updatedOffset = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertEquals(5, updatedOffset);
        assertEquals(48, buf[0]); // ASCII value for '0'
        assertEquals(48, buf[1]); // ASCII value for '0'
        assertEquals(48, buf[2]); // ASCII value for '0'
        assertEquals(49, buf[3]); // ASCII value for '1'
        assertEquals(50, buf[4]); // ASCII value for '2'
        assertEquals(32, buf[5]); // ASCII value for space
        assertEquals(0, buf[6]); // ASCII value for NUL
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int updatedOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertEquals(5, updatedOffset);
        assertEquals(48, buf[0]); // ASCII value for '0'
        assertEquals(48, buf[1]); // ASCII value for '0'
        assertEquals(48, buf[2]); // ASCII value for '0'
        assertEquals(49, buf[3]); // ASCII value for '1'
        assertEquals(50, buf[4]); // ASCII value for '2'
        assertEquals(32, buf[5]); // ASCII value for space
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int updatedOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertEquals(5, updatedOffset);
        assertEquals(48, buf[0]); // ASCII value for '0'
        assertEquals(48, buf[1]); // ASCII value for '0'
        assertEquals(48, buf[2]); // ASCII value for '0'
        assertEquals(49, buf[3]); // ASCII value for '1'
        assertEquals(50, buf[4]); // ASCII value for '2'
        assertEquals(0, buf[5]); // ASCII value for NUL
        assertEquals(32, buf[6]); // ASCII value for space
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = 72; // ASCII value for 'H'
        buf[1] = 101; // ASCII value for 'e'
        buf[2] = 108; // ASCII value for 'l'
        buf[3] = 108; // ASCII value for 'l'
        buf[4] = 111; // ASCII value for 'o'
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(532, result);
    }

}