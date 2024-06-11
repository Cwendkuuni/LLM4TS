import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '5', '5', '7', '7', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01755775577L, result);

        byte[] invalidBuffer = new byte[]{'0', '7', '5', '5', '7', '8', '5', '5', '7', '7', '5', '5', '0'}; // Invalid byte
        try {
            result = TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        byte[] emptyBuffer = new byte[0];
        try {
            result = TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{0x0, (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'u', (byte) 'm', (byte) '.', (byte) 'o', (byte) 'b', (byte) 'j', (byte) ' ', 0};
        String name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.a.checksum.obj ", name);

        byte[] emptyBuffer = new byte[0];
        try {
            name = TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        byte[] invalidBuffer = new byte[]{0x1, (byte) 'a', 0}; // Trailing NUL missing
        try {
            name = TarUtils.parseName(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.a.checksum.obj";
        byte[] buffer = new byte[16];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("tar.a.checksum.obj\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0 ", new String(buffer, 0, offset));

        name = "tar.a.checksum.obj";
        offset = TarUtils.formatNameBytes(name, buffer, 14, buffer.length);
        assertEquals("tar.a.checksum.obj" + new String(new byte[12], 0), new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("01234567543", new String(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("01234567543 \0 ", new String(buffer, 0, offset));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x0, (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'u', (byte) 'm', (byte) '.', (byte) 'o', (byte) 'b', (byte) 'j'};
        long checksum = TarUtils.computeChecksum(buffer);
        long expected = 0x270157321220132;
        assertEquals(expected, checksum);
    }
}