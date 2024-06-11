package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 0;
        buffer[1] = 40;
        buffer[2] = 30;
        buffer[3] = 20;
        buffer[4] = 0;
        
        assertEquals(403020, TarUtils.parseOctal(buffer, 1, 3));
        assertEquals(320, TarUtils.parseOctal(buffer, 2, 2));
        assertEquals(30, TarUtils.parseOctal(buffer, 3, 1));
        assertEquals(0, TarUtils.parseOctal(buffer, 0, 1));
        assertEquals(0, TarUtils.parseOctal(buffer, 4, 1));
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = "HelloWorld".getBytes();
        buffer[5] = 0;
        
        assertEquals("Hello", TarUtils.parseName(buffer, 0, 5));
        assertEquals("Hello", TarUtils.parseName(buffer, 0, 10));
        assertEquals("HelloWorld", TarUtils.parseName(buffer, 0, 11));
        assertEquals("World", TarUtils.parseName(buffer, 5, 10));
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "HelloWorld";
        byte[] buffer = new byte[10];
        
        assertEquals(10, TarUtils.formatNameBytes(name, buffer, 0, 10));
        assertArrayEquals("HelloWorld".getBytes(), buffer);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 10);
        assertArrayEquals("0000000123".getBytes(), buffer);
        
        TarUtils.formatUnsignedOctalString(456, buffer, 0, 5);
        assertArrayEquals("00456".getBytes(), buffer);
    }
    
    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        
        assertEquals(10, TarUtils.formatOctalBytes(123, buffer, 0, 10));
        assertArrayEquals("0000000123".getBytes(), buffer);
        
        assertEquals(7, TarUtils.formatOctalBytes(456, buffer, 0, 7));
        assertArrayEquals("0000456 ".getBytes(), buffer);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];
        
        assertEquals(10, TarUtils.formatLongOctalBytes(123, buffer, 0, 10));
        assertArrayEquals("0000000123".getBytes(), buffer);
        
        assertEquals(7, TarUtils.formatLongOctalBytes(456, buffer, 0, 7));
        assertArrayEquals("000456 ".getBytes(), buffer);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];
        
        assertEquals(10, TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 10));
        assertArrayEquals("0000000123".getBytes(), buffer);
        
        assertEquals(7, TarUtils.formatCheckSumOctalBytes(456, buffer, 0, 7));
        assertArrayEquals("000456 ".getBytes(), buffer);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = 1;
        buffer[1] = 2;
        buffer[2] = 3;
        buffer[3] = 4;
        buffer[4] = 5;
        buffer[5] = 6;
        
        assertEquals(21, TarUtils.computeCheckSum(buffer));
    }
}