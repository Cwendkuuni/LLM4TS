package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {1, 2, 3, 0, 0};
        long result = TarUtils.parseOctal(buffer, 0, 5);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {97, 98, 99, 0, 0};
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes("test", buf, 0, 4);
        assertEquals('t', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
        assertEquals('t', buf[3]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buf, 0, 5);
        assertEquals('1', buf[0]);
        assertEquals('2', buf[1]);
        assertEquals('3', buf[2]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[6];
        int offset = TarUtils.formatOctalBytes(123, buf, 0, 6);
        assertEquals('1', buf[0]);
        assertEquals('2', buf[1]);
        assertEquals('3', buf[2]);
        assertEquals(' ', buf[3]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[6];
        int offset = TarUtils.formatLongOctalBytes(123, buf, 0, 6);
        assertEquals('1', buf[0]);
        assertEquals('2', buf[1]);
        assertEquals('3', buf[2]);
        assertEquals(' ', buf[3]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[6];
        int offset = TarUtils.formatCheckSumOctalBytes(123, buf, 0, 6);
        assertEquals('1', buf[0]);
        assertEquals('2', buf[1]);
        assertEquals('3', buf[2]);
        assertEquals(' ', buf[3]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {1, 2, 3, 4};
        long sum = TarUtils.computeCheckSum(buf);
        assertEquals(10, sum);
    }
}