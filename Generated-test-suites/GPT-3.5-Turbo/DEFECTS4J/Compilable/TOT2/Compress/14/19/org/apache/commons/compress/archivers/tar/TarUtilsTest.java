package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', 0, ' '}; // Valid octal string: "123"
        assertEquals(83L, TarUtils.parseOctal(buffer, 0, 4));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = {(byte) 0x80, 0, 0, 0, 0}; // Binary number: -2147483648
        assertEquals(-2147483648L, TarUtils.parseOctalOrBinary(buffer, 0, 5));
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = {1, 0, ' '}; // True
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0}; // Name: "test"
        assertEquals("test", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatNameBytes(name, buffer, offset, length);
        assertArrayEquals(new byte[] {'t', 'e', 's', 't', 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(456L, buffer, 0, 3);
        assertArrayEquals(new byte[] { '7', '1', '0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[3];
        TarUtils.formatOctalBytes(123L, buffer, 0, 3);
        assertArrayEquals(new byte[] { '1', '2', ' '}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = {'t', 'e', 's', 't', 0}; // Sample header
        assertEquals(448, TarUtils.computeCheckSum(header));
    }

}