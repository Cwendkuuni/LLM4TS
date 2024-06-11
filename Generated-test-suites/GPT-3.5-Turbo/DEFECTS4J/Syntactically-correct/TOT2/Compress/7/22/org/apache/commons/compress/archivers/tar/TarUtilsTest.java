import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test basic functionality
        byte[] buffer = "12345".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(12345, result);

        // Test edge case with leading zeros
        buffer = "000".getBytes();
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0, result);

        // Test exception handling with non-octal input
        buffer = "12A45".getBytes();
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));

        // Add more test scenarios here...
    }

    @Test
    public void testParseName() {
        // Test basic functionality
        byte[] buffer = "TestName\0".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("TestName", result);

        // Test edge case with null character in the middle
        buffer = "Test\0Name".getBytes();
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Test", result);

        // Add more test scenarios here...
    }

    // Add test cases for the remaining public methods in TarUtils...

}