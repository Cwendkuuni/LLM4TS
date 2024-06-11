import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51, 52}; // ASCII values of '1', '2', '3', '4'
        assertEquals(1234, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = {49, 50, 51, 172}; // ASCII values of '1', '2', '3', invalid byte
        assertThrows(IllegalArgumentException.class, () -> 
            TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = {0x2A}; // Binary value of 42
        assertEquals(42, TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = {1};
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    // Additional test cases for other methods in TarUtils can be added here...

}