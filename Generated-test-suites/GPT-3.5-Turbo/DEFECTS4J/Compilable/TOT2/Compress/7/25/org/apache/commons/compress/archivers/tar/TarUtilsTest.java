package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51, 0}; // "123\0"
        assertEquals(123, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0}; // "ABC\0"
        assertEquals("ABC", TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buf = new byte[10];
        TarUtils.formatNameBytes(name, buf, 0, name.length());
        String expected = "Test\0\0\0\0\0";
        assertArrayEquals(expected.getBytes(), buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buf = new byte[10];
        TarUtils.formatUnsignedOctalString(123, buf, 0, 10);
        String expected = "0000000123";
        assertArrayEquals(expected.getBytes(), buf);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[10];
        TarUtils.formatOctalBytes(123, buf, 0, 10);
        String expected = "0000000123 ";
        assertArrayEquals(expected.getBytes(), buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[10];
        TarUtils.formatLongOctalBytes(123, buf, 0, 10);
        String expected = "0000000123 ";
        assertArrayEquals(expected.getBytes(), buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[10];
        TarUtils.formatCheckSumOctalBytes(123, buf, 0, 10);
        String expected = "0000000123 \0";
        assertArrayEquals(expected.getBytes(), buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = {49, 50, 51}; // "123"
        assertEquals(6, TarUtils.computeCheckSum(header));
    }
}