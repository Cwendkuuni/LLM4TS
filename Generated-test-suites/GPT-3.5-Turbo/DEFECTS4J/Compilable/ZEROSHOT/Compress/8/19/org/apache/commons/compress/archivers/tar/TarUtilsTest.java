package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(10, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0L;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0L;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0L;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(0L, result);
    }

}