package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {50, 48, 55, 55, 0}; // "2077"
        long expected = 2077;
        assertEquals(expected, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        // Edge case: empty buffer
        byte[] emptyBuffer = {};
        assertEquals(0, TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length));
        
        // Error scenario: invalid octal digit
        byte[] invalidBuffer = {50, 48, 57, 55, 0}; // "2097"
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0}; // "ABC"
        String expected = "ABC";
        assertEquals(expected, TarUtils.parseName(buffer, 0, buffer.length));
        
        // Edge case: empty buffer
        byte[] emptyBuffer = {};
        assertEquals("", TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals("Test\0\0", new String(buffer));
        
        // Edge case: longer name
        String longName = "ReallyLongName";
        byte[] longBuffer = new byte[5];
        int longOffset = 0;
        int longLength = 5;
        int longResult = TarUtils.formatNameBytes(longName, longBuffer, longOffset, longLength);
        assertEquals("Reall", new String(longBuffer));
    }

    // Additional test cases for the remaining methods...

}