import org.junit.*;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // basic functionality
        byte[] buffer = "123\0   ".getBytes();
        assertEquals(123, TarUtils.parseOctal(buffer, 0, 6));
        
        // buffer with NULs
        byte[] buffer2 = "\0\0\0\0 ".getBytes();
        assertEquals(0, TarUtils.parseOctal(buffer2, 0, 5));
        
        // invalid byte
        byte[] buffer3 = "a\0  ".getBytes();
        try {
            TarUtils.parseOctal(buffer3, 0, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 97 at offset 0 in '{NUL}{NUL}{NUL}{NUL} ' len=5", e.getMessage());
        }
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = "test\0".getBytes();
        assertEquals("test", TarUtils.parseName(buffer, 0, 6));
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[6];
        TarUtils.formatNameBytes(name, buf, 0, 6);
        assertArrayEquals("test\0\0", buf);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);
        assertArrayEquals("00123".getBytes(), buffer);
        
        byte[] buffer2 = new byte[4];
        try {
            TarUtils.formatUnsignedOctalString(12345, buffer2, 0, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("12345=30071 will not fit in octal number buffer of length 4", e.getMessage());
        }
    }
    
    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[8];
        TarUtils.formatOctalBytes(123, buffer, 0, 8);
        assertArrayEquals("00000123 ".getBytes(), buffer);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[8];
        TarUtils.formatLongOctalBytes(123, buffer, 0, 8);
        assertArrayEquals("000000123".getBytes(), buffer);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[8];
        TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 8);
        assertArrayEquals("00000123 ".getBytes(), buffer);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "test".getBytes();
        assertEquals(448, TarUtils.computeCheckSum(buffer));
    }
}