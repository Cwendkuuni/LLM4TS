import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '0'};
        long expectedResult = 017555555555555L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidLength() {
        byte[] buffer = new byte[]{'0', '7', '5'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithTrailingInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '0'};
        long expectedResult1 = 017555555555555L;
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(expectedResult1, result1);

        byte[] buffer2 = new byte[]{0x81, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        long expectedResult2 = 0x81010000000000L;
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(expectedResult2, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryWithInvalidLength() {
        byte[] buffer = new byte[]{0x81};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryWithTrailingInvalidByte() {
        byte[] buffer = new byte[]{0x81, 0xFF};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryWithBinaryNumberExceedsMaxSignedLongValue() {
        byte[] buffer = new byte[]{0x81, 0x80, 0x80, 0x80};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{0x01};
        boolean expectedResult1 = true;
        boolean result1 = TarUtils.parseBoolean(buffer1, 0);
        assertEquals(expectedResult1, result1);

        byte[] buffer2 = new byte[]{0x00};
        boolean expectedResult2 = false;
        boolean result2 = TarUtils.parseBoolean(buffer2, 0);
        assertEquals(expectedResult2, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanWithInvalidByte() {
        byte[] buffer = new byte[]{0x02};
        TarUtils.parseBoolean(buffer, 0);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String expectedResult = "tar.file.gzip";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "archive.tar.gz";
        byte[] buffer = new byte[TarConstants.NAME_LENGTH + TarConstants.MAGIC_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{0x75, 0x74, 0x61, 0x74, 0x69, 0x6c, 0x65, 0x2e, 0x74, 0x61, 0x74, 0x74, 0x72, 0x65, 0x73, 0x74, 0x61, 0x72, 0x79};
        assertArrayEquals(expectedBuffer, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 017555555555555L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{0x31, 0x37, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x00, 0x20, 0x00};
        assertArrayEquals(expectedBuffer, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 017555555555555L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 1];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{0x31, 0x37, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x20, 0x00};
        assertArrayEquals(expectedBuffer, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 017555555555555555555L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 1];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{0x31, 0x37, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x35, 0x20, 0x00};
        assertArrayEquals(expectedBuffer, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 12345678901234567890L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 1];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x1, 0x2, 0x0, 0x0, 0x0, 0x3, 0x2, 0x0, 0x0, 0x0, 0x0, 0x0, 0x20, 0x00};
        assertArrayEquals(expectedBuffer, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x75, 0x74, 0x61, 0x74, 0x69, 0x6c, 0x65, 0x2e, 0x74, 0x61, 0x74, 0x74, 0x72, 0x65, 0x73, 0x74, 0x61, 0x72, 0x79, 0x00};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expectedChecksum = 3417083099425871399L;
        assertEquals(expectedChecksum, checksum);
    }
}