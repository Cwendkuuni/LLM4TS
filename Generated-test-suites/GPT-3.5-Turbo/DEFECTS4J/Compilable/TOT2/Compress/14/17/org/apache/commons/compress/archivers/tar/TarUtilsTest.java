package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {'0', '1', '2', '3', '4', ' '};
        int offset = 0;
        int length = 5;
        long expected = 1234;
        assertEquals(expected, TarUtils.parseOctal(buffer, offset, length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[] {(byte) 0x80, 0x01, 0x00};
        int offset = 0;
        int length = 3;
        long expected = 128;
        assertEquals(expected, TarUtils.parseOctalOrBinary(buffer, offset, length));
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[] {1};
        int offset = 0;
        assertTrue(TarUtils.parseBoolean(buffer, offset));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {'T', 'e', 's', 't', 0, ' '};
        int offset = 0;
        int length = 5;
        String expected = "Test";
        assertEquals(expected, TarUtils.parseName(buffer, offset, length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int expectedOffset = 5;
        assertEquals(expectedOffset, TarUtils.formatNameBytes(name, buf, offset, length));
        String expected = "Test" + (char) 0;
        assertArrayEquals(expected.getBytes(), buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "1234";
        assertArrayEquals(expected.getBytes(), buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[] {1, 2, 3, 4, 5};
        long expected = 15;
        assertEquals(expected, TarUtils.computeCheckSum(buf));
    }

}