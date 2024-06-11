import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '0'};
        int offset = 0;
        int length = buffer.length;
        long expected = 017550L;
        long actual = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) 'A', (byte) '5', (byte) '5', (byte) '0'};
        int offset = 0;
        int length = buffer.length;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) ' ', (byte) 'T', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'ip'};
        int offset = 0;
        int length = buffer.length;
        String expected = "my Tar.gzip";
        String actual = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myTar.gzip";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] expected = new byte[]{(byte) 'm', (byte) 'y', (byte) 'T', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        assertArrayEquals(expected, buffer, 0, buffer.length);
        assertEquals(actual, offset + length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 255;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '3', '7', '7', '7', '7', '7', '7', '7', '7'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 255;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int actual = TarUtils.formatOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '3', '7', '7', '7', '7', '7', '7', '7', '7', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
        assertEquals(actual, offset + length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 65535;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int actual = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '17', '7', '7', '7', '7', '7', '7', '7', '7'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
        assertEquals(actual, offset + length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 32767;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int actual = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '16', '3', '7', '7', '7', '7', '7', '7', '6'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
        assertEquals(actual, offset + length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55};
        long expected = 0x1122334455L;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}