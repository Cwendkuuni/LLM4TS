import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This class provides JUnit tests for the TarUtils class.
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', ' ', '7', '7', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01755077550L, result);

        byte[] bufferInvalid = new byte[]{'0', '7', '5', '5', '0', 'X'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', ' ', 'a', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("hello world.archiv.gz", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello world.archiv.gz";
        byte[] buffer = new byte[20];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(20, result);
        assertEquals("hello world.archiv.gz", new String(buffer, 0, 20));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "012345";
        assertEquals(expected, new String(buffer, 0, 6));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[11];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "012345   ";
        assertEquals(expected, new String(buffer, 0, result));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567890123";
        assertEquals(expected, new String(buffer, 0, result));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567890123   ";
        assertEquals(expected, new String(buffer, 0, result));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', ' ', 'a', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        long checksum = TarUtils.computeChecksum(buf);
        assertEquals(0x2f081a0d91b6f732L, checksum);
    }
}