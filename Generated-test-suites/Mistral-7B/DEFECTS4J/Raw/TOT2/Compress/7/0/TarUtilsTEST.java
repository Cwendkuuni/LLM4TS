import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatCheckSumOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(017555L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '8'}; // '8' is invalid octal digit
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("tar.object", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "mytarfile.tar";
        byte[] buffer = new byte[256];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(32, offset); // length of "mytarfile.tar" is 9+1+10 = 19, padded with trailing NULs
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        // the output should be "012345" followed by trailing NULs
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '0'}, buffer, 0, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithValueTooLarge() {
        long value = 123456789012345L;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        // the output should be "012345 " followed by trailing NULs
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0'}, buffer, 0, 7);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        // the output should be "0123456789012345 " followed by trailing NULs
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '0'}, buffer, 0, 12);
    }

    @Test
    public void testComputeCheckSum() {
        String hex = "0123456789ABCDEF";
        byte[] buffer = new byte[17];
        for (int i = 0; i < hex.length(); ++i) {
            int b = Integer.parseInt(String.valueOf(hex.charAt(i)), 16);
            buffer[i] = (byte) b;
        }
        long checksum = TarUtils.computeCheckSum(buffer);
        long expectedChecksum = 0xedb88320L;
        Assert.assertEquals
                (expectedChecksum, checksum);
    }
}