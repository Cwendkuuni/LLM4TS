package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{48, 49}; // ASCII values for '0' and '1'
        assertEquals(1, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctalWithLeadingNul() {
        byte[] buffer = new byte[]{0, 48}; // Leading NUL followed by ASCII value for '0'
        assertEquals(0, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[]{65}; // ASCII value for 'A' (invalid byte)
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{(byte) 0x80, 1}; // Binary representation of 128 + 1 = 129
        assertEquals(129, TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    // Test cases for other methods can be added following the same structure
}