package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', '4', '5', '6'};
        int offset = 0;
        int length = 6;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(123456, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'h', 'e', 'l', 'l', 'o'};
        int offset = 0;
        int length = 5;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "world";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(5, newOffset);
        assertArrayEquals(new byte[]{'w', 'o', 'r', 'l', 'd', 0, 0, 0, 0, 0}, buf);
    }

    // More test cases for the remaining methods...

}