import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {
    // Test for parseOctal method
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);

        // Test with empty buffer
        result = TarUtils.parseOctal(new byte[]{}, 0, 0);
        assertEquals(0L, result);

        // Test with invalid byte
        result = TarUtils.parseOctal(new byte[]{(byte) '8'}, 0, 1);
        fail("Expected IllegalArgumentException");
    }

    // Test for parseName method
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcde", result);
    }

    // Test for formatNameBytes method
    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[10];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, result);
        for (int i = 0; i < buffer.length; ++i) {
            assertEquals(name.charAt(i), (char) buffer[i]);
        }
    }

    // Test for formatUnsignedOctalString method
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int result = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(10, result);
        for (int i = 0; i < 8; ++i) {
            assertEquals('0', buffer[i]);
        }
        assertEquals('5', buffer[8]);
        assertEquals('7', buffer[9]);
    }

    // Test for formatOctalBytes method
    @Test
    public void testFormatOctalBytes() {
        long value = 1234567;
        byte[] buffer = new byte[13];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(13, result);
        for (int i = 0; i < 8; ++i) {
            assertEquals('0', buffer[i]);
        }
        assertEquals('5', buffer[8]);
        assertEquals('7', buffer[9]);
        assertEquals(' ', buffer[10]);
        assertEquals(0, buffer[11]);
        for (int i = 12; i < buffer.length; ++i) {
            assertEquals(0, buffer[i]);
        }
    }
}