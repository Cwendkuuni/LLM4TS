import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0'};
        assertEquals(01777770L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferInvalid = new byte[]{'0', '7', '7', '7', '7', '8', '7', '7', '0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length));

        byte[] bufferTrailingNul = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0', '0'};
        assertEquals(01777770L, TarUtils.parseOctal(bufferTrailingNul, 0, bufferTrailingNul.length));

        byte[] bufferTrailingSpace = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0', ' '};
        assertEquals(01777770L, TarUtils.parseOctal(bufferTrailingSpace, 0, bufferTrailingSpace.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] bufferOctal = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0'};
        assertEquals(01777770L, TarUtils.parseOctalOrBinary(bufferOctal, 0, bufferOctal.length));

        byte[] bufferBinary = new byte[]{(byte) 0x81, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a};
        assertEquals(0x812a2a2a2a2a2a2aL, TarUtils.parseOctalOrBinary(bufferBinary, 0, bufferBinary.length));

        byte[] bufferInvalid = new byte[]{'0', '7', '7', '7', '7', '8', '7', '7', '0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(bufferInvalid, 0, bufferInvalid.length));

        byte[] bufferBinaryTooLong = new byte[]{(byte) 0x81, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(bufferBinaryTooLong, 0, bufferBinaryTooLong.length));
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        assertEquals(true, TarUtils.parseBoolean(bufferTrue, 0));

        byte[] bufferFalse = new byte[]{0};
        assertEquals(false, TarUtils.parseBoolean(bufferFalse, 0));

        byte[] bufferInvalid = new byte[]{2};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(bufferInvalid, 0));
    }

    @Test
    public void testParseName() {
        byte[] bufferName = new byte[]{'t', 'a', 'r', ' ', 'T', 'e', 's', 't', '1', '.'};
        assertEquals("tar Test1.", TarUtils.parseName(bufferName, 0, bufferName.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar Test1.";
        byte[] buf = new byte[TarConstants.NAMELEN];
        int offset = TarUtils.formatNameBytes(name, buf, 0, TarConstants.NAMELEN);
        assertEquals("tar Test1.", new String(buf, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 777777;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("0777777", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 777777;
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0777777\0 ", new String(buffer));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 777777;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0777777\0 ", new String(buffer));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals("0123456789012345\0 ", new String(buffer));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 777777;
        byte[] buffer = new byte[TarConstants.UIDLEN * 2];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0777777\0  ", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r'};
        assertEquals(4253569115L, TarUtils.computeCheckSum(buffer));
    }
}