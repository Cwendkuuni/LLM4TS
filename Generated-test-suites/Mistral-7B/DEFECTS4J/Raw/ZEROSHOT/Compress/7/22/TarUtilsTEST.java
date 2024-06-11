import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, 5);
        assertEquals(017750L, result);

        byte[] invalidBuffer = new byte[]{'0', '8'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 2);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'p', 'a', 'c', 'h', 'e', ' ', 'T', 'a', 'r', '.', 'z', 'i', 'p'};
        String result = TarUtils.parseName(buffer, 0, 13);
        assertEquals("apache Tar.zip", result);

        byte[] invalidBuffer = new byte[]{'a', 'p', 'a', 'c', 'h', 'e', ' ', 'T', 'a', 'r', '.'};
        try {
            TarUtils.parseName(invalidBuffer, 0, 12);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "apache Tar.zip";
        byte[] buffer = new byte[13];
        int result = TarUtils.formatNameBytes(name, buffer, 0, 13);
        assertArrayEquals(new byte[]{'a', 'p', 'a', 'c', 'h', 'e', ' ', 'T', 'a', 'r', '.', 'z', 'i', 'p'}, buffer);
        assertEquals(13, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '0', '0', '7', '5', '3', ' '}, buffer);

        long value2 = 1;
        TarUtils.formatUnsignedOctalString(value2, buffer, 0, 1);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, 12);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '0', '0', '7', '5', '3', ' ', ' '}, buffer);
        assertEquals(12, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[11];
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, 11);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '0', '0', '7', '5', '3', ' '}, buffer);
        assertEquals(11, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[13];
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 13);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'}, buffer);
        assertEquals(13, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'p', 'a', 'c', 'h', 'e', ' ', 'T', 'a', 'r', '.', 'z', 'i', 'p'};
        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }
        assertEquals(checksum, computedChecksum);
    }
}