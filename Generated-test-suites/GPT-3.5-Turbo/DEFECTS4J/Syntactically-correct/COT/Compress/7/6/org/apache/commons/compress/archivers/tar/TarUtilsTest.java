
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) '0';
        buffer[1] = (byte) '7';
        buffer[2] = (byte) '5';
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(075L, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "testfile.txt\0".getBytes();
        String result = TarUtils.parseName(buffer, 0, 13);
        assertEquals("testfile.txt", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testfile.txt";
        byte[] buf = new byte[13];
        TarUtils.formatNameBytes(name, buf, 0, 13);
        byte[] expected = "testfile.txt\0".getBytes();
        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789L;
        byte[] buf = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buf, 0, 10);
        byte[] expected = "123456567\0".getBytes();
        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789L;
        byte[] buf = new byte[12];
        TarUtils.formatOctalBytes(value, buf, 0, 12);
        byte[] expected = "123456567 \0".getBytes();
        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789L;
        byte[] buf = new byte[11];
        TarUtils.formatLongOctalBytes(value, buf, 0, 11);
        byte[] expected = "123456567 \0".getBytes();
        assertArrayEquals(expected, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789L;
        byte[] buf = new byte[12];
        TarUtils.formatCheckSumOctalBytes(value, buf, 0, 12);
        byte[] expected = "123456567\0 ".getBytes();
        assertArrayEquals(expected, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) '1';
        buffer[1] = (byte) '2';
        buffer[2] = (byte) '3';
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(381L, result);
    }
}