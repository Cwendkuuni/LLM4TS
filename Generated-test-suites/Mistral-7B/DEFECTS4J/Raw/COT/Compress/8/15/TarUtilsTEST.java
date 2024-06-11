import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long expected = 0123456L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput_MissingTrailingNul() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput_InvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', 'A', '4', '5', '6'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C'};
        String expected = "0123456789ABC";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "myfile.txt";
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(12, offset);
        for (int i = 0; i < 11; ++i) {
            assertEquals(name.charAt(i), (char) (buffer[i] & 0xFF));
        }
        assertEquals('\0', buffer[11]);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 123456;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertEquals("012622", new String(buffer, 0, 7));
        assertEquals('\0', buffer[7]);
        assertEquals(' ', buffer[8]);
        assertEquals('\0', buffer[9]);
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 123456;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(11, offset);
        assertEquals("012622 ", new String(buffer, 0, 7));
        assertEquals('\0', buffer[10]);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long value = 123456;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(13, offset);
        assertEquals("01262200 ", new String(buffer, 0, 7));
        assertEquals('\0', buffer[12]);
        assertEquals(' ', buffer[11]);
    }

    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buf = new byte[]{0x01, 0x12, 0x34, 0x56, 0x78, 0x9A, 0xBC, 0xDE, 0xF0};
        long expected = 0x123456789ABCF0L;
        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected, actual);
    }
}