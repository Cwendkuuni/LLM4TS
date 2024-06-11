package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_basic() {
        byte[] buffer = {'1', '2', '3', '4', '5'};
        long result = TarUtils.parseOctal(buffer, 0, 5);
        assertEquals(12345, result);
    }

    @Test
    public void testParseName_basic() {
        byte[] buffer = {'h', 'e', 'l', 'l', 'o'};
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes_basic() {
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatNameBytes("hello", buffer, 0, 5);
        assertEquals("hello", new String(buffer, 0, 5).trim());
    }

    // Add more test cases for other methods here based on edge cases and exception handling scenarios

}