
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 0;
        buffer[1] = 0;
        buffer[2] = 1;
        buffer[3] = 2;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 3;
        buffer[7] = 4;
        buffer[8] = 1;
        buffer[9] = 0;
        assertEquals(51324, TarUtils.parseOctal(buffer, 0, 10));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 97;  // a
        buffer[1] = 98;  // b
        buffer[2] = 0;
        buffer[3] = 0;
        buffer[4] = 0;
        buffer[5] = 0;
        buffer[6] = 0;
        buffer[7] = 0;
        buffer[8] = 0;
        buffer[9] = 0;
        assertEquals("ab", TarUtils.parseName(buffer, 0, 10));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int updatedOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals("abc", new String(buffer, offset, length));
        assertEquals(offset + length, updatedOffset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals("000123456", new String(buffer, offset, length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int updatedOffset = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals("0000000123 ", new String(buffer, offset, length));
        assertEquals(offset + length, updatedOffset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int updatedOffset = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals("123456789 ", new String(buffer, offset, length));
        assertEquals(offset + length, updatedOffset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int updatedOffset = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals("0000000123 ", new String(buffer, offset, length));
        assertEquals(offset + length, updatedOffset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = 1;
        buffer[1] = 2;
        buffer[2] = 3;
        buffer[3] = 4;
        buffer[4] = 5;
        buffer[5] = 6;
        buffer[6] = 7;
        buffer[7] = 8;
        buffer[8] = 9;
        buffer[9] = 10;
        assertEquals(55, TarUtils.computeCheckSum(buffer));
    }
}
