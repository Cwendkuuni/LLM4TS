import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '0', '0'};
        long expected = 0177500L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '8', '0'}; // invalid byte '8'
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '.', 't', 'a', 'r'};
        String expected = "hello world.tar";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello world.tar";
        byte[] buf = new byte[32];
        int offset = 0;
        int length = name.length();
        offset = TarUtils.formatNameBytes(name, buf, offset, length);
        byte[] expectedBytes = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '.', 't', 'a', 'r', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedBytes, buf, 0, buf.length);
        assertEquals(offset + length, 32);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedBytes = new byte[]{'0', '1', '0', '1', '0', '1', '4', '5', '6', '7', '8', '9'};
        assertArrayEquals(expectedBytes, buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[12];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatOctalBytes(value, buf, offset, length);
        byte[] expectedBytes = new byte[]{'0', '1', '0', '1', '0', '1', '4', '5', '6', '7', '8', ' ', ' ', '0'};
        assertArrayEquals(expectedBytes, buf, 0, buf.length);
        assertEquals(offset + length, buf.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890123L;
        byte[] buf = new byte[13];
        int offset = 0;
        int length = 11;
        offset = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        byte[] expectedBytes = new byte[]{'0', '1', '1', '1', '1', '4', '5', '6', '7', '8', '9', '0', '1'};
        assertArrayEquals(expectedBytes, buf, 0, buf.length);
        assertEquals(offset + length, buf.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890123L;
        byte[] buf = new byte[13];
        int offset = 0;
        int length = 11;
        offset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        byte[] expectedBytes = new byte[]{'0', '1', '1', '1', '1', '4', '5', '6', '7', '8', '9', '0', ' '};
        assertArrayEquals(expectedBytes, buf, 0, buf.length);
        assertEquals(offset + length, buf.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
        long expected = 0x095127F388C6;
        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected, actual);
    }

}