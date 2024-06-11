import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0'};
        long expected = 017540l;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidDigit() {
        byte[] buffer = new byte[]{'0', '7', '5', 'X', '4', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', '.', 'd', '.', 'e', '.', 'f', 0};
        String expected = "ab.c.d.e.f.";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "ab.c.d.e.f.";
        byte[] buffer = new byte[name.length() + 1];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        for (int i = 0; i < name.length(); ++i) {
            assertEquals(name.charAt(i), (char) buffer[i]);
        }
        assertEquals(0, buffer[name.length()]);
        assertEquals(result, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("01234", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234;
        byte[] buffer = new byte[10];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("01234  ", new String(buffer, 0, result));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[11];
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("012345675", new String(buffer, 0, result));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("012345675  ", new String(buffer, 0, result));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x78, (byte) 0x9a, (byte) 0xba, (byte) 0xbe};
        long expected = (0x78 & 0xFF) << 24 |
                ((0x9a & 0xFF) << 16) |
                ((0xba & 0xFF) << 8 ) |
                (0xbe & 0xFF);
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}