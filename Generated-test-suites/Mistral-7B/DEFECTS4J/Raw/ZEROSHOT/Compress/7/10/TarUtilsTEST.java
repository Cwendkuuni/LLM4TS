import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', ' ', '4', ' ', '1', ' ', '2', ' ', '7', '2'};
        long expected = 017550412;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'A', 'B', 'C'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'.', 'm', 'y', ' ', 'A', 'p', 'a', 'c', 'h', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String expected = "my.Apache.tar.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my.Apache.tar.gz";
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'m', 'y', '.', 'A', 'p', 'a', 'c', 'h', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 0}, buffer);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '0', ' ', ' ', '.'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '0', ' ', ' ', '012345'}, buffer);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', ' ', 'C', 'h', 'e', 'c', 'k', 's', 'u', 'm'};
        long expected = 2609713136L;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}