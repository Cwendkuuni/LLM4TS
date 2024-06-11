import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidOctalNumber() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        long expectedResult = 0123456L;
        long actualResult = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidOctalDigit() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '8', (byte) '5', (byte) '6', (byte) '7', (byte) '8'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_ValidName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'p', (byte) 'j', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        String expectedResult = "tar.op.gzip";
        String actualResult = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatNameBytes_ValidName() {
        String name = "tar.op.gzip";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int actualResult = TarUtils.formatNameBytes(name, buf, offset, length);
        assertArrayEquals(new byte[]{'t', 'a', 'r', '.', 'o', 'p', '.', 'g', 'z', 'i', 'p'}, buf);
        assertEquals(offset + length, actualResult);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidValue() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', '7'}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_ValidValue() {
        long value = 1234567;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int actualResult = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2', '3', '4', '5', '6', ' '}, buf);
        assertEquals(offset + length, actualResult);
    }

    @Test
    public void testComputeCheckSum_ValidBytes() {
        byte[] buffer = new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67};
        long expectedResult = 0x01234567L;
        long actualResult = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, actualResult);
    }
}