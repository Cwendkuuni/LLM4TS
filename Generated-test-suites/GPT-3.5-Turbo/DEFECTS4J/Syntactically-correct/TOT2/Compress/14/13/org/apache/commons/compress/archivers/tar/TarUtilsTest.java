import static org.junit.Assert.*;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = new byte[] { '0', '1', '2', '3', ' ' };
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(123L, result);

        // Edge case - All NULs
        buffer = new byte[] { 0, 0, 0, 0, ' ' };
        result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(0L, result);

        // Exception handling - Invalid byte
        buffer = new byte[] { '0', '1', '2', 'a', ' ' };
        try {
            TarUtils.parseOctal(buffer, 0, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        // Typical use case
        byte[] buffer = new byte[] { (byte) 0b10010110, ' '};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, 2);
        assertEquals(150L, result);

        // Edge case - Binary number exceeds maximum signed long value
        buffer = new byte[] { (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000, ' '};
        try {
            TarUtils.parseOctalOrBinary(buffer, 0, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Additional test cases for other methods can be added here

}