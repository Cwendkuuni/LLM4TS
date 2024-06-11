import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456710L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8'}; // invalid byte 7
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingSpaceOrNul() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'}; // missing trailing space or NUL
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalAllNuls() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalLeadingNul() {
        byte[] buffer = new byte[]{'0'}; // leading NUL
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{0x2A, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x2a01020304050607L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidByte() {
        byte[] buffer = new byte[]{0x2A, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0xF}; // invalid byte F
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidBinaryNumber() {
        byte[] buffer = new byte[]{0x2A, 0x01, 0x02, 0x03, 0x04, 0x05, (byte) 0x80, 0x06, 0x07}; // binary number exceeds maximum signed long value
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalidByte() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'} ;
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.gz", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "mytarfile.tar.gz";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'m', 'y', 't', 'a', 'r', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'}, buffer);
        assertEquals(offset, 10);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0123456710L;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7', '1', '0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0123456710L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7', '1', '0', ' '}, buffer);
        assertEquals(offset, 13);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0123456710L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7', '1'}, buffer);
        assertEquals(offset, 12);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0x2B, 0x21, 0x35, 0x34, 0x36, 0x37, 0x38, 0x39, 0x30, 0x12}, buffer);
        assertEquals(offset, 10);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x123456789012345L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'\u0000', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '}, buffer);
        assertEquals(offset, 13);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', ' ', 'F', 'i', 'l', 'e', '.', 't', 'a', 'r', '.'};
        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = TarUtils.computeChecksum(buffer);
        assertEquals(checksum, computedChecksum);
    }
}