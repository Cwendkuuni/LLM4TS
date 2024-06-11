package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {'1', '2', '3', ' ', ' ', ' '};
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(123, result);

        // Edge case
        buffer = new byte[2];
        result = TarUtils.parseOctal(buffer, 0, 2);
        assertEquals(0, result);

        // Exception handling
        buffer = new byte[]{'1', '2', '8'};
        try {
            TarUtils.parseOctal(buffer, 0, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        // Typical use case
        byte[] buffer = {127, -1, -1};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, 3);
        assertEquals(2147483647, result);

        // Edge case
        buffer = new byte[]{0, 0, 0};
        result = TarUtils.parseOctalOrBinary(buffer, 0, 3);
        assertEquals(0, result);

        // Exception handling
        buffer = new byte[]{127, -1, -1, -1};
        try {
            TarUtils.parseOctalOrBinary(buffer, 0, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // More test methods for other public methods in TarUtils class

}