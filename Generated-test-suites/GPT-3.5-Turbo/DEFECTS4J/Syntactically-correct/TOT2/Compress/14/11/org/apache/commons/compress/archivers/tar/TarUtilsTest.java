import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = { '1', '2', '3', '4', ' ', 0 };
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(1234, result);
    }

    @Test
    public void testParseOctal_AllNUL() {
        byte[] buffer = { 0, 0, 0, 0, ' ' };
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = { 'a', 'b', 'c', 'd' };
        TarUtils.parseOctal(buffer, 0, 4);
    }

    // Additional test cases for other public methods in TarUtils class

}