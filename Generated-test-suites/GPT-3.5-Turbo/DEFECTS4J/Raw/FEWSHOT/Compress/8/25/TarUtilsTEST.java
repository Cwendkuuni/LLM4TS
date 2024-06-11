import org.apache.commons.compress.archivers.tar.TarUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "1234567890".getBytes();
        assertEquals(342391, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        buffer = "000000000".getBytes();
        assertEquals(0, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        buffer = " abcdefgh ".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        buffer = "12345678 abcdefgh".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = "Hello World\0".getBytes();
        assertEquals("Hello World", TarUtils.parseName(buffer, 0, buffer.length));
        
        buffer = "Hello World!".getBytes();
        assertEquals("Hello World!", TarUtils.parseName(buffer, 0, buffer.length));
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "Hello World";
        byte[] buffer = new byte[20];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("Hello World", new String(buffer));
        
        name = "Goodbye World!";
        buffer = new byte[10];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("Goodbye Wo", new String(buffer));
        
        name = "Goodbye World!";
        buffer = new byte[15];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("Goodbye World!", new String(buffer));
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 342391;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("0012575377", new String(buffer));
        
        value = 0;
        buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("0000000000", new String(buffer));
        
        value = 1234567890123L;
        buffer = new byte[10];
        try {
            TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 342391;
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0012575377 ", new String(buffer));
        
        value = 0;
        buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0000000000 ", new String(buffer));
        
        value = 1234567890123L;
        buffer = new byte[10];
        try {
            TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 342391;
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0012575377 ", new String(buffer));
        
        value = 0;
        buffer = new byte[10];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0000000000 ", new String(buffer));
        
        value = 1234567890123L;
        buffer = new byte[10];
        try {
            TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 342391;
        byte[] buffer = new byte[10];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0012575377 ", new String(buffer));
        
        value = 0;
        buffer = new byte[10];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0000000000 ", new String(buffer));
        
        value = 1234567890123L;
        buffer = new byte[10];
        try {
            TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "Hello World".getBytes();
        assertEquals(1056, TarUtils.computeCheckSum(buffer));
        
        buffer = "Goodbye World!".getBytes();
        assertEquals(1063, TarUtils.computeCheckSum(buffer));
    }
}