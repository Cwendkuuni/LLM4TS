package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "123".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(83, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "test".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[10];
        int result = TarUtils.formatNameBytes(name, buf, 0, 4);
        assertEquals(4, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[10];
        TarUtils.formatUnsignedOctalString(123, buf, 0, buf.length);
        assertEquals("0000000173", new String(buf));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[10];
        int result = TarUtils.formatOctalBytes(123, buf, 0, buf.length);
        assertEquals(10, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[10];
        int result = TarUtils.formatLongOctalBytes(123, buf, 0, buf.length);
        assertEquals(10, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[10];
        int result = TarUtils.formatCheckSumOctalBytes(123, buf, 0, buf.length);
        assertEquals(10, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = "test".getBytes();
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(384, result);
    }
}