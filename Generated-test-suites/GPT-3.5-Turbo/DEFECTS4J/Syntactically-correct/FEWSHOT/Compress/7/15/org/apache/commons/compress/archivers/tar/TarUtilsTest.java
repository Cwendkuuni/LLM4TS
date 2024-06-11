package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "123456\0".getBytes();
        
        assertEquals(123456, TarUtils.parseOctal(buffer, 0, 6));
        
        buffer = "abcdef\0".getBytes();
        
        try {
            TarUtils.parseOctal(buffer, 0, 6);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
        
        buffer = "12345a\0".getBytes();
        
        try {
            TarUtils.parseOctal(buffer, 0, 6);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = "test\0".getBytes();
        
        assertEquals("test", TarUtils.parseName(buffer, 0, 5));
        
        buffer = "test123\0".getBytes();
        
        assertEquals("test12", TarUtils.parseName(buffer, 0, 7));
    }
    
    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        
        int resultOffset = TarUtils.formatNameBytes("test", buffer, offset, length);
        
        assertEquals(7, resultOffset);
        assertEquals("test\0\0\0\0\0", new String(buffer, offset, resultOffset-offset));
        
        resultOffset = TarUtils.formatNameBytes("test123", buffer, offset, length);
        
        assertEquals(10, resultOffset);
        assertEquals("test123\0\0\0", new String(buffer, offset, resultOffset-offset));
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        
        TarUtils.formatUnsignedOctalString(123456L, buffer, offset, length);
        
        assertEquals("0000017550", new String(buffer, offset, length));
        
        try {
            TarUtils.formatUnsignedOctalString(123456L, buffer, offset, 8);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        
        int resultOffset = TarUtils.formatOctalBytes(123456L, buffer, offset, length);
        
        assertEquals(10, resultOffset);
        assertEquals("000001755 ", new String(buffer, offset, resultOffset-offset));
        
        try {
            TarUtils.formatOctalBytes(123456L, buffer, offset, 8);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        
        int resultOffset = TarUtils.formatLongOctalBytes(123456L, buffer, offset, length);
        
        assertEquals(10, resultOffset);
        assertEquals("000001755 ", new String(buffer, offset, resultOffset-offset));
        
        try {
            TarUtils.formatLongOctalBytes(123456L, buffer, offset, 8);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        
        int resultOffset = TarUtils.formatCheckSumOctalBytes(123456L, buffer, offset, length);
        
        assertEquals(10, resultOffset);
        assertEquals("000001755 ", new String(buffer, offset, resultOffset-offset));
        
        try {
            TarUtils.formatCheckSumOctalBytes(123456L, buffer, offset, 8);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "123456".getBytes();
        
        assertEquals(300, TarUtils.computeCheckSum(buffer));
        
        buffer = "abcdef".getBytes();
        
        assertEquals(612, TarUtils.computeCheckSum(buffer));
    }

}