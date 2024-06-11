import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {'0', '1', '2', '3', '4', '5', '6', '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);

        byte[] invalidBuffer = new byte[] {'A', 'B', 'C', 'D'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("hello world", result);

        byte[] emptyBuffer = new byte[0];
        String resultEmpty = TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length);
        assertEquals("", resultEmpty);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello world";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        for (int i = 0; i < name.length(); ++i) {
            assertEquals((byte) name.charAt(i), buffer[i]);
        }

        for (int i = name.length(); i < offset; ++i) {
            assertEquals((byte) 0, buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        assertEquals(7564212, value);

        for (int i = 0; i < offset; ++i) {
            assertEquals((byte) '0', buffer[i]);
        }

        assertEquals((byte) '7', buffer[offset]);
        assertEquals((byte) '5', buffer[offset+1]);
        assertEquals((byte) '6', buffer[offset+2]);
        assertEquals((byte) '4', buffer[offset+3]);
        assertEquals((byte) '2', buffer[offset+4]);
        assertEquals((byte) '1', buffer[offset+5]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        assertEquals(7564212, value);

        for (int i = 0; i < offset-2; ++i) {
            assertEquals((byte) '0', buffer[i]);
        }

        assertEquals((byte) '7', buffer[offset-2]);
        assertEquals((byte) '5', buffer[offset-1]);
        assertEquals((byte) ' ', buffer[offset]);
        assertEquals((byte) '0', buffer[offset+1]);

        for (int i = offset+2; i < buffer.length; ++i) {
            assertEquals((byte) 0, buffer[i]);
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        long checksum = TarUtils.computeChecksum(buffer);
        long expectedChecksum = 6751044854227081302L;
        assertEquals(expectedChecksum, checksum);
    }
}