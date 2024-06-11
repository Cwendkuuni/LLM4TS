import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017777L, result);

        byte[] invalidBuffer = new byte[]{(byte) '0', (byte) '8', (byte) '7', (byte) '7', (byte) '7', (byte) 0};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', (byte) 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.file", result);

        byte[] invalidBuffer = new byte[]{'t', 'a', (byte) 255, 'a', '.', 't', 'a', 'r', (byte) 0};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(invalidBuffer, 0, invalidBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.file";
        byte[] buffer = new byte[20];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(name.length(), result);
        for (int i = 0; i < result; ++i) {
            assertEquals((byte) name.charAt(i), buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int result = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(10, result);
        for (int i = 0; i < result; ++i) {
            assertEquals(i < 3 ? (byte) '0' + (byte) (value >>> 3 * i & 7) : (byte) 0, buffer[i]);
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, result);
        for (int i = 0; i < result; ++i) {
            if (i < 8) {
                assertEquals(i < 3 ? (byte) '0' + (byte) (value >>> 3 * i & 7) : (byte) ' ', buffer[i]);
            } else {
                assertEquals((byte) '0', buffer[i]);
            }
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x42,
                (byte) 0x61, (byte) 0x63, (byte) 0x68, (byte) 0x00};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expectedChecksum = 0x612981A9C3B4B264L;
        assertEquals(expectedChecksum, checksum);
    }
}