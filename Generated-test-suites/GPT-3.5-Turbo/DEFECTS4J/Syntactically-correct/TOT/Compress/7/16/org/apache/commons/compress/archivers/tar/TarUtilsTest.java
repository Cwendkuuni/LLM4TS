import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal_typicalCase() {
        byte[] buffer = new byte[]{49, 48, 48}; // ASCII representation of "100"
        long expected = 100;

        long result = TarUtils.parseOctal(buffer, 0, buffer.length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_emptyBuffer() {
        byte[] buffer = new byte[0];
        long expected = 0;

        long result = TarUtils.parseOctal(buffer, 0, buffer.length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_trailingSpace() {
        byte[] buffer = new byte[]{49, 48, 48, 32}; // ASCII representation of "100 "
        long expected = 100;

        long result = TarUtils.parseOctal(buffer, 0, buffer.length);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidDigit() {
        byte[] buffer = new byte[]{49, 50, 103}; // ASCII representation of "12g"
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_typicalCase() {
        byte[] buffer = "example.txt\0".getBytes();
        String expected = "example.txt";

        String result = TarUtils.parseName(buffer, 0, buffer.length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseName_emptyBuffer() {
        byte[] buffer = new byte[0];
        String expected = "";

        String result = TarUtils.parseName(buffer, 0, buffer.length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseName_noTrailingNull() {
        byte[] buffer = "example.txt".getBytes();
        String expected = "example.txt";

        String result = TarUtils.parseName(buffer, 0, buffer.length);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_typicalCase() {
        String name = "example.txt";
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;
        byte[] expected = "example.txt\0".getBytes();

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatNameBytes_longerName() {
        String name = "example.txt";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        byte[] expected = "exam\0".getBytes();

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatUnsignedOctalString_typicalCase() {
        long value = 12345;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        byte[] expected = "030071\0".getBytes();

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_bufferTooSmall() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_typicalCase() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        byte[] expected = "030071 \0".getBytes();

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_bufferTooSmall() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes_typicalCase() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        byte[] expected = "030071 \0".getBytes();

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_bufferTooSmall() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_typicalCase() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        byte[] expected = "030071 \0".getBytes();

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_bufferTooSmall() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testComputeCheckSum_typicalCase() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long expected = 15;

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum_emptyBuffer() {
        byte[] buffer = new byte[0];
        long expected = 0;

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(expected, result);
    }

}