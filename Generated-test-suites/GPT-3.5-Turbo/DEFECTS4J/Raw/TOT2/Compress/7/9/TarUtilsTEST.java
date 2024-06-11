import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {50, 51, 54, 0}; // "234"
        long expected = 234;
        assertEquals(expected, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67, 0}; // "ABC"
        String expected = "ABC";
        assertEquals(expected, TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "ABC";
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;
        TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] expected = {65, 66, 67}; // "ABC"
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = {48, 51, 50, 51}; // "123"
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;
        int updatedOffset = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, updatedOffset);
        byte[] expected = {48, 51, 50, 32}; // "123 "
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        int updatedOffset = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, updatedOffset);
        byte[] expected = {48, 51, 50, 51, 52, 32}; // "123456 "
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        int updatedOffset = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, updatedOffset);
        byte[] expected = {48, 51, 50, 51, 52, 32}; // "123456 "
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {1, 2, 3, 4, 5}; // Dummy values for testing
        long expectedSum = 15;
        assertEquals(expectedSum, TarUtils.computeCheckSum(buf));
    }
}