package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{48,49,50,51,52,53};
        int offset = 0;
        int length = 6;
        long expected = 12345;
        long actual = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{116, 101, 115, 116, 46, 116, 120, 116, 0};
        int offset = 0;
        int length = 9;
        String expected = "test.txt";
        String actual = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "test.txt";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = 10;
        int actual = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "303132333435";
        String actual = new String(buffer);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        int expected = 8;
        int actual = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[7];
        int offset = 0;
        int length = 7;
        int expected = 7;
        int actual = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        int expected = 8;
        int actual = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{48,49,50,51,52,53};
        long expected = 255;
        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected, actual);
    }
}