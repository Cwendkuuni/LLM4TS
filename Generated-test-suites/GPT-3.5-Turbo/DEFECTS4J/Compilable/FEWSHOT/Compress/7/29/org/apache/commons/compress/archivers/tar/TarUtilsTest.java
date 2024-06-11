package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 0;
        buffer[1] = 1;
        buffer[2] = 2;
        buffer[3] = 3;
        buffer[4] = 4;
        buffer[5] = 5;
        buffer[6] = 6;
        buffer[7] = 7;
        buffer[8] = 8;
        buffer[9] = 9;
        assertEquals(0, TarUtils.parseOctal(buffer, 0, 10));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[5];
        buffer[0] = 97;
        buffer[1] = 98;
        buffer[2] = 99;
        buffer[3] = 100;
        buffer[4] = 101;
        assertEquals("abcde", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcde";
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        int offset = 0;
        int length = 5;
        assertEquals(5, TarUtils.formatNameBytes(name, buffer, offset, length));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        TarUtils.formatUnsignedOctalString(10L, buffer, 0, 5);
        assertEquals(48, buffer[0]);
        assertEquals(48, buffer[1]);
        assertEquals(48, buffer[2]);
        assertEquals(49, buffer[3]);
        assertEquals(48, buffer[4]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        assertEquals(5, TarUtils.formatOctalBytes(10L, buffer, 0, 5));
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        assertEquals(5, TarUtils.formatLongOctalBytes(10L, buffer, 0, 5));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        assertEquals(5, TarUtils.formatCheckSumOctalBytes(10L, buffer, 0, 5));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = 1;
        buffer[2] = 2;
        buffer[3] = 3;
        buffer[4] = 4;
        assertEquals(10, TarUtils.computeCheckSum(buffer));
    }
}