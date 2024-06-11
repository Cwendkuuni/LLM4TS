import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = new byte[] {'0', '5', '7', '7', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(577L, result);
    }

    @Test
    public void testParseOctal_invalidInput() {
        byte[] buffer = new byte[] {'0', '5', 'G', '7', ' '}; // 'G' is an invalid octal digit
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases for other methods here

}