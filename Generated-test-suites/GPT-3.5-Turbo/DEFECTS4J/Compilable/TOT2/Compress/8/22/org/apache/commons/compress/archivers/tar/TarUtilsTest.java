package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) '1';
        buffer[1] = (byte) '0';
        buffer[2] = (byte) ' ';
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(8, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[5];
        buffer[0] = (byte) 'T';
        buffer[1] = (byte) 'E';
        buffer[2] = (byte) 'S';
        buffer[3] = (byte) 'T';
        buffer[4] = 0;
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("TEST", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];
        int offset = TarUtils.formatNameBytes("TEST", buf, 0, 5);
        assertEquals(5, offset);
        assertEquals('T', buf[0]);
        assertEquals('E', buf[1]);
        assertEquals('S', buf[2]);
        assertEquals('T', buf[3]);
        assertEquals(0, buf[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(8, buffer, 0, 3);
        assertEquals('0', buffer[0]);
        assertEquals('1', buffer[1]);
        assertEquals('0', buffer[2]);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[4];
        int offset = TarUtils.formatOctalBytes(8, buf, 0, 4);
        assertEquals(4, offset);
        assertEquals('0', buf[0]);
        assertEquals('1', buf[1]);
        assertEquals('0', buf[2]);
        assertEquals(' ', buf[3]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[4];
        header[0] = (byte) 't';
        header[1] = (byte) 'e';
        header[2] = (byte) 's';
        header[3] = (byte) 't';
        long sum = TarUtils.computeCheckSum(header);
        assertEquals(448, sum);
    }

}