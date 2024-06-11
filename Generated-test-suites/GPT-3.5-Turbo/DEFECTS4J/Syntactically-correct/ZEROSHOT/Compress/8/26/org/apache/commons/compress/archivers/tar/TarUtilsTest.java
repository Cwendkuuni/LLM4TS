import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{65, 66, 67, 68, 69, 70};
        int offset = 0;
        int length = 6;
        long expected = 17320;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctalWithMissingTrailingSpace() {
        byte[] buffer = new byte[]{65, 66, 67, 68, 69, 70};
        int offset = 0;
        int length = 5;
        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 65 at offset 0 in 'ABCDE F' len=5", e.getMessage());
        }
    }

    @Test
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[]{65, 66, 67, 68, 69, 71};
        int offset = 0;
        int length = 6;
        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 71 at offset 5 in 'ABCDE FG' len=6", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{65, 66, 67, 68, 69, 70, 0};
        int offset = 0;
        int length = 7;
        String expected = "ABCDEF";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseNameWithTrailingNull() {
        byte[] buffer = new byte[]{65, 66, 67, 68, 69, 70, 0, 71, 72, 73};
        int offset = 0;
        int length = 7;
        String expected = "ABCDEF";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "ABC";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int expected = 6;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, result);
        assertArrayEquals(new byte[]{65, 66, 67, 0, 0, 0}, buf);
    }

    @Test
    public void testFormatNameBytesWithLongName() {
        String name = "ABCDEF";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int expected = 6;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, result);
        assertArrayEquals(new byte[]{65, 66, 67, 68, 69, 70}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{48, 60, 62, 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int expected = 6;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        assertArrayEquals(new byte[]{48, 60, 62, 32, 0, 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int expected = 6;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        assertArrayEquals(new byte[]{48, 60, 62, 32, 0, 0}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;
        int expected = 6;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(expected, result);
        assertArrayEquals(new byte[]{48, 60, 62, 0, 32, 0}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{65, 66, 67, 68, 69, 70};
        long expected = 432;
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(expected, result);
    }
}