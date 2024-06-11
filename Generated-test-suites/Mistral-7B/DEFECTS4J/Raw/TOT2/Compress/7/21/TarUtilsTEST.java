import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatCheckSumOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;

/**
 * TarUtilsTest class for JUnit testing of TarUtils methods.
 */
public class TarUtilsTest {

    /**
     * Test method for TarUtils.parseOctal(byte[], int, int).
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0x0, (byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '7', (byte) '7', (byte) '7'};
        int offset = 0;
        int length = buffer.length;
        long expected = 017555777L;
        long actual = TarUtils.parseOctal(buffer, offset, length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for TarUtils.parseName(byte[], int, int).
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'l', (byte) 'z', (byte) 'i', (byte) 'p', 0};
        int offset = 0;
        int length = buffer.length;
        String expected = "tar.az.lib";
        String actual = TarUtils.parseName(buffer, offset, length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for TarUtils.formatNameBytes(String, byte[], int, int).
     */
    @Test
    public void testFormatNameBytes() {
        String name = "myFile.txt";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = formatNameBytes(name, buffer, offset, length);
        Assert.assertEquals(expected, offset + length);
        Assert.assertArrayEquals(name.getBytes(), buffer, 0, name.length());
    }

    /**
     * Test method for TarUtils.formatOctalBytes(long, byte[], int, int).
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = formatOctalBytes(value, buffer, offset, length);
        Assert.assertEquals(expected, offset + length);
        // Check the format of the byte array
        Assert.assertArrayEquals(new byte[]{0x01, 0x01, 0x2, 0x3, 0x4, 0x5}, buffer, 0, 6);
        Assert.assertEquals(6, buffer.length - 2);
        Assert.assertEquals((byte) ' ', buffer[expected - 1]);
        Assert.assertEquals((byte) 0, buffer[expected]);
    }

    /**
     * Test method for TarUtils.formatUnsignedOctalString(long, byte[], int, int).
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = formatOctalBytes(value, buffer, offset, length);
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length - 2);
        Assert.assertEquals(expected, offset + length);
        Assert.assertEquals("012345", new String(buffer, 0, length - 2));
    }

    /**
     * Test method for TarUtils.computeCheckSum(byte[]).
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x0, 0x1, 0x2, 0x3};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for TarUtils.formatCheckSumOctalBytes(long, byte[], int, int).
     */
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 13;
        int expected = formatCheckSumOctalBytes(value, buffer, offset, length);
        Assert.assertEquals(expected, offset + length);
        Assert.assertEquals("02026641611577756134671", new String(buffer, 0, length));
    }
}