import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7'};
        long expected = 01777L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidOctalDigit() {
        byte[] buffer = new byte[]{(byte) '0', (byte) 'A', (byte) '7', (byte) '7'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) ' ',(byte) 'T', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        String expected = "my Tar.gzip";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my Tar.gzip";
        byte[] buffer = new byte[23];
        int expected = formatNameBytes(name, buffer, 0, buffer.length);
        int actual = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(expected, actual);
        byte[] result = new byte[expected];
        System.arraycopy(buffer, 0, result, 0, expected);
        String actualResult = new String(result);
        assertEquals(name, actualResult);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[11];
        int length = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);
        String actualResult = new String(result);
        String expectedResult = "012345";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[13];
        int length = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);
        String actualResult = new String(result, 0, 11);
        String expectedResult = "012345 ";
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[13];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o'};
        long expected = 82467L;
        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSumEmptyBuffer() {
        byte[] buf = new byte[0];
        long expected = 0L;
        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected, actual);
    }
}