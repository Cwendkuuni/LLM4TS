package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "123456\0".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(123456L, result);
    }

    @Test
    public void testParseOctal_invalidLength() {
        byte[] buffer = "123456\0".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = {(byte)0x80};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, 1);
        assertEquals(128L, result);
    }

    @Test
    public void testParseBoolean_true() {
        byte[] buffer = {1};
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    @Test
    public void testParseBoolean_false() {
        byte[] buffer = {0};
        assertFalse(TarUtils.parseBoolean(buffer, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = "hello\0".getBytes();
        String result = TarUtils.parseName(buffer, 0, 6);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes("world", buffer, 0, 5);
        assertEquals("world", new String(buffer, 0, 5));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "Hello, World!".getBytes();
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(1010L, result);
    }

}