package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'0', '1', '2', '3', '4', '5', ' '};
        assertEquals(12345, TarUtils.parseOctal(buffer, 0, 6));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0};
        assertEquals("test", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes("test", buffer, 0, 4);
        assertEquals("test", new String(buffer, 0, 4));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);
        assertEquals("00123", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatOctalBytes(12345, buffer, 0, 5);
        assertEquals("12345 ", new String(buffer, 0, 5));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {'t', 'e', 's', 't'};
        assertEquals(448, TarUtils.computeCheckSum(buffer));
    }

}