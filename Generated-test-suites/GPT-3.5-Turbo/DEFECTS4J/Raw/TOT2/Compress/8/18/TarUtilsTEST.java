import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = {1, 2, 3};
        int offset = 0;
        int length = 3;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(123, result);
    }

    @Test
    public void testParseOctal_invalidInput() {
        byte[] buffer = {1, 2, 'a'};
        int offset = 0;
        int length = 3;

        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Should throw IllegalArgumentException for invalid input");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName_validInput() {
        byte[] buffer = {65, 66, 0, 0};
        int offset = 0;
        int length = 3;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("AB", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(5, result);
        assertEquals('T', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
        assertEquals('t', buf[3]);
        assertEquals(0, buf[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3};
        long expectedSum = 6L;

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(expectedSum, result);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}