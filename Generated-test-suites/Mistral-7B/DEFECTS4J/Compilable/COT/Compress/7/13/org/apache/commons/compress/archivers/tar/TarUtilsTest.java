package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '0', '0'};
        long expected = 0175500;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '8'}; // invalid octal digit
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String expected = "tar.obj.tar.gz";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myfile.txt";
        byte[] buffer = new byte[32];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        for (int i = 0; i < name.length(); ++i) {
            assertEquals(name.charAt(i), buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        long value = 1234;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0210123";
        String result = new String(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringOverflow() {
        byte[] buffer = new byte[10];
        long value = Long.MAX_VALUE + 1;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[13];
        long value = 12345;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0210123\t\0";
        String result = new String(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesOverflow() {
        byte[] buffer = new byte[13];
        long value = Long.MAX_VALUE + 1;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        long expected = 549;
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, checksum);
    }

    @Test
    public void testComputeCheckSumAllZeroes() {
        byte[] buffer = new byte[0];
        long expected = 0;
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, checksum);
    }
}