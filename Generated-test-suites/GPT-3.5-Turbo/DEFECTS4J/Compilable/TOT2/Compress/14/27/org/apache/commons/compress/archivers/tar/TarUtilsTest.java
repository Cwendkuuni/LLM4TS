package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0x30, 0x37, 0x32, 0x30, 0x20}; // "0720 "
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(720, result);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{(byte)0x80, 0x01}; // -127
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(-127, result);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{0x01};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x00}; // "Hello"
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Hello", result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234;
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("1234", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[512];
        long checkSum = TarUtils.computeCheckSum(header);
        long expectedCheckSum = 0;
        assertEquals(expectedCheckSum, checkSum);
    }

    // Include additional test cases for edge cases and exceptions

}