package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 54, 0, 32}; // "126" followed by space
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(126, result);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = {(byte) 0x80, 0, 0}; // binary number exceeding maximum signed long value
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, 3));
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = {1}; // true
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {97, 98, 99, 0, 0}; // "abc" with two NULs
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 3);
        assertArrayEquals(new byte[]{97, 98, 99, 0, 0, 0, 0, 0, 0, 0}, buffer);
        assertEquals(3, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatCheckSumOctalBytes(126, buffer, 0, 5);
        assertArrayEquals(new byte[]{49, 50, 54, 0, 32}, buffer); // "126" with trailing NUL and space
        assertEquals(5, offset);
    }

}