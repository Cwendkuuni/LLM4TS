import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3'};
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(123, result);

        byte[] buffer2 = {'0', '1', '2', '3'};
        long result2 = TarUtils.parseOctal(buffer2, 0, 4);
        assertEquals(123, result2);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0};
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("test", result);

        byte[] buffer2 = {'t', 'e', 's', 't', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        String result2 = TarUtils.parseName(buffer2, 0, 16);
        assertEquals("test", result2);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(10, result);

        byte[] expected = {'t', 'e', 's', 't', 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = {'0', '1', '7', '3', 0};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(5, result);

        byte[] expected = {'0', '1', '7', '3', ' ', 0};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(5, result);

        byte[] expected = {'0', '1', '7', '3', ' '};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(5, result);

        byte[] expected = {'0', '1', '7', '3', 0};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3};

        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(6, result);
    }
}