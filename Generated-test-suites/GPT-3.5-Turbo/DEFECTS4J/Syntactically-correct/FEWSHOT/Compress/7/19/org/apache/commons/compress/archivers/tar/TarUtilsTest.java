
package org.apache.commons.compress.archivers.tar;

import org.junit.Test;

import static org.junit.Assert.*;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[8];
        buffer[0] = 48;
        buffer[1] = 55;
        buffer[2] = 54;
        buffer[3] = 55;
        buffer[4] = 48;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;

        assertEquals(07670, TarUtils.parseOctal(buffer, 0, 8));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[8];
        buffer[0] = 116;
        buffer[1] = 101;
        buffer[2] = 115;
        buffer[3] = 116;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;

        assertEquals("test", TarUtils.parseName(buffer, 0, 8));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[8];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;

        assertEquals(8, TarUtils.formatNameBytes(name, buffer, 0, 4));
        assertEquals(116, buffer[0]);
        assertEquals(101, buffer[1]);
        assertEquals(115, buffer[2]);
        assertEquals(116, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(0, buffer[5]);
        assertEquals(0, buffer[6]);
        assertEquals(0, buffer[7]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[8];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;

        TarUtils.formatUnsignedOctalString(123456, buffer, 0, 8);
        assertEquals(49, buffer[0]);
        assertEquals(50, buffer[1]);
        assertEquals(51, buffer[2]);
        assertEquals(52, buffer[3]);
        assertEquals(53, buffer[4]);
        assertEquals(54, buffer[5]);
        assertEquals(56, buffer[6]);
        assertEquals(48, buffer[7]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[8];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;

        assertEquals(8, TarUtils.formatOctalBytes(123456, buffer, 0, 8));
        assertEquals(49, buffer[0]);
        assertEquals(50, buffer[1]);
        assertEquals(51, buffer[2]);
        assertEquals(52, buffer[3]);
        assertEquals(53, buffer[4]);
        assertEquals(54, buffer[5]);
        assertEquals(56, buffer[6]);
        assertEquals(48, buffer[7]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[8];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;

        assertEquals(8, TarUtils.formatLongOctalBytes(123456, buffer, 0, 8));
        assertEquals(49, buffer[0]);
        assertEquals(50, buffer[1]);
        assertEquals(51, buffer[2]);
        assertEquals(52, buffer[3]);
        assertEquals(53, buffer[4]);
        assertEquals(54, buffer[5]);
        assertEquals(0, buffer[6]);
        assertEquals(0, buffer[7]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[8];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;

        assertEquals(8, TarUtils.formatCheckSumOctalBytes(123456, buffer, 0, 8));
        assertEquals(49, buffer[0]);
        assertEquals(50, buffer[1]);
        assertEquals(51, buffer[2]);
        assertEquals(52, buffer[3]);
        assertEquals(53, buffer[4]);
        assertEquals(54, buffer[5]);
        assertEquals(0, buffer[6]);
        assertEquals(32, buffer[7]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[8];
        buffer[0] = 49;
        buffer[1] = 50;
        buffer[2] = 51;
        buffer[3] = 52;
        buffer[4] = 53;
        buffer[5] = 54;
        buffer[6] = 55;
        buffer[7] = 56;

        assertEquals(392, TarUtils.computeCheckSum(buffer));
    }
}
