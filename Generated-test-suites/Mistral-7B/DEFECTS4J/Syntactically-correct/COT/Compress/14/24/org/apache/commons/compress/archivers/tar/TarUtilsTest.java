import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long result = TarUtils.parseOctal(buffer, 0, 9);
        assertEquals(01234567L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '8'};
        TarUtils.parseOctal(buffer, 0, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidLength() {
        byte[] buffer = new byte[]{'0'};
        TarUtils.parseOctal(buffer, 0, 0);
    }

    @Test
    public void testParseOctalAllNUL() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, 0);
        assertEquals(0L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingNUL() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        TarUtils.parseOctal(buffer, 0, 8);
    }

    @Test
    public void testParseOctalLeadingNUL() {
        byte[] buffer = new byte[]{'0', '\u0000', '1', '2', '3', '4', '5', '6', '7'};
        long result = TarUtils.parseOctal(buffer, 0, 10);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, 8);
        assertEquals(01234567L, result1);

        byte[] buffer2 = new byte[]{'0', '\u0080', '1', '2', '3', '4', '5', '6', '7'};
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, 9);
        assertEquals(194512167L, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidLength() {
        byte[] buffer = new byte[0];
        TarUtils.parseOctalOrBinary(buffer, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '8'};
        TarUtils.parseOctalOrBinary(buffer, 0, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryBinaryNumberExceedsMaximumSignedLongValue() {
        byte[] buffer = new byte[]{'0', '\u0080', '0'};
        TarUtils.parseOctalOrBinary(buffer, 0, 3);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{1};
        boolean result1 = TarUtils.parseBoolean(buffer1, 0);
        assertTrue(result1);

        byte[] buffer2 = new byte[]{0};
        boolean result2 = TarUtils.parseBoolean(buffer2, 0);
        assertFalse(result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalidByte() {
        byte[] buffer = new byte[]{2};
        TarUtils.parseBoolean(buffer, 0);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar file", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar file";
        byte[] buffer = new byte[TarConstants.NAME_LENGTH];
        int offset = 0;
        offset = TarUtils.formatNameBytes(name, buffer, offset, buffer.length);
        byte[] expected = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', 0};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
        byte[] expected = new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7'};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[11];
        int offset = 0;
        offset = TarUtils.formatOctalBytes(value, buffer, offset, 11);
        byte[] expected = new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7', ' '};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[9];
        int offset = 0;
        offset = TarUtils.formatLongOctalBytes(value, buffer, offset, 9);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' '};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1234567L;
        byte[] buffer = new byte[10];
        int offset = 0;
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, offset, 10);
        byte[] expected = new byte[]{'0', '\u0080', '1', '2', '3', '4', '5', '6', '7'};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[11];
        int offset = 0;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, 11);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', 0x76, 0};
        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = 0x76816d91f67b54b1L;
        assertEquals(checksum, computedChecksum);
    }

}