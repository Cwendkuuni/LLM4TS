import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7'};
        int offset = 0;
        int length = buffer.length;

        long result = TarUtils.parseOctal(buffer, offset, length);

        Assert.assertEquals(0755577L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput_TrailingByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '1'};
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput_LeadingByte() {
        byte[] buffer = new byte[]{'1', '7', '5', '5', '7', '7'};
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseOctal_InputWithOnlyLeadingAndTrailingSpaces() {
        byte[] buffer = new byte[]{' ', ' ', ' ', ' ', ' ', ' '};

        long result = TarUtils.parseOctal(buffer, 0, buffer.length);

        Assert.assertEquals(0L, result);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'a', ' ', 'd', 'i', 'r', ' ', 'e', 'n', 't', 'r', 'y'};

        String result = TarUtils.parseName(buffer, 0, buffer.length);

        Assert.assertEquals("tar entry", result);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "tar entry";
        byte[] buffer = new byte[name.length() + 1];
        int offset = 0;
        int length = buffer.length;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        Assert.assertEquals(offset + length, result);
        Assert.assertArrayEquals(name.getBytes(), buffer, 0, name.length());
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "tar entry longer than buffer";
        byte[] buffer = new byte[name.length()];
        int offset = 0;
        int length = buffer.length;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        Assert.assertEquals(offset + length, result);
        String expected = name.substring(0, length);
        Assert.assertArrayEquals(expected.getBytes(), buffer, 0, length);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 123;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        String expected = "012\007\037\077";
        Assert.assertArrayEquals(expected.getBytes(), buffer, 0, length);
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

        Assert.assertEquals(offset + length, result);
        Assert.assertEquals("012  ", new String(buffer, 0, length));
    }

    @Test
    public void testFormatLongOctalBytes_ValidInput() {
        long value = 123456789;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);

        Assert.assertEquals(offset + length, result);
        Assert.assertEquals("0124567\075", new String(buffer, 0, length));
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long value = 123456789;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = buffer.length;

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        Assert.assertEquals(offset + length, result);
        Assert.assertEquals("0124567\000 \001", new String(buffer, 0, length));
    }

    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'a', ' ', 'd', 'i', 'r', ' ', 'e', 'n', 't', 'r', 'y'};

        long result = TarUtils.computeCheckSum(buffer);

        long expected = 0x21337815;
        Assert.assertEquals(expected, result);
    }
}