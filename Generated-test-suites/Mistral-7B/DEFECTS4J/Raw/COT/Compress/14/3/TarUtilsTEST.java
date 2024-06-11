import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidCase() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', ' ', 'N'};
        long expected = 77777777777L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_MissingTrailingSpace_ThrowsException() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctal_InvalidByte_ThrowsException() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8'};
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctalOrBinary_ValidBinaryNumber() {
        byte[] buffer = new byte[]{(byte) 0xFF, (byte) 0x7F, (byte) 0x7F};
        long expected = 255L << 8 | 127L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseOctalOrBinary_ValidOctalNumber() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
        long expected = 77777777777L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseOctalOrBinary_InvalidByte_ThrowsException() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8'};
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    @Test
    public void testParseBoolean_ValidCase() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseBoolean_InvalidByte_ThrowsException() {
        byte[] buffer = new byte[]{0};
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(buffer, 0));
    }

    @Test
    public void testParseName_ValidCase() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String expected = "tar.file.gzip";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_ValidCase() {
        String name = "testName.tar.gz";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 10);
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer_ThrowsException() {
        String name = "testName.tar.gz.testName.tar.gz.";
        byte[] buffer = new byte[10];
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatNameBytes(name, buffer, 0, buffer.length));
    }

    @Test
    public void testFormatUnsignedOctalString_ValidCase() {
        long value = 777L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, buffer.length);
        String expected = "0777";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatUnsignedOctalString_ValueTooLargeForBuffer_ThrowsException() {
        long value = 123456789012345L;
        byte[] buffer = new byte[5];
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length));
    }

    @Test
    public void testFormatOctalBytes_ValidCase() {
        long value = 777L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 11);
    }

    @Test
    public void testFormatOctalBytes_ValueTooLargeForBuffer_ThrowsException() {
        long value = 123456789012345L;
        byte[] buffer = new byte[5];
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, 0, buffer.length));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidCase() {
        long value = 777L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 11);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValueTooLargeForBuffer_ThrowsException() {
        long value = 123456789012345L;
        byte[] buffer = new byte[5];
        Assert.assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValueFitsAsOctal_WritesOctal() {
        long value = 777L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 11);
        String result = new String(buffer, 0, offset);
        String expected = "0777";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValueDoesNotFitAsOctal_WritesBinary() {
        long value = 123456789012345L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 11);
        String result = new String(buffer, 0, offset);
        Assert.assertTrue(result.startsWith("0"));
        Assert.assertEquals(10, Long.toBinaryString(value & BYTE_MASK).length());
        for (int i = 0; i < 10; i++){
            Assert.assertEquals(Byte.parseByte(String.valueOf(result.charAt(i+1)), 16), (byte) (value>>>8*i & BYTE_MASK));
        }
    }
}