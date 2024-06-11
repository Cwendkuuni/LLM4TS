package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {(byte) '1', (byte) '2', (byte) '3'};
        int offset = 0;
        int length = buffer.length;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(123, result);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {(byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 0};
        int offset = 0;
        int length = buffer.length;
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = name.length();
        
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertEquals(4, newOffset);
        assertArrayEquals(new byte[] {(byte) 't', (byte) 'e', (byte) 's', (byte) 't', 0, 0, 0, 0, 0, 0}, buf);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, (byte) '1', (byte) '7', (byte) '3', (byte) '0'}, buffer);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        
        int newOffset = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertEquals(10, newOffset);
        assertArrayEquals(new byte[] {(byte) '0', (byte) '1', (byte) '7', (byte) '3', (byte) '0', (byte) ' ', 0, 0, 0, 0}, buf);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        
        int newOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertEquals(10, newOffset);
        assertArrayEquals(new byte[] {(byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) ' ', 0, 0}, buf);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;
        
        int newOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertEquals(10, newOffset);
        assertArrayEquals(new byte[] {(byte) '0', (byte) '1', (byte) '7', (byte) '3', (byte) '0', 0, (byte) ' ' , 0, 0, 0}, buf);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[] {(byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(432, result);
    }
}