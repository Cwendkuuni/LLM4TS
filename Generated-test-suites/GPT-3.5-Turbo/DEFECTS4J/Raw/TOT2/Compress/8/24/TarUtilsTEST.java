import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = {'1', '2', '3', '4', (byte) ' '};
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(1234, result);
    }

    @Test
    public void testParseOctal_missingTrailingSpace() {
        byte[] buffer = {'1', '2', '3', '4'};
        try {
            TarUtils.parseOctal(buffer, 0, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testParseName_validInput() {
        byte[] buffer = {'t', 'e', 's', 't', 0};
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals("test", result);
    }

    @Test
    public void testFormatLongOctalBytes_validInput() {
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatLongOctalBytes(1234, buffer, 0, 4);
        assertEquals('1', buffer[0]);
        assertEquals('0', buffer[1]);
        assertEquals('2', buffer[2]);
        assertEquals(' ', buffer[3]); 
    }

    @Test
    public void testComputeCheckSum_validInput() {
        byte[] buffer = {'t', 'e', 's', 't'};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(432, result);
    }

    // Add more test cases for all other methods

}