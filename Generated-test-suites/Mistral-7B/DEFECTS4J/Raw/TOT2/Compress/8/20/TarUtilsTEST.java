import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {
    // Test for parseOctal method
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '3', (byte) '1'};
        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(17553, result);

        byte[] invalidBuffer = new byte[]{(byte) '8'}; // invalid byte
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for parseName method
    @Test
    public void testParseName() {
        byte[] nameBuffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'ip', (byte) '.', (byte) 'p', (byte) 'l', (byte) ' ', (byte) 'm', (byte) 'y', (byte) '.', (byte) 'd', (byte) 'a', (byte) 't', (byte) ' ', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'm', (byte) ' ', (byte) 's', (byte) 'u', (byte) 'm', (byte) ' ', (byte) 'o', (byte) 'f', (byte) 'f', (byte) ' ', (byte) 's', (byte) 'e', (byte) 't', (byte) ' ', (byte) 'T', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) ' ', (byte) 'v', (byte) '1', (byte) '.', (byte) 'd', (byte) 'a', (byte) 't', (byte) ' ', (byte) 'a', (byte) 's', (byte) 's', (byte) 'e', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) ' ', (byte) 'v', (byte) '2'};
        String resultName = TarUtils.parseName(nameBuffer, 0, nameBuffer.length);
        assertEquals("tar.gz.my.data.checksums.sum.Tar.gz.v1.data.asset.Tar.gz.v2", resultName);

        byte[] emptyBuffer = new byte[0]; // buffer with no data
        String emptyResultName = TarUtils.parseName(emptyBuffer, 0, 0);
        assertEquals("", emptyResultName);
    }

    // Test for formatNameBytes method
    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        // Check the content of the buffer
        for (int i = 0; i < buffer.length; i++) {
            if (i < name.length()) {
                assertEquals((byte) name.charAt(i), buffer[i]);
            } else {
                assertEquals(0, buffer[i]);
            }
        }
    }

    // Test for formatUnsignedOctalString method
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0125111";
        assertEquals(expected, new String(buffer));
    }

    // Test for formatOctalBytes method
    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(11, offset);
        // Check the content of the buffer
        String expected = "0125111  ";
        assertEquals(expected, new String(buffer, 0, offset));
    }

    // Test for computeCheckSum method
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0x6d, (byte) 0xef, (byte) 0x0b, (byte) 0x76};
        long checksum = TarUtils.computeCheckSum(buffer);
        long expectedChecksum = 0xbf17d1;
        assertEquals(expectedChecksum, checksum);
    }
}