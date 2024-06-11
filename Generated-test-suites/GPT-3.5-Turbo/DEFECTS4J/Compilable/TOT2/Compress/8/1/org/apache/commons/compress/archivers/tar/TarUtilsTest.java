package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {48, 55, 51, 56, 0}; // Represents "0738" in octal
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(738, result);
        
        // Edge case with all NULs
        byte[] allNULs = {0, 0, 0};
        result = TarUtils.parseOctal(allNULs, 0, allNULs.length);
        assertEquals(0, result);
        
        // Error scenario missing trailing space/NUL
        byte[] missingSpace = {48, 55, 51, 56}; // Missing trailing NUL
        try {
            result = TarUtils.parseOctal(missingSpace, 0, missingSpace.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0, 0}; // Represents "ABC" in ASCII
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("ABC", result);
        
        // Edge case with name longer than buffer
        byte[] longerName = {65, 66}; // Represents "AB" in ASCII
        result = TarUtils.parseName(longerName, 0, longerName.length);
        assertEquals("AB", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Testing";
        byte[] buf = new byte[8]; // Buffer length greater than name
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        
        assertEquals(8, offset);
        assertEquals("Testing", new String(buf));
        
        // Edge case with name longer than buffer
        String longName = "ThisIsALongName";
        byte[] buf2 = new byte[5]; // Buffer length less than name
        int offset2 = TarUtils.formatNameBytes(longName, buf2, 0, buf2.length);
        
        assertEquals(5, offset2);
        assertEquals("ThisI", new String(buf2));
    }

    // Add more test cases for the remaining methods...

}