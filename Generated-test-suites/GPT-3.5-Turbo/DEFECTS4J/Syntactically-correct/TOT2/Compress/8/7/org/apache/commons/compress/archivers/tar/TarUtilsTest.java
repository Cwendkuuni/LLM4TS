import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = {49, 50, 51, 0}; // ASCII values for '123'
        assertEquals(123L, TarUtils.parseOctal(buffer, 0, 3));

        // Edge case - all NULs
        byte[] allZeros = {0, 0, 0, 0};
        assertEquals(0L, TarUtils.parseOctal(allZeros, 0, 4));

        // Exception handling scenario - invalid byte detected
        byte[] invalidByte = {49, 50, 56, 0}; // ASCII values for '128'
        try {
            TarUtils.parseOctal(invalidByte, 0, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer = {97, 98, 99, 0}; // ASCII values for 'abc'
        assertEquals("abc", TarUtils.parseName(buffer, 0, 3));

        // Edge case - character with sign extension
        byte[] withSignExtension = {127, 0}; // ASCII value for DEL character
        assertEquals("�", TarUtils.parseName(withSignExtension, 0, 1));
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        byte[] output = new byte[10];
        int offset = TarUtils.formatNameBytes("test", output, 0, 4);
        byte[] expected = {116, 101, 115, 116, 0, 0, 0, 0, 0, 0}; // ASCII values for 'test'
        assertArrayEquals(expected, output);

        // Edge case - name longer than buffer
        offset = TarUtils.formatNameBytes("verylongname", output, 0, 10);
        byte[] expectedLong = {118, 101, 114, 121, 108, 111, 110, 103, 110, 0}; // ASCII values for 'verylongn'
        assertArrayEquals(expectedLong, output);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] input = "test".getBytes(); // ASCII values for 'test'
        assertEquals(448L, TarUtils.computeCheckSum(input));
    }

    // Add more test cases for other methods as needed

}