
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57}; // "0123456789"
        assertEquals(123456789, TarUtils.parseOctal(buffer, 0, 10));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{104, 101, 108, 108, 111, 0}; // "hello"
        assertEquals("hello", TarUtils.parseName(buffer, 0, 6));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatNameBytes("msg", buffer, 0, 5);
        assertEquals(5, offset);
        assertEquals(109, buffer[0]); // 'm'
        assertEquals(115, buffer[1]); // 's'
        assertEquals(103, buffer[2]); // 'g'
        assertEquals(0, buffer[3]); // NUL
        assertEquals(0, buffer[4]); // NUL
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
        assertEquals(48, buffer[9]); // '0'
        assertEquals(49, buffer[8]); // '1'
        assertEquals(50, buffer[7]); // '2'
        assertEquals(51, buffer[6]); // '3'
        assertEquals(52, buffer[5]); // '4'
        assertEquals(53, buffer[4]); // '5'
        assertEquals(54, buffer[3]); // '6'
        assertEquals(55, buffer[2]); // '7'
        assertEquals(56, buffer[1]); // '8'
        assertEquals(57, buffer[0]); // '9'
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, 12);
        assertEquals(12, offset);
        assertEquals(48, buffer[11]); // '0'
        assertEquals(49, buffer[10]); // '1'
        assertEquals(50, buffer[9]); // '2'
        assertEquals(51, buffer[8]); // '3'
        assertEquals(52, buffer[7]); // '4'
        assertEquals(53, buffer[6]); // '5'
        assertEquals(54, buffer[5]); // '6'
        assertEquals(55, buffer[4]); // '7'
        assertEquals(56, buffer[3]); // '8'
        assertEquals(57, buffer[2]); // '9'
        assertEquals(32, buffer[1]); // space
        assertEquals(0, buffer[0]); // NUL
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, 11);
        assertEquals(11, offset);
        assertEquals(49, buffer[10]); // '1'
        assertEquals(50, buffer[9]); // '2'
        assertEquals(51, buffer[8]); // '3'
        assertEquals(52, buffer[7]); // '4'
        assertEquals(53, buffer[6]); // '5'
        assertEquals(54, buffer[5]); // '6'
        assertEquals(55, buffer[4]); // '7'
        assertEquals(56, buffer[3]); // '8'
        assertEquals(57, buffer[2]); // '9'
        assertEquals(32, buffer[1]); // space
        assertEquals(0, buffer[0]); // NUL
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 12);
        assertEquals(12, offset);
        assertEquals(49, buffer[11]); // '1'
        assertEquals(50, buffer[10]); // '2'
        assertEquals(51, buffer[9]); // '3'
        assertEquals(52, buffer[8]); // '4'
        assertEquals(53, buffer[7]); // '5'
        assertEquals(54, buffer[6]); // '6'
        assertEquals(55, buffer[5]); // '7'
        assertEquals(56, buffer[4]); // '8'
        assertEquals(57, buffer[3]); // '9'
        assertEquals(0, buffer[2]); // NUL
        assertEquals(32, buffer[1]); // space
        assertEquals(0, buffer[0]); // NUL
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{49, 50, 51, 52, 53, 54, 55, 56, 57}; // "123456789"
        assertEquals(825, TarUtils.computeCheckSum(buffer));
    }
}
