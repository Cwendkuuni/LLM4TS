package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        long result = TarUtils.parseOctal(buffer, 0, 8);
        assertEquals(0, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{65, 66, 67, 0, 0, 0, 0, 0};
        String result = TarUtils.parseName(buffer, 0, 8);
        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[8];
        int offset = TarUtils.formatNameBytes("ABC", buf, 0, 8);
        assertEquals(8, offset);
        assertArrayEquals(new byte[]{65, 66, 67, 0, 0, 0, 0, 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(12345, buffer, 0, 8);
        assertArrayEquals(new byte[]{48, 60, 51, 31, 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[8];
        int offset = TarUtils.formatOctalBytes(12345, buf, 0, 8);
        assertEquals(8, offset);
        assertArrayEquals(new byte[]{48, 60, 51, 31, 32, 0, 0, 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[8];
        int offset = TarUtils.formatLongOctalBytes(12345, buf, 0, 8);
        assertEquals(8, offset);
        assertArrayEquals(new byte[]{48, 60, 51, 31, 32, 0, 0, 0}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[8];
        int offset = TarUtils.formatCheckSumOctalBytes(12345, buf, 0, 8);
        assertEquals(8, offset);
        assertArrayEquals(new byte[]{48, 60, 51, 31, 0, 32, 0, 0}, buf);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{65, 66, 67, 0, 0, 0, 0, 0};
        long sum = TarUtils.computeCheckSum(buf);
        assertEquals(195, sum);
    }
}