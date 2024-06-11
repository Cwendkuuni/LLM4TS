import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validInput() {
        // Test valid input
        byte[] buffer = { '1', '2', '3', '4', '5', 0 };
        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(12345, result);
    }

    @Test
    public void testParseName_validInput() {
        // Test valid input
        byte[] buffer = { 't', 'e', 's', 't', 0, '1', '2' };
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes_validInput() {
        // Test valid input
        String name = "example";
        byte[] buffer = new byte[10];
        int result = TarUtils.formatNameBytes(name, buffer, 0, 7);
        assertEquals(7, result);
        assertEquals('e', buffer[0]);
        assertEquals('x', buffer[1]);
        assertEquals('a', buffer[2]);
        assertEquals('m', buffer[3]);
        assertEquals('p', buffer[4]);
        assertEquals('l', buffer[5]);
        assertEquals('e', buffer[6]);
        assertEquals(0, buffer[7]);
    }

    // Add more test cases for other methods

}