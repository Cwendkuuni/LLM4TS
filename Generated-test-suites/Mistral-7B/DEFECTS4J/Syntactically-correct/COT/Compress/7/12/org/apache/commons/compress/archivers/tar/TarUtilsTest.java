import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '5', (byte) '5'};
        long expectedResult = 017755L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '8', (byte) '5'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{(byte) 'd', (byte) 'i', (byte) 'r', (byte) ' ', (byte) 'm', (byte) 'y', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) 's', (byte) '.'};
        String expectedResult = "dir files.";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseName_TruncatedInput() {
        byte[] buffer = new byte[]{(byte) 'd', (byte) 'i', (byte) 'r'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("dir", result);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "testName";
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expectedResult = new byte[]{(byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 'N', (byte) 'a', (byte) 'm', (byte) 'e', (byte) '\u0000', (byte) '\u0000', (byte) '\u0000'};
        assertArrayEquals(expectedResult, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 12345;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedResult = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '\u0000', (byte) ' ', (byte) '\u0000'};
        assertArrayEquals(expectedResult, buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_TooLongValue() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 12345;
        byte[] buf = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buf, 0, buf.length);
        byte[] expectedResult = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) ' ', (byte) '\u0000'};
        assertArrayEquals(expectedResult, buf, 0, offset);
    }

    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x77, (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x55, (byte) 0x21, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        long checksum = TarUtils.computeChecksum(buffer);
        long expectedChecksum = 0x698c152c341e1f05L;
        assertEquals(expectedChecksum, checksum);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long value = 12345;
        byte[] buf = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);
        byte[] expectedResult = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '\u0000', (byte) ' '};
        assertArrayEquals(expectedResult, buf, 0, offset);
    }
}