import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '8'}; // Invalid byte 8
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'.' , 'h', 'e', 'l', 'l', 'o', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "testName";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{
                (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 'N', (byte) 'a', (byte) 'm', (byte) 'e',
                (byte) 0, (byte) 0
        };
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{
                (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
                (byte) ' ', (byte) 0
        };
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{
                (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
                (byte) ' ', (byte) 0, 0, 0
        };
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{1, 2, 3, 4};
        long checkSum = TarUtils.computeCheckSum(buffer);
        long expected = 1027; // Manually computed checksum
        assertEquals(expected, checkSum);
    }
}