import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017540L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', 'x', '4', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', '.', 't', 'x', 't'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.txt", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.gz";
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'t', 'a', 'r', '.', 'g', 'z', 0, 0, 0, 0, 0}, buffer, 0, 11);
        assertEquals(11, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '1', '4', '5', '0', '0', '0', '0'}, buffer, 0, 10);
        assertEquals(10, offset);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '1', '4', '5', ' ', '0', '0', '0', '0', '0'}, buffer, 0, 12);
        assertEquals(12, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', '.', 't', 'x', 't'};
        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }
        assertEquals(checksum, computedChecksum);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0'}, buffer, 0, 13);
        assertEquals(13, offset);
    }
}