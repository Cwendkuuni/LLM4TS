import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {49, 50, 51, 0, 0, 0}; // "123"
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(123, result);

        // Edge case
        byte[] buffer2 = {48, 48, 48, 49, 0, 0}; // "0001"
        long result2 = TarUtils.parseOctal(buffer2, 0, 4);
        assertEquals(1, result2);

        // Error scenario
        byte[] buffer3 = {49, 50, 51, 52}; // "1234"
        try {
            TarUtils.parseOctal(buffer3, 0, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer = {97, 98, 99, 0, 0}; // "abc"
        String result = TarUtils.parseName(buffer, 0, 3);
        assertEquals("abc", result);

        // Edge case
        byte[] buffer2 = {102, 0, 0, 0, 0}; // "f"
        String result2 = TarUtils.parseName(buffer2, 0, 1);
        assertEquals("f", result2);
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        byte[] buffer = new byte[5];
        int result = TarUtils.formatNameBytes("test", buffer, 0, 4);
        assertEquals(4, result);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);

        // Edge case
        byte[] buffer2 = new byte[3];
        int result2 = TarUtils.formatNameBytes("abc", buffer2, 0, 3);
        assertEquals(3, result2);
        assertEquals('a', buffer2[0]);
        assertEquals('b', buffer2[1]);
        assertEquals('c', buffer2[2]);
    }

    // Additional test cases for other methods in TarUtils class can be added here

}