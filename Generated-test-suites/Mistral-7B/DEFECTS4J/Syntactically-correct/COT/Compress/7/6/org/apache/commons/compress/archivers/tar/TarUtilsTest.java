import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidOctalString() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4'};
        int offset = 0;
        int length = 5;
        long expected = 01234L;
        long actual = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidOctalString() {
        byte[] buffer = new byte[]{'0', '1', '2', 'X', '4'};
        int offset = 0;
        int length = 5;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_ValidEntryName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'i', 'm', 'a', 'g', 'e', '.', 't', 'x', 't', 'x'};
        int offset = 0;
        int length = 13;
        String expected = "tar.image.txt";
        String actual = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseName_EmptyEntryName() {
        byte[] buffer = new byte[]{' '};
        int offset = 0;
        int length = 1;
        String actual = TarUtils.parseName(buffer, offset, length);
        assertEquals("", actual);
    }

    @Test
    public void testFormatNameBytes_ValidEntryName() {
        String name = "tar.image.txt";
        byte[] buf = new byte[13];
        int offset = 0;
        int length = 13;
        int expected = TarUtils.formatNameBytes(name, buf, offset, length);
        int actual = TarUtils.formatNameBytes(name, buf, offset, length);
        assertArrayEquals(buf, expected);
        assertEquals(expected, actual);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "tar.image.txt.log";
        byte[] buf = new byte[13];
        int offset = 0;
        int length = 13;
        TarUtils.formatNameBytes(name, buf, offset, length);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidOctalNumber() {
        long value = 12345L;
        byte[] buffer = new byte[7];
        int offset = 0;
        int length = 7;
        int expected = TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] actual = new byte[7];
        TarUtils.formatUnsignedOctalString(value, actual, offset, length);
        assertArrayEquals(buffer, actual);
        assertEquals(expected, offset + length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueLongerThanBuffer() {
        long value = 1234567890L;
        byte[] buffer = new byte[6];
        int offset = 0;
        int length = 6;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_ValidOctalNumber() {
        long value = 12345L;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = TarUtils.formatOctalBytes(value, buf, offset, length);
        int actual = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_ValueLongerThanBuffer() {
        long value = 1234567890L;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testComputeCheckSum_ValidBuffer() {
        byte[] buf = new byte[]{'t', 'a', 'r', ' ', 'i', 'm', 'a', 'g', 'e', '.'};
        long expected = 0x54686174616d696e;
        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected, actual);
    }
}