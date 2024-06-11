import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0x12345678L, result);

        byte[] bufferInvalid = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5'};
        try {
            TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] bufferTrailing = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5'};
        try {
            TarUtils.parseOctal(bufferTrailing, 0, bufferTrailing.length - 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] bufferOctal = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5'};
        long result = TarUtils.parseOctalOrBinary(bufferOctal, 0, bufferOctal.length);
        Assert.assertEquals(0x12345678L, result);

        byte[] bufferBinary = new byte[]{(byte) 0x80, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39};
        long resultBinary = TarUtils.parseOctalOrBinary(bufferBinary, 0, bufferBinary.length);
        Assert.assertEquals(0x0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f2021222324252627282930313233343536373839L, resultBinary);

        byte[] bufferInvalidBinary = new byte[]{(byte) 0x80, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3a};
        try {
            TarUtils.parseOctalOrBinary(bufferInvalidBinary, 0, bufferInvalidBinary.length);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        Assert.assertTrue(TarUtils.parseBoolean(bufferTrue, 0));

        byte[] bufferFalse = new byte[]{0};
        Assert.assertFalse(TarUtils.parseBoolean(bufferFalse, 0));

        byte[] bufferInvalid = new byte[]{1, 0};
        try {
            TarUtils.parseBoolean(bufferInvalid, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] bufferName = new byte[]{'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', '.', 't', 'x', 't', 'x', ' ', 'I', 's', ' ', 'A', ' ', 'A', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r'};
        String result = TarUtils.parseName(bufferName, 0, bufferName.length);
        Assert.assertEquals("Tar File.txt Is A archive.tar", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Tar File.txt Is A archive.tar";
        byte[] bufferName = new byte[32];
        int length = TarUtils.formatNameBytes(name, bufferName, 0, bufferName.length);
        Assert.assertEquals(name.length(), length);
        String result = new String(bufferName, 0, length);
        Assert.assertEquals(name, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678L;
        byte[] buffer = new byte[21];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "01234567555555555555555555555555555555555555555555555567";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[13];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567  ";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x1234567890123456L;
        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123457601563456";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0x1234567890123456L;
        byte[] buffer = new byte[13];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte firstByte = buffer[0];
        Assert.assertTrue(firstByte >= 0 && firstByte <= 0x7f || firstByte >= 0xc0);
        String expected = "0123457601563456";
        if ((firstByte & 0xff) == 0) {
            expected += " ";
        }
        String result = new String(buffer, 1, buffer.length - 1);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[11];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "5555555555  ";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[]{'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', '.', 't', 'x', 't', 'x', ' ', 'I', 's', ' ', 'A', ' ', 'A', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r'};
        long checksum = TarUtils.computeCheckSum(header);
        Assert.assertEquals(0x1153B1F5D29D42D7L, checksum);

        byte[] headerInvalid = new byte[]{'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', '.', 't', 'x', 't', 'x', ' ', 'I', 's', ' ', 'A', ' ', 'A', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r', 'A'};
        try {
            TarUtils.computeCheckSum(headerInvalid);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}