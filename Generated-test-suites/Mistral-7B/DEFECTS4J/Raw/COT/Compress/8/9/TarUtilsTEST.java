import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] inputBuffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '5', '0'};
        long expectedResult = 01755775L;
        long actualResult = TarUtils.parseOctal(inputBuffer, 0, inputBuffer.length);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput_MissingTrailingNUL() {
        byte[] inputBuffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '5'};
        TarUtils.parseOctal(inputBuffer, 0, inputBuffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput_InvalidByte() {
        byte[] inputBuffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '5', '8'};
        TarUtils.parseOctal(inputBuffer, 0, inputBuffer.length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] inputBuffer = new byte[]{(byte) 0x73, (byte) 0x61, (byte) 0x73, (byte) 0x74, (byte) 0x65, (byte) 0x52, (byte) 0x2E, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x31, (byte) 0x2E, (byte) 0x63, (byte) 0x6F, (byte) 0x6D};
        String expectedResult = "stat.txt.mod";
        String actualResult = TarUtils.parseName(inputBuffer, 0, inputBuffer.length);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "stat.txt.mod";
        byte[] buffer = new byte[name.length() + 1];
        int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(name.getBytes(), Arrays.copyOfRange(buffer, 0, length));
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 01755775L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{(byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '7', (byte) '5', (byte) '5', (byte) '7', (byte) '7', (byte) '7', (byte) '5', (byte) '0', (byte) ' '};
        assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_InvalidInput() {
        long value = 1234567890L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 01755775L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{(byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '7', (byte) '5', (byte) '5', (byte) '7', (byte) '7', (byte) '5', (byte) '0', (byte) ' ', (byte) '0'};
        assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, offset));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_InvalidInput() {
        long value = 1234567890L;
        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long value = 1234567890L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{(byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '0', (byte) ' ', (byte) '0'};
        assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, offset));
    }
}