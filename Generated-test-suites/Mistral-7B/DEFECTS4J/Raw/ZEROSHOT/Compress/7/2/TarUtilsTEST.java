import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5'};
        int offset = 0;
        int length = 4;

        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(017555L, result);

        buffer = new byte[]{'0', '1', '2'};
        offset = 0;
        length = 3;

        try {
            result = TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0', '1', '2', '3'};
        offset = 0;
        length = 4;

        try {
            result = TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 't', 'e', 's', 't', '.', 'c', 'o', 'm', 'p', 'r', 'e', 's', 's', 'o', 'n', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
        int offset = 0;
        int length = buffer.length;

        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("test.compress.tar.gz", result);

        buffer = new byte[]{'t', 'a', 'r', '.', 't', 'e', 's', 't', 0};
        offset = 0;
        length = buffer.length;

        result = TarUtils.parseName(buffer, offset, length);
        assertEquals("test.tar", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test.compress.tar.gz";
        byte[] buffer = new byte[256];
        int offset = 0;
        int length = name.length();

        offset = TarUtils.formatNameBytes(name, buffer, offset, length);

        for (int i = 0; i < length; ++i) {
            assertEquals(name.charAt(i), (char) buffer[i + offset]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 10;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals("012345", new String(buffer, offset, length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 11;

        TarUtils.formatOctalBytes(value, buffer, offset, length);

        assertEquals("012345 ", new String(buffer, offset, length));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 't', 'e', 's', 't', '.', 'c', 'o', 'm', 'p', 'r', 'e', 's', 's', 'o', 'n', '.', 't', 'a', 'r', '.', 'g', 'z'};

        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }

        assertEquals(checksum, computedChecksum);
    }
}