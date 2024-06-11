import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0'};
        long expectedResult = 01777770L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', (byte) 0x80};
        TarUtils.parseOctal(buffer, 0, buffer.length); // This line should throw an exception
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', ' ', 0};
        String expectedResult = "abcd e";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "filename.ext";
        byte[] buf = new byte[20];
        int offset = 0;
        int length = name.length();
        TarUtils.formatNameBytes(name, buf, offset, length);
        byte[] expectedResult = new byte[]{0, 'f', 'i', 'l', 'e', 'n', 'a', 'm', '.', 'e', 'x', 't', 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedResult, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedResult = new byte[]{0, '5', '4', '2', '3', ' ', '0', '7', '5', '4'};
        assertArrayEquals(expectedResult, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length); // This line should throw an exception
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[12];
        TarUtils.formatOctalBytes(value, buf, 0, buf.length);
        byte[] expectedResult = new byte[]{0, '5', '4', '2', '3', ' ', '0', '7', '5', '4', ' ', ' '};
        assertArrayEquals(expectedResult, buf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesWithValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buf = new byte[12];
        TarUtils.formatOctalBytes(value, buf, 0, buf.length); // This line should throw an exception
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[13];
        TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);
        byte[] expectedResult = new byte[]{0, '5', '4', '2', '3', ' ', '0', '7', '5', '4', ' ', ' '};
        byte[] trailingNullAndSpace = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ' ', ' '};
        assertArrayEquals(expectedResult, buf, 0, 11); // Compare the first 11 bytes with the expected result
        assertArrayEquals(trailingNullAndSpace, buf, 11); // Compare the last 2 bytes with the trailing null and space
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesWithValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buf = new byte[13];
        TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length); // This line should throw an exception
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        long expectedResult = 0x095C7203020100L;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expectedResult, result);
    }
}