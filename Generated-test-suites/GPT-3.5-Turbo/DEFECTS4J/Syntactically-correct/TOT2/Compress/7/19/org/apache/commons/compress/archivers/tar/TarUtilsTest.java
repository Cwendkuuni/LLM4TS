import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] { 49, 50, 51, 0 };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] { 65, 66, 67, 0, 0, 0 };
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buffer = new byte[6];
        int result = TarUtils.formatNameBytes(name, buffer, 0, 4);
        assertEquals(4, result);
        assertArrayEquals(new byte[] { 84, 101, 115, 116, 0, 0 }, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 3);
        assertArrayEquals(new byte[] { 49, 57, 0 }, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[3];
        int result = TarUtils.formatOctalBytes(123, buffer, 0, 3);
        assertEquals(3, result);
        assertArrayEquals(new byte[] { 49, 50, 0 }, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] { 1, 2, 3, 4, 5 };
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(15, result);
    }

    @Test
    public void testInvalidOctalDigit() {
        byte[] buffer = new byte[] { 48, 49, 50, 55, 0 };
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInvalidValueFitInBuffer() {
        byte[] buffer = new byte[2];
        try {
            TarUtils.formatUnsignedOctalString(123, buffer, 0, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testValueTooLongForBuffer() {
        String name = "ThisIsALongName";
        byte[] buffer = new byte[5];
        int result = TarUtils.formatNameBytes(name, buffer, 0, 5);
        assertEquals(5, result);
        assertArrayEquals(new byte[] { 84, 104, 105, 115, 0 }, buffer);
    }
}