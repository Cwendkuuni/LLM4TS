package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {'1', '2', '3', '4', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(1234, result);

        // Edge case: Buffer with all NULs
        byte[] allNULs = {0, 0, 0, 0, 0};
        long resultAllNULs = TarUtils.parseOctal(allNULs, 0, allNULs.length);
        assertEquals(0, resultAllNULs);

        // Error scenario: Invalid byte detected
        byte[] invalidByte = {'1', '2', '3', 'x', ' '};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(invalidByte, 0, invalidByte.length);
        });
    }

    @Test
    public void testParseOctalOrBinary() {
        // Typical use case
        byte[] buffer = {127, '1', '2', '3', '4'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(21234, result);

        // Edge case: Buffer with octal value
        byte[] octalValue = {63, '7', '7', '7', ' '};
        long resultOctal = TarUtils.parseOctalOrBinary(octalValue, 0, octalValue.length);
        assertEquals(7777, resultOctal);

        // Error scenario: Binary number exceeds maximum signed long value
        byte[] exceedMaxValue = {(byte) 0x80, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctalOrBinary(exceedMaxValue, 0, exceedMaxValue.length);
        });
    }

    // Add test cases for other public methods in the TarUtils class

}