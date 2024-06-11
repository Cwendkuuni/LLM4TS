package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51};
        int offset = 0;
        int length = 3;
        
        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(123, result);
    }
    
    @Test
    public void testParseOctalWithLeadingZeros() {
        byte[] buffer = {48, 48, 49, 50, 51};
        int offset = 0;
        int length = 5;
        
        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(123, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidDigit() {
        byte[] buffer = {49, 56, 55};
        int offset = 0;
        int length = 3;
        
        TarUtils.parseOctal(buffer, offset, length);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = {72, 101, 108, 108, 111, 0};
        int offset = 0;
        int length = 6;
        
        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("Hello", result);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "Hello";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(6, result);
        assertEquals(72, buf[0]);
        assertEquals(101, buf[1]);
        assertEquals(108, buf[2]);
        assertEquals(108, buf[3]);
        assertEquals(111, buf[4]);
        assertEquals(0, buf[5]);
    }
    
    @Test
    public void testFormatNameBytesWithNameLongerThanBuffer() {
        String name = "Hello World";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(6, result);
        assertEquals(72, buf[0]);
        assertEquals(101, buf[1]);
        assertEquals(108, buf[2]);
        assertEquals(108, buf[3]);
        assertEquals(111, buf[4]);
        assertEquals(0, buf[5]);
    }
    
    @Test
    public void testFormatNameBytesWithBufferLongerThanName() {
        String name = "Hello";
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(6, result);
        assertEquals(72, buf[0]);
        assertEquals(101, buf[1]);
        assertEquals(108, buf[2]);
        assertEquals(108, buf[3]);
        assertEquals(111, buf[4]);
        assertEquals(0, buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(0, buf[7]);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertEquals(48, buffer[0]);
        assertEquals(54, buffer[1]);
        assertEquals(51, buffer[2]);
        assertEquals(50, buffer[3]);
        assertEquals(51, buffer[4]);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithInvalidLength() {
        long value = 123;
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertEquals(6, result);
        assertEquals(48, buf[0]);
        assertEquals(54, buf[1]);
        assertEquals(51, buf[2]);
        assertEquals(50, buf[3]);
        assertEquals(51, buf[4]);
        assertEquals(32, buf[5]);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertEquals(5, result);
        assertEquals(48, buf[0]);
        assertEquals(54, buf[1]);
        assertEquals(51, buf[2]);
        assertEquals(50, buf[3]);
        assertEquals(32, buf[4]);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertEquals(6, result);
        assertEquals(48, buf[0]);
        assertEquals(54, buf[1]);
        assertEquals(51, buf[2]);
        assertEquals(50, buf[3]);
        assertEquals(0, buf[4]);
        assertEquals(32, buf[5]);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = {72, 101, 108, 108, 111, 0};
        
        long result = TarUtils.computeCheckSum(buf);

        assertEquals(460, result);
    }
}