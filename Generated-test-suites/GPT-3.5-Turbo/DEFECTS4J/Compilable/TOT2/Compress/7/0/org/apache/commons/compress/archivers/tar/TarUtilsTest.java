package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {'1', '2', '3', 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);

        // Edge case: buffer with all zeros
        byte[] bufferZero = {0, 0, 0, 0};
        long resultZero = TarUtils.parseOctal(bufferZero, 0, bufferZero.length);
        assertEquals(0, resultZero);

        // Error scenario: invalid octal digit
        byte[] bufferError = {'a', 'b', 'c', 0};
        try {
            TarUtils.parseOctal(bufferError, 0, bufferError.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer = {'t', 'e', 's', 't', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("test", result);

        // Edge case: buffer with trailing NUL
        byte[] bufferNUL = {'t', 'e', 's', 't', 0, 'e'};
        String resultNUL = TarUtils.parseName(bufferNUL, 0, 4);
        assertEquals("test", resultNUL);
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        String name = "test";
        byte[] buffer = new byte[5];
        int newOffset = TarUtils.formatNameBytes(name, buffer, 0, 4);
        assertEquals("test\0\0", new String(buffer));

        // Edge case: longer name than buffer
        String longName = "longName";
        byte[] bufferLong = new byte[5];
        int newOffsetLong = TarUtils.formatNameBytes(longName, bufferLong, 0, 5);
        assertEquals("longN", new String(bufferLong));
    }

    // Additional test methods for other public methods in TarUtils class can be added here

}