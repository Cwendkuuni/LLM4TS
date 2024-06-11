import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_typical() {
        // Typical input: 123456
        byte[] buffer = {'1', '2', '3', '4', '5', '6'};
        int offset = 0;
        int length = 6;
        long expectedResult = 123456;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseOctal_edge() {
        // Edge input: 0
        byte[] buffer = {'0'};
        int offset = 0;
        int length = 1;
        long expectedResult = 0;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_error() {
        // Error input: ABC
        byte[] buffer = {'A', 'B', 'C'};
        int offset = 0;
        int length = 3;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_typical() {
        // Typical input: "filename"
        byte[] buffer = {'f', 'i', 'l', 'e', 'n', 'a', 'm', 'e', 0};
        int offset = 0;
        int length = 9;
        String expectedResult = "filename";

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseName_edge() {
        // Edge input: empty name
        byte[] buffer = {0};
        int offset = 0;
        int length = 1;
        String expectedResult = "";

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes_typical() {
        // Typical input: "filename"
        String name = "filename";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = {'f', 'i', 'l', 'e', 'n', 'a', 'm', 'e', 0, 0};

        int resultOffset = TarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, resultOffset);
    }

    @Test
    public void testFormatNameBytes_edge() {
        // Edge input: empty name
        String name = "";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        byte[] expectedResult = {0, 0, 0, 0, 0};

        int resultOffset = TarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, resultOffset);
    }

    @Test
    public void testFormatUnsignedOctalString_typical() {
        // Typical input: 1234567890
        long value = 1234567890;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = {'4', '5', '6', '3', '2', '1', '0', '4', '2', '2'};

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(expectedResult, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_error() {
        // Error input: 12345678901234567890 (value larger than buffer length)
        long value = 12345678901234567890L;
        byte[] buffer = new byte[20];
        int offset = 0;
        int length = 20;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_typical() {
        // Typical input: 1234567890
        long value = 1234567890;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = {'4', '5', '6', '3', '2', '1', '0', ' ', 0, 0};

        int resultOffset = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, resultOffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_error() {
        // Error input: 12345678901234567890 (value larger than buffer length)
        long value = 12345678901234567890L;
        byte[] buf = new byte[20];
        int offset = 0;
        int length = 20;

        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes_typical() {
        // Typical input: 1234567890
        long value = 1234567890;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = {'4', '5', '6', '3', '2', '1', '0', ' ', 0, 0};

        int resultOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, resultOffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_error() {
        // Error input: 12345678901234567890 (value larger than buffer length)
        long value = 12345678901234567890L;
        byte[] buf = new byte[20];
        int offset = 0;
        int length = 20;

        TarUtils.formatLongOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_typical() {
        // Typical input: 1234567890
        long value = 1234567890;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = {'4', '5', '6', '3', '2', '1', '0', 0, ' ', 0};

        int resultOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, resultOffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_error() {
        // Error input: 12345678901234567890 (value larger than buffer length)
        long value = 12345678901234567890L;
        byte[] buf = new byte[20];
        int offset = 0;
        int length = 20;

        TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testComputeCheckSum_typical() {
        // Typical input: "abc123"
        byte[] buf = {'a', 'b', 'c', '1', '2', '3'};
        long expectedResult = 729;

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testComputeCheckSum_edge() {
        // Edge input: empty buffer
        byte[] buf = {};
        long expectedResult = 0;

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(expectedResult, result);
    }
}