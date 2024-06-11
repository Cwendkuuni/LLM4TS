import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_Valid() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '5', '5', '7', '7', '7'};
        long expected = 010075577;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '5', '5', '7', '7', '8'}; // invalid byte '8'
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_MissingTrailingSpaceOrNUL() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '5', '5', '7', '7'}; // missing trailing space or NUL
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', ' ', 'a', '.', 't', 's', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x', '.'};
        String expected = "tar.  a. txtx. ";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.  a. txtx.";
        byte[] buffer = new byte[32];
        int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(32, length);
    }

    @Test
    public void testFormatUnsignedOctalString_Valid() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "01234567";
        String actual = new String(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatUnsignedOctalString_ValueTooLong() {
        long value = 1234567890;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.fail("Expected IllegalArgumentException, but no exception was thrown.");
    }

    @Test
    public void testFormatOctalBytes_Valid() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567 ";
        String actual = new String(buffer, 0, 11);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalBytes_Valid() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567";
        String actual = new String(buffer, 0, 11);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatCheckSumOctalBytes_Valid() {
        long value = 1234567;
        byte[] buffer = new byte[4];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "01234567"; // leading zero added
        String actual = new String(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSum_Valid() {
        byte[] buf = new byte[]{0x75, 0x74, 0x69, 0x66, 0x69, 0x63, 0x61, 0x72, 0x65, 0x20, 0x74, 0x65, 0x73, 0x74, 0x20, 0x66, 0x69, 0x6c, 0x65, 0x64, 0x20, 0x74, 0x68, 0x69, 0x6e, 0x67, 0x20, 0x62, 0x65, 0x20, 0x73, 0x75, 0x72, 0x76, 0x65, 0x72};
        long checksumExpected = 3675535939231183353L;
        long checksumActual = TarUtils.computeCheckSum(buf);
        assertEquals(checksumExpected, checksumActual);
    }
}