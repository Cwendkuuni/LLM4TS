import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "12345 ".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(12345, result);
    }

    @Test
    public void testParseOctal_WithLeadingNUL() {
        byte[] buffer = "\0\0\0\0\0\0123 ".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = "12x45 ".getBytes(); // Invalid byte 'x'
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    // More test cases for parseOctal can be added here

    @Test
    public void testParseOctalOrBinary_Octal() {
        byte[] buffer = "12345 ".getBytes();
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(12345, result);
    }

    @Test
    public void testParseOctalOrBinary_Binary() {
        byte[] buffer = new byte[] { (byte) 0x80, 0, 0, 0, 0, 0, 0, 1 }; // Binary value 9223372036854775809
        long result = TarUtils.parseOctalOrBinary(buffer, 0, 8);
        assertEquals(9223372036854775809L, result);
    }

    // More test cases for parseOctalOrBinary can be added here

    // Additional test cases for other methods can be added here

}