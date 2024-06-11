
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ('0' + i); // fill buffer with numbers 0-9
        }
        
        assertEquals(123456789L, TarUtils.parseOctal(buffer, 0, 9));
        assertEquals(0L, TarUtils.parseOctal(buffer, 0, 1));
        assertEquals(89L, TarUtils.parseOctal(buffer, 8, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[] { (byte) 'A' }; // invalid byte
        TarUtils.parseOctal(buffer, 0, 1);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ('A' + i); // fill buffer with letters A-J
        }
        
        assertEquals("ABCDEFGHIJ", TarUtils.parseName(buffer, 0, 10));
        assertEquals("A", TarUtils.parseName(buffer, 0, 1));
        assertEquals("IJ", TarUtils.parseName(buffer, 8, 2));
    }
    
    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        String name = "TEST";
        int offset = 1;
        int length = 6;
        
        TarUtils.formatNameBytes(name, buffer, offset, length);
        
        assertEquals(0, buffer[0]);
        assertEquals((byte) 'T', buffer[1]);
        assertEquals((byte) 'E', buffer[2]);
        assertEquals((byte) 'S', buffer[3]);
        assertEquals((byte) 'T', buffer[4]);
        assertEquals(0, buffer[5]);
        assertEquals(0, buffer[6]);
        assertEquals(0, buffer[7]);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        
        TarUtils.formatUnsignedOctalString(1234L, buffer, 0, 5);
        
        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '0', buffer[1]);
        assertEquals((byte) '0', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) '2', buffer[4]);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringOverflow() {
        byte[] buffer = new byte[2];
        
        TarUtils.formatUnsignedOctalString(1234L, buffer, 0, 2);
    }
    
    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[6];
        
        TarUtils.formatOctalBytes(123L, buffer, 0, 6);
        
        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '0', buffer[1]);
        assertEquals((byte) '0', buffer[2]);
        assertEquals((byte) '0', buffer[3]);
        assertEquals((byte) '1', buffer[4]);
        assertEquals((byte) '2', buffer[5]);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesOverflow() {
        byte[] buffer = new byte[2];
        
        TarUtils.formatOctalBytes(123L, buffer, 0, 2);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[5];
        
        TarUtils.formatLongOctalBytes(123L, buffer, 0, 5);
        
        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '0', buffer[1]);
        assertEquals((byte) '1', buffer[2]);
        assertEquals((byte) '2', buffer[3]);
        assertEquals((byte) '3', buffer[4]);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytesOverflow() {
        byte[] buffer = new byte[2];
        
        TarUtils.formatLongOctalBytes(123L, buffer, 0, 2);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[6];
        
        TarUtils.formatCheckSumOctalBytes(123L, buffer, 0, 6);
        
        assertEquals((byte) '0', buffer[0]);
        assertEquals((byte) '0', buffer[1]);
        assertEquals((byte) '0', buffer[2]);
        assertEquals((byte) '0', buffer[3]);
        assertEquals((byte) '1', buffer[4]);
        assertEquals((byte) '2', buffer[5]);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesOverflow() {
        byte[] buffer = new byte[2];
        
        TarUtils.formatCheckSumOctalBytes(123L, buffer, 0, 2);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 };
        
        long sum = TarUtils.computeCheckSum(buffer);
        
        assertEquals(10, sum);
    }

}
