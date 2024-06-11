package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {'1', '2', '3', ' '};
        assertEquals(123, TarUtils.parseOctal(buffer, 0, 3));

        // Edge case: buffer with all NULs
        byte[] allNULBuffer = {0, 0, 0, ' '};
        assertEquals(0, TarUtils.parseOctal(allNULBuffer, 0, 3));

        // Exception handling: trailing space/NUL missing
        byte[] missingTrailerBuffer = {'1', '2', '3'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(missingTrailerBuffer, 0, 3));
    }

    @Test
    public void testParseOctalOrBinary() {
        // Typical use case: octal number
        byte[] octalBuffer = {'4', '2', ' '};
        assertEquals(42, TarUtils.parseOctalOrBinary(octalBuffer, 0, 2));

        // Typical use case: binary number
        byte[] binaryBuffer = {(byte)0xFF, (byte)0xFF, ' '};
        assertEquals(65535, TarUtils.parseOctalOrBinary(binaryBuffer, 0, 2));

        // Edge case: binary number exceeds maximum signed long value
        byte[] largeBinaryBuffer = {(byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, ' '};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(largeBinaryBuffer, 0, 6));
    }

    // Additional test cases for other methods should be added

}