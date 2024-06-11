package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {'0', '1', '2', '3', 0};
        assertEquals(123L, TarUtils.parseOctal(buffer, 0, 4));
    }

    @Test
    public void testParseOctalWithLeadingSpaces() {
        byte[] buffer = new byte[] {' ', ' ', '1', '0', '1', 0};
        assertEquals(101L, TarUtils.parseOctal(buffer, 0, 6));
    }

    // Add more test cases for parseOctal edge cases and exceptions

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {'T', 'e', 's', 't', 0, 'E', 'n', 'd'};
        assertEquals("Test", TarUtils.parseName(buffer, 0, 5));
    }

    // Add more test cases for parseName edge cases and exceptions

    // Repeat the above structure for all public methods in TarUtils

}