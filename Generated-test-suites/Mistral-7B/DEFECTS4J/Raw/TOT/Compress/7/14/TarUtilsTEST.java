import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] { (byte) '7', (byte) '5', (byte) '4', 0 };
        long expected = 01754;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[] { (byte) 'X', (byte) '5', (byte) '4', 0 };
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] { (byte) 'm', (byte) 'y', (byte) 'file', 0 };
        String expected = "myfile";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myfile";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 9;
        int expected = TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] actual = new byte[10];
        System.arraycopy(buffer, 0, actual, 0, expected);
        byte[] expectedBytes = new byte[] { (byte) 'm', (byte) 'y', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', 0, 0, 0 };
        assertArrayEquals(expectedBytes, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[9];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = new byte[] { (byte) '5', (byte) '4', (byte) '3', (byte) '4', (byte) '5', (byte) ' ', (byte) '0', 0 };
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 9;
        int actual = TarUtils.formatOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[] { (byte) '0', (byte) '5', (byte) '4', (byte) '3', (byte) '4', (byte) '5', (byte) ' ', 0, 0 };
        assertArrayEquals(expected, buffer);
        assertEquals(offset + length, actual);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[] { 75, 100, 120, 125, 98, 0, 0, 0 };
        long checksum = TarUtils.computeChecksum(buf);
        long computedChecksum = 75L + 100L + 120L + 125L + 98L + (75 & 0xFF) + (100 & 0xFF) + (120 & 0xFF) + (125 & 0xFF) + (98 & 0xFF);
        assertEquals(checksum, computedChecksum);
    }
}