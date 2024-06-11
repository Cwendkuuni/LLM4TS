package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "123".getBytes();
        int offset = 0;
        int length = 3;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "Test".getBytes();
        int offset = 0;
        int length = 4;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("Test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Testing";
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(8, newOffset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(123, buffer, offset, length);
        assertEquals("00123", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int newOffset = TarUtils.formatOctalBytes(123, buf, offset, length);
        assertEquals(5, newOffset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = "Test".getBytes();
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(416, result);
    }

}