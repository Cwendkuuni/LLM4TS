import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0'};
        long expectedResult = 017540;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'1', '2', '3'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r'};
        String expectedResult = "tar.tar";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myfile.txt";
        byte[] buf = new byte[256];
        int length = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        byte[] expectedResult = new byte[]{'m', 'y', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        assertArrayEquals(expectedResult, buf, 0, length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedResult = new byte[]{'0', '1', '1', '7', '5', '1', '5', '4', '1', '7', '5'};
        assertArrayEquals(expectedResult, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int result = TarUtils.formatOctalBytes(value, buf, 0, buf.length);
        byte[] expectedResult = new byte[]{'0', '1', '1', '7', '5', '1', '5', '4', '1', '7'};
        assertArrayEquals(expectedResult, buf, 0, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'h', 'e', 'a', 'd', 'e', 'r'};
        long expectedResult = 1317693230;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[9];
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);
        byte[] expectedResult = new byte[]{'0', '1', '1', '3', '3', '3', '1', '5', '0'};
        assertArrayEquals(expectedResult, buf, 0, result);
    }
}