package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = { '1', '2', '3', 0, '4', ' ' };
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName_validInput() {
        byte[] buffer = { 't', 'e', 's', 't', 0, 'n', 'a', 'm', 'e', ' ' };
        String expected = "test";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_validInput() {
        String name = "test";
        byte[] buf = new byte[8];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertEquals(8, offset);
        assertEquals('t', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
        assertEquals('t', buf[3]);
        assertEquals(0, buf[4]);
        assertEquals(0, buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(0, buf[7]);
    }

    @Test
    public void testFormatUnsignedOctalString_validInput() {
        byte[] buf = new byte[8];
        TarUtils.formatUnsignedOctalString(123, buf, 0, buf.length);
        String expected = "00000123";
        String result = new String(buf);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes_validInput() {
        byte[] buf = new byte[8];
        int offset = TarUtils.formatOctalBytes(123, buf, 0, buf.length);
        assertEquals(8, offset);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('0', buf[2]);
        assertEquals('0', buf[3]);
        assertEquals('0', buf[4]);
        assertEquals('0', buf[5]);
        assertEquals('1', buf[6]);
        assertEquals(' ', buf[7]);
    }

    @Test
    public void testComputeCheckSum_validInput() {
        byte[] buf = { 't', 'e', 's', 't', 0, 'n', 'a', 'm', 'e', ' ' };
        long expected = 569;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidInput() {
        byte[] buffer = { 't', 'e', 's', 't', ' ' };
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_valueTooLong() {
        byte[] buf = new byte[4];
        TarUtils.formatUnsignedOctalString(123456, buf, 0, buf.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytes_nameTooLong() {
        String name = "ThisNameIsTooLong";
        byte[] buf = new byte[8];
        TarUtils.formatNameBytes(name, buf, 0, buf.length);
    }
}