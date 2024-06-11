package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    // Test for parseOctal method
    @Test
    public void testParseOctal() {
        byte[] buffer = {50, 51, 52}; // ascii representation of '234'
        int offset = 0;
        int length = buffer.length;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(234, result);
    }
    
    // Test for parseName method
    @Test
    public void testParseName() {
        byte[] buffer = {104, 101, 108, 108, 111}; // ascii representation of 'hello'
        int offset = 0;
        int length = buffer.length;
        
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("hello", result);
    }
    
    // Test for formatNameBytes method
    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertArrayEquals(new byte[]{104, 101, 108, 108, 111, 0, 0, 0, 0, 0}, buffer);
        assertEquals(10, result);
    }
    
    // Test for formatUnsignedOctalString method
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 234;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{50, 51, 52, 0, 0}, buffer);
    }
    
    // Test for formatOctalBytes method
    @Test
    public void testFormatOctalBytes() {
        long value = 234;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        
        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{50, 51, 52, 32, 0}, buffer);
        assertEquals(5, result);
    }
    
    // Test for formatLongOctalBytes method
    @Test
    public void testFormatLongOctalBytes() {
        long value = 234;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = buffer.length;
        
        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{50, 51, 52, 32}, buffer);
        assertEquals(4, result);
    }
    
    // Test for formatCheckSumOctalBytes method
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 234;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = buffer.length;
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{50, 51, 52, 0, 32, 0}, buffer);
        assertEquals(6, result);
    }
    
    // Test for computeCheckSum method
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {50, 51, 52}; // ascii representation of '234'
        
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(234, result);
    }
}
