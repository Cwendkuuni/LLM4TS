package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 49; // ascii value of '1'
        buffer[1] = 50; // ascii value of '2'
        buffer[2] = 48; // ascii value of '0'
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(120, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 104; // ascii value of 'h'
        buffer[1] = 101; // ascii value of 'e'
        buffer[2] = 108; // ascii value of 'l'
        buffer[3] = 108; // ascii value of 'l'
        buffer[4] = 111; // ascii value of 'o'
        buffer[5] = 0; // null termination
        String result = TarUtils.parseName(buffer, 0, 6);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "world";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 5);
        assertEquals(5, offset);
        assertEquals(119, buffer[0]);
        assertEquals(111, buffer[1]);
        assertEquals(114, buffer[2]);
        assertEquals(108, buffer[3]);
        assertEquals(100, buffer[4]);
        assertEquals(0, buffer[5]);
        assertEquals(0, buffer[6]);
        assertEquals(0, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(0, buffer[9]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);
        assertEquals(51, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[5];
        TarUtils.formatOctalBytes(123, buffer, 0, 5);
        assertEquals(51, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(32, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[5];
        TarUtils.formatLongOctalBytes(123, buffer, 0, 5);
        assertEquals(51, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[5];
        TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 5);
        assertEquals(51, buffer[0]);
        assertEquals(55, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(32, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[5];
        buffer[0] = 49; // ascii value of '1'
        buffer[1] = 50; // ascii value of '2'
        buffer[2] = 51; // ascii value of '3'
        buffer[3] = 0; // null termination
        buffer[4] = 0; // padding
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(200, result);
    }
}
