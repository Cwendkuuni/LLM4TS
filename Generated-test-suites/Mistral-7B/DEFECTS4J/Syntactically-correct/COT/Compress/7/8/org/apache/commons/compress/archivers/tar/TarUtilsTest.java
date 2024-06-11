import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidOctalNumber() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) 0};
        long expectedResult = 0123456L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidOctalDigit() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '8', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) 0};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_ValidName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0};
        String expectedResult = "tar.arch.tests.zip";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseName_TrailingNull() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0};
        String expectedResult = "tar.arch.tests.zip";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes_ValidName() {
        String name = "tar.arch.tests.zip";
        byte[] expectedBuffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0};
        byte[] buffer = new byte[256];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "tar.arch.tests.zip.long.name.that.will.be.truncated";
        byte[] buffer = new byte[256];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0};
        assertArrayEquals(expectedBuffer, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidNumber() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_NumberTooLarge() {
        long value = 123456789012345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes_ValidNumber() {
        long value = 1234567;
        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' ', (byte) 0};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void testFormatLongOctalBytes_ValidNumber() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' '};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidNumber() {
        long value = 1234567;
        byte[] buffer = new byte[12];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' ', (byte) 0};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test
    public void testComputeCheckSum_ValidBuffer() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0};
        long expectedChecksum = 2281531749L;
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedChecksum, checksum);
    }
}