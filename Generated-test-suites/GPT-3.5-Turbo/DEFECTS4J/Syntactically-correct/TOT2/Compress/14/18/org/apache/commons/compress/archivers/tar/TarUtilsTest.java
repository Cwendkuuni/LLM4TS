import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {'1', '2', '3', ' '};
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(83, result);

        // Edge case: All NULs
        byte[] allNulBuffer = {0, 0, 0, 0};
        long allNulResult = TarUtils.parseOctal(allNulBuffer, 0, 4);
        assertEquals(0, allNulResult);

        // Exception handling: Missing trailing space
        byte[] invalidBuffer = {'1', '2', '3'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        // Typical use case: parse as octal
        byte[] octalBuffer = {63, '1', '2', '3', ' '};
        long octalResult = TarUtils.parseOctalOrBinary(octalBuffer, 1, 3);
        assertEquals(83, octalResult);

        // Typical use case: parse as binary
        byte[] binaryBuffer = {127, (byte) 255, 0, 0};
        long binaryResult = TarUtils.parseOctalOrBinary(binaryBuffer, 0, 4);
        assertEquals(2130706432, binaryResult);
    }

    // Add more test methods for other public methods in the TarUtils class

}