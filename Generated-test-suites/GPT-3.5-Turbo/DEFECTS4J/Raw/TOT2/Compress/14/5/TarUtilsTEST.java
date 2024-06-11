import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{' ', ' ', '1', '2', '3', ' '};
        long result = TarUtils.parseOctal(buffer, 2, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseOctal_AllNUL() {
        byte[] buffer = new byte[]{'\0', '\0', '\0'};
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(0, result);
    }

    @Test
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{'1', '2', 'g', '4', '5', ' '};
        try {
            TarUtils.parseOctal(buffer, 2, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    // Add more test cases for other methods here...

}