import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical use case
        byte[] buffer = new byte[] {'1', '2', '3', '4', ' '};
        int offset = 0;
        int length = 5;
        long expectedResult = 1234;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, result);

        // Edge case - buffer contains all NULs
        buffer = new byte[] {0, 0, 0, 0, ' '};
        offset = 0;
        length = 5;
        expectedResult = 0;
        result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, result);

        // Error scenario - invalid byte detected
        buffer = new byte[] {'1', '2', 'A', '4', ' '};
        offset = 0;
        length = 5;
        try {
            result = TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testParseName() {
        // Typical use case
        byte[] buffer = new byte[] {'t', 'e', 's', 't', 0};
        int offset = 0;
        int length = 5;
        String expectedResult = "test";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, result);

        // Edge case - buffer length is reached
        buffer = new byte[] {'t', 'e', 's', 't'};
        offset = 0;
        length = 4;
        expectedResult = "test";
        result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes() {
        // Typical use case
        String name = "test";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = new byte[] {'t', 'e', 's', 't', 0, 0, 0, 0, 0, 0};
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, result);

        // Edge case - buffer is longer than name
        name = "test";
        buf = new byte[2];
        offset = 0;
        length = 2;
        expectedResult = new byte[] {'t', 'e'};
        result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatOctalBytes() {
        // Typical use case
        long value = 1234;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = new byte[] {'0', '0', '0', '0', '0', '0', '0', '0', '1', '2'}; // ASCII representation of 1234
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, result);

        // Error scenario - value will not fit in the buffer
        value = 12345678;
        buf = new byte[5];
        offset = 0;
        length = 5;
        try {
            result = TarUtils.formatOctalBytes(value, buf, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        // Typical use case
        long value = 1234;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = new byte[] {'0', '0', '0', '0', '0', '0', '0', '1', '2', ' '}; // ASCII representation of 1234
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, result);

        // Error scenario - value will not fit in the buffer
        value = 12345678;
        buf = new byte[5];
        offset = 0;
        length = 5;
        try {
            result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // Typical use case
        long value = 1234;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        byte[] expectedResult = new byte[] {'0', '0', '0', '0', '0', '0', '0', '1', '2', ' '}; // ASCII representation of 1234
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertArrayEquals(expectedResult, buf);
        assertEquals(offset + length, result);

        // Error scenario - value will not fit in the buffer
        value = 12345678;
        buf = new byte[5];
        offset = 0;
        length = 5;
        try {
            result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testComputeCheckSum() {
        // Typical use case
        byte[] buf = new byte[] {1, 2, 3, 4};
        long expectedResult = 10;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expectedResult, result);
    }
}