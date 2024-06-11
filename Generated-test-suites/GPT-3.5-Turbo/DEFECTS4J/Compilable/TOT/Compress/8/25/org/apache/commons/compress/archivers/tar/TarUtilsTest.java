package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = {'0', '7', '5', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        int offset = 0;
        int length = 12;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(61, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = {'0', '8', '5', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        int offset = 0;
        int length = 12;
        
        TarUtils.parseOctal(buffer, offset, length);
    }
    
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', 0};
        int offset = 0;
        int length = 12;
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals("hello world", result);
    }
    
    @Test
    public void testParseNameEmptyInput() {
        byte[] buffer = {0, 'w', 'o', 'r', 'l', 'd', 0, '!', '!', '!', '!', '!'};
        int offset = 0;
        int length = 12;
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals("", result);
    }
    
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "hello world";
        byte[] buf = new byte[12];
        int offset = 0;
        int length = 12;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertArrayEquals(new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', 0}, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatNameBytesTruncatedOutput() {
        String name = "hello world";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertArrayEquals(new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l'}, buf);
        assertEquals(offset + length, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidInput() {
        long value = 1234567890;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }
    
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 1234567890;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(new byte[]{'1', '1', '7', '6', '4', '3', '2', '6', '2', ' '}, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatLongOctalBytesValidInput() {
        long value = 1234567890;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(new byte[]{'1', '1', '7', '6', '4', '3', '2', '6', '2', ' '}, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatCheckSumOctalBytesValidInput() {
        long value = 1234567890;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(new byte[]{'1', '1', '7', '6', '4', '3', '2', '6', 0, ' '}, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testComputeCheckSumValidInput() {
        byte[] buf = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', 0};
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(1095, result);
    }
}