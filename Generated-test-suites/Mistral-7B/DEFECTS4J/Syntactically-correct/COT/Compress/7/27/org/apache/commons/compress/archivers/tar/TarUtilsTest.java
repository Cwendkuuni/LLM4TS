import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8', ' '};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.txt", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.gz";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'t', 'a', 'r', '.', 'g', 'z', (byte) 0, 0, 0, 0}, buffer);
        assertEquals(offset, 10);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'}, buffer);
        assertEquals(offset, 12);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        long checksum = TarUtils.computeChecksum(buf);
        long computedChecksum = 0;
        for (int i = 0; i < buf.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & buf[i];
        }
        assertEquals(computedChecksum, checksum);
    }
}