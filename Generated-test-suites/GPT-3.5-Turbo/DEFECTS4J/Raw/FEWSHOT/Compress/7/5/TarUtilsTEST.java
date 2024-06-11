import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = "123".getBytes();
        assertEquals(123L, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        buffer = "123\0".getBytes();
        assertEquals(123L, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        buffer = "123 ".getBytes();
        assertEquals(123L, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        buffer = "1234".getBytes();
        assertEquals(123L, TarUtils.parseOctal(buffer, 0, 3));
        
        buffer = "3234".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, 3);
        } catch(IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 2 in '323'", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = "test".getBytes();
        assertEquals("test", TarUtils.parseName(buffer, 0, buffer.length));
        
        buffer = "test\0".getBytes();
        assertEquals("test", TarUtils.parseName(buffer, 0, buffer.length));
        
        buffer = "test ".getBytes();
        assertEquals("test", TarUtils.parseName(buffer, 0, buffer.length));
        
        buffer = "testing".getBytes();
        assertEquals("test", TarUtils.parseName(buffer, 0, 4));
        
        buffer = new byte[] {'t', 'e', 's', 't', 'i', 'n', 'g'};
        assertEquals("test", TarUtils.parseName(buffer, 0, 4));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[10];
        int offset = 0;
        offset = TarUtils.formatNameBytes(name, buf, offset, 10);
        assertEquals(4, offset);
        assertEquals("test\0\0\0\0", new String(buf));
        
        name = "test";
        buf = new byte[3];
        offset = 0;
        offset = TarUtils.formatNameBytes(name, buf, offset, 3);
        assertEquals(3, offset);
        assertEquals("tes", new String(buf));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        TarUtils.formatUnsignedOctalString(value, buf, offset, 10);
        assertEquals("000000000000000173", new String(buf));
        
        value = 123;
        buf = new byte[3];
        offset = 0;
        try {
            TarUtils.formatUnsignedOctalString(value, buf, offset, 3);
        } catch (IllegalArgumentException e) {
            assertEquals("123=173 will not fit in octal number buffer of length 3", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        offset = TarUtils.formatOctalBytes(value, buf, offset, 10);
        assertEquals(10, offset);
        assertEquals("0000000000173 ", new String(buf));
        
        value = 123;
        buf = new byte[3];
        offset = 0;
        offset = TarUtils.formatOctalBytes(value, buf, offset, 3);
        assertEquals(3, offset);
        assertEquals("017", new String(buf));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        offset = TarUtils.formatLongOctalBytes(value, buf, offset, 10);
        assertEquals(10, offset);
        assertEquals("00000000173", new String(buf));
        
        value = 123;
        buf = new byte[3];
        offset = 0;
        offset = TarUtils.formatLongOctalBytes(value, buf, offset, 3);
        assertEquals(3, offset);
        assertEquals("173", new String(buf));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        offset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, 10);
        assertEquals(10, offset);
        assertEquals("0000000173 ", new String(buf));
        
        value = 123;
        buf = new byte[3];
        offset = 0;
        offset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, 3);
        assertEquals(3, offset);
        assertEquals("173", new String(buf));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = "test".getBytes();
        assertEquals(448, TarUtils.computeCheckSum(buf));
        
        buf = "testing".getBytes();
        assertEquals(572, TarUtils.computeCheckSum(buf));
    }
}