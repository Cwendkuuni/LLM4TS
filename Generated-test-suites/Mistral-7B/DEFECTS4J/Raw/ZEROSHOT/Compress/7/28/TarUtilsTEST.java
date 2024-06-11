import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0', '0'};
        long expected = 0177500;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '8', '0'}; // 8 is an invalid octal digit
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', 0};
        String expected = "tar";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myName";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = name.length();
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] expected = new byte[]{(byte) 'm', (byte) 'y', (byte) 'N', (byte) 'a', (byte) 'm', 0, 0, 0, 0, 0};
        assertArrayEquals(expected, buffer, 0, buffer.length);
        assertEquals(result, offset + length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 7;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 8;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', ' ', ' '};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 9;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 9;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x78, (byte) 0x9a, (byte) 0xba, (byte) 0xbe,
            (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06};
        long expected = 0x9a78babebe01020304L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}