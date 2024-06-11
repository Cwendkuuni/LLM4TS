package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = '1';
        buffer[1] = '2';
        buffer[2] = '3';
        buffer[3] = '\0';
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(123, result);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 'f';
        buffer[1] = 'o';
        buffer[2] = 'o';
        buffer[3] = '\0';
        String result = TarUtils.parseName(buffer, 0, 4);
        assertNotNull(result);
        assertEquals("foo", result);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "foo";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(4, result);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 4;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertNotNull(buffer);
        assertEquals('0', buffer[offset]);
        assertEquals('1', buffer[offset + 1]);
        assertEquals('7', buffer[offset + 2]);
        assertEquals('3', buffer[offset + 3]);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(4, result);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(4, result);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(4, result);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = 'a';
        buf[1] = 'b';
        buf[2] = 'c';
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(294, result);
    }
}