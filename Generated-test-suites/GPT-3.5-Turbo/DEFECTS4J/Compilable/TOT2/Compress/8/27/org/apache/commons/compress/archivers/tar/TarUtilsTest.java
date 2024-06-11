package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = "1234".getBytes();
        int offset = 0;
        int length = 4;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(1234, result);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = "test".getBytes();
        int offset = 0;
        int length = 4;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "test";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 4;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(4, result);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 1234;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals("1234", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 1234;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertEquals(6, result);
    }

    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buf = "test".getBytes();

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(448, result);
    }
}