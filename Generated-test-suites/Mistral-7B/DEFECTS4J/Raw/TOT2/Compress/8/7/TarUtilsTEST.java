import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {
    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '6', '4', '7', '7', '5', '5', '6', '4', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175564775564L, result); // Octal value
    }

    @Test
    public void testParseOctal_TrailingZeroes() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '6', '4', '7', '7', '5', '5', '6', '4', '0', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175564775564L, result); // Octal value
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '6', '4', '7', '7', '5', '5', '6', '4', 'X'};
        TarUtils.parseOctal(buffer, 0, buffer.length); // This will throw an IllegalArgumentException
    }

    @Test
    public void testParseOctal_EmptyInput() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result); // Empty input
    }

    // Other test methods for other TarUtils methods go here
}