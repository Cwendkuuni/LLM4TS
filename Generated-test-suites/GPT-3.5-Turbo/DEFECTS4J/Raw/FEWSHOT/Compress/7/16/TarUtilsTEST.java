
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = {0, 0, 0, 0, 0};
        assertEquals(0, TarUtils.parseOctal(buffer, 0, 5));
        
        byte[] buffer2 = {0, 0, 0, '1', 0};
        assertEquals(1, TarUtils.parseOctal(buffer2, 0, 5));
        
        byte[] buffer3 = {0, 0, 0, '1', '2'};
        assertEquals(12, TarUtils.parseOctal(buffer3, 0, 5));
        
        byte[] buffer4 = {0, '7', '6', '5', '4'};
        assertEquals(7654, TarUtils.parseOctal(buffer4, 0, 5));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0};
        assertEquals("test", TarUtils.parseName(buffer, 0, 5));
        
        byte[] buffer2 = {'n', 'a', 'm', 'e', 0};
        assertEquals("name", TarUtils.parseName(buffer2, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[5];
        assertEquals(5, TarUtils.formatNameBytes("test", buffer, 0, 5));
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        assertEquals(0, buffer[4]);
        
        byte[] buffer2 = new byte[5];
        assertEquals(5, TarUtils.formatNameBytes("name", buffer2, 0, 5));
        assertEquals('n', buffer2[0]);
        assertEquals('a', buffer2[1]);
        assertEquals('m', buffer2[2]);
        assertEquals('e', buffer2[3]);
        assertEquals(0, buffer2[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(0, buffer, 0, 5);
        assertEquals('0', buffer[0]);
        assertEquals('0', buffer[1]);
        assertEquals('0', buffer[2]);
        assertEquals('0', buffer[3]);
        assertEquals('0', buffer[4]);
        
        byte[] buffer2 = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer2, 0, 5);
        assertEquals('0', buffer2[0]);
        assertEquals('0', buffer2[1]);
        assertEquals('1', buffer2[2]);
        assertEquals('7', buffer2[3]);
        assertEquals('3', buffer2[4]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[5];
        assertEquals(5, TarUtils.formatOctalBytes(123, buffer, 0, 5));
        assertEquals('0', buffer[0]);
        assertEquals('0', buffer[1]);
        assertEquals('1', buffer[2]);
        assertEquals('7', buffer[3]);
        assertEquals('3', buffer[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[5];
        assertEquals(5, TarUtils.formatLongOctalBytes(1234567890, buffer, 0, 5));
        assertEquals('4', buffer[0]);
        assertEquals('6', buffer[1]);
        assertEquals('4', buffer[2]);
        assertEquals('6', buffer[3]);
        assertEquals('4', buffer[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[5];
        assertEquals(5, TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 5));
        assertEquals('0', buffer[0]);
        assertEquals('0', buffer[1]);
        assertEquals('1', buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4, 5};
        assertEquals(15, TarUtils.computeCheckSum(buffer));
        
        byte[] buffer2 = {0, 0, 0, 0, 0};
        assertEquals(0, TarUtils.computeCheckSum(buffer2));
    }
}
