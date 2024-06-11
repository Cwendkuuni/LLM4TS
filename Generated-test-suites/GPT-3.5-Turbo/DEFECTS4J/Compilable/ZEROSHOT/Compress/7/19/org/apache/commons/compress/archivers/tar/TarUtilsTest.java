package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 48;
        buffer[1] = 49;
        buffer[2] = 55;
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(175, result);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 97;
        buffer[1] = 98;
        buffer[2] = 99;
        String result = TarUtils.parseName(buffer, 0, 3);
        assertEquals("abc", result);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(5, result);
        assertEquals(97, buf[0]);
        assertEquals(98, buf[1]);
        assertEquals(99, buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(0, buf[4]);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 175;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals(48, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(53, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 175;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(5, result);
        assertEquals(48, buf[0]);
        assertEquals(55, buf[1]);
        assertEquals(32, buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(0, buf[4]);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 175;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(5, result);
        assertEquals(48, buf[0]);
        assertEquals(55, buf[1]);
        assertEquals(32, buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(0, buf[4]);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 175;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(5, result);
        assertEquals(48, buf[0]);
        assertEquals(55, buf[1]);
        assertEquals(0, buf[2]);
        assertEquals(32, buf[3]);
        assertEquals(0, buf[4]);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = 48;
        buf[1] = 49;
        buf[2] = 50;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(147, result);
    }
}