package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', '4'};
        assertEquals(1234, TarUtils.parseOctal(buffer, 0, 4));
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0, 'a', 'b'};
        assertEquals("test", TarUtils.parseName(buffer, 0, 5));
    }
    
    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes("test", buf, 0, 5);
        assertEquals(5, offset);
        assertEquals('t', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
        assertEquals('t', buf[3]);
        assertEquals(0, buf[4]);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(425, buf, 0, 5);
        assertEquals('6', buf[0]);
        assertEquals('7', buf[1]);
        assertEquals('1', buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(0, buf[4]);
    }
    
    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatOctalBytes(123, buf, 0, 5);
        assertEquals(5, offset);
        assertEquals('1', buf[0]);
        assertEquals('2', buf[1]);
        assertEquals('3', buf[2]);
        assertEquals(' ', buf[3]);
        assertEquals(0, buf[4]);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = {'t', 'e', 's', 't'};
        assertEquals(448, TarUtils.computeCheckSum(buf));
    }
}