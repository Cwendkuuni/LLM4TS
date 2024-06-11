import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '0', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0'};
        long expected = 01111111111111111111111111111L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '0', '1', '2', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailer() {
        byte[] buffer = new byte[]{'0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'};
        long expected1 = 0b111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111;
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(expected1, result1);

        byte[] buffer2 = new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'};
        long expected2 = 0b00011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111;
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(expected2, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidBinaryNumber() {
        byte[] buffer = new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{'0'};
        boolean expected1 = false;
        boolean result1 = TarUtils.parseBoolean(buffer1, 0);
        assertEquals(expected1, result1);

        byte[] buffer2 = new byte[]{'1'};
        boolean expected2 = true;
        boolean result2 = TarUtils.parseBoolean(buffer2, 0);
        assertEquals(expected2, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalidByte() {
        byte[] buffer = new byte[]{'2'};
        TarUtils.parseBoolean(buffer, 0);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String expected = "tar.file.gzip";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.file.gzip";
        byte[] buffer = new byte[32];
        int offset = 0;
        int length = 13;
        offset = TarUtils.formatNameBytes(name, buffer, offset, length);
        String result = new String(buffer, 0, offset);
        assertEquals(name.substring(0, length), result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 3337373575115L;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0777751157511";
        String result = new String(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 3337373575115L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0777751157511  ";
        String result = new String(buffer, 0, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 3337373575115L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "07777511575";
        String result = new String(buffer, 0, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = -123456789123456789123456789123456789123456789123456789123456789123456789L;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String expected = "0o777775155220720267";
        String result = new String(buffer, 0, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expected = 0x964322B5F14872C3L;
        assertEquals(expected, checksum);
    }

}