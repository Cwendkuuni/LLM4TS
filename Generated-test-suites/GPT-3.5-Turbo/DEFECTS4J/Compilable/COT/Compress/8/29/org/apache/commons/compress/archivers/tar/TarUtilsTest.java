package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {0, 0, 0, 1, 0};
        int offset = 0;
        int length = 2;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(256, result);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', ' ', 'n', 'a', 'm', 'e', 0};
        int offset = 0;
        int length = 9;
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals("test name", result);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "test name";
        byte[] buf = new byte[11];
        int offset = 0;
        int length = 11;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertEquals(11, result);
        assertArrayEquals(new byte[]{'t', 'e', 's', 't', ' ', 'n', 'a', 'm', 'e', 0, 0}, buf);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 256;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertArrayEquals(new byte[]{0, 0, '1', '0'}, buffer);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 256;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertEquals(3, result);
        assertArrayEquals(new byte[]{'1', '0', ' '}, buf);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 256;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertEquals(3, result);
        assertArrayEquals(new byte[]{'1', '0', ' '}, buf);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 256;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertEquals(3, result);
        assertArrayEquals(new byte[]{'1', '0', ' '}, buf);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = {1, 2, 3};
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(6, result);
    }
}