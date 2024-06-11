package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51};
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {97, 98, 99, 0};
        String expected = "abc";
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int expected = 5;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, result);
        assertEquals((byte)116, buf[0]);
        assertEquals((byte)101, buf[1]);
        assertEquals((byte)115, buf[2]);
        assertEquals((byte)116, buf[3]);
        assertEquals((byte)0, buf[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        assertEquals((byte)51, buf[0]);
        assertEquals((byte)55, buf[1]);
        assertEquals((byte)0, buf[2]);
        assertEquals((byte)0, buf[3]);
        assertEquals((byte)0, buf[4]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidValue() {
        long value = 999;
        byte[] buf = new byte[2];
        int offset = 0;
        int length = 2;
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int expected = 5;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        assertEquals((byte)51, buf[0]);
        assertEquals((byte)55, buf[1]);
        assertEquals((byte)32, buf[2]);
        assertEquals((byte)0, buf[3]);
        assertEquals((byte)0, buf[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        int expected = 4;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        assertEquals((byte)51, buf[0]);
        assertEquals((byte)55, buf[1]);
        assertEquals((byte)32, buf[2]);
        assertEquals((byte)0, buf[3]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int expected = 5;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        assertEquals((byte)51, buf[0]);
        assertEquals((byte)55, buf[1]);
        assertEquals((byte)0, buf[2]);
        assertEquals((byte)32, buf[3]);
        assertEquals((byte)0, buf[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {49, 50, 51};
        long expected = 6;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

}