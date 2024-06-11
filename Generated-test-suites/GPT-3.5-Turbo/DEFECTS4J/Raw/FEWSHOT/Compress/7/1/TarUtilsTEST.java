
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = "123456\0".getBytes();
        assertEquals(123456L, TarUtils.parseOctal(buffer, 0, 6));
    }

    @Test
    public void testParseName() {
        byte[] buffer = "test\0".getBytes();
        assertEquals("test", TarUtils.parseName(buffer, 0, 4));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes("test", buffer, 0, 4);
        String result = new String(buffer, 0, offset);
        assertEquals("test\0\0\0\0\0", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(123456, buffer, 0, 6);
        String result = new String(buffer);
        assertEquals("0123456", result);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(123456, buffer, 0, 8);
        String result = new String(buffer, 0, offset);
        assertEquals("0123456 \0", result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(123456, buffer, 0, 9);
        String result = new String(buffer, 0, offset);
        assertEquals("0123456 ", result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(123456, buffer, 0, 9);
        String result = new String(buffer, 0, offset);
        assertEquals("0123456\0 ", result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "123456\0".getBytes();
        assertEquals(54, TarUtils.computeCheckSum(buffer));
    }
}
