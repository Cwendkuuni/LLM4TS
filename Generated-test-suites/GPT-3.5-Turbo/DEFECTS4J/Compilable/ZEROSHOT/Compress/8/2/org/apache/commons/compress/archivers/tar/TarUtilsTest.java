package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    // Test parseOctal() method

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) '1';
        buffer[1] = (byte) '2';
        buffer[2] = (byte) '3';
        buffer[3] = (byte) '4';
        buffer[4] = (byte) '5';

        long result = TarUtils.parseOctal(buffer, 0, 5);

        assertEquals(12345, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[3];
        buffer[0] = (byte) '1';
        buffer[1] = (byte) 'a';
        buffer[2] = (byte) '3';

        TarUtils.parseOctal(buffer, 0, 3);
    }

    // Test parseName() method

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) 'h';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 'l';
        buffer[3] = (byte) 'l';
        buffer[4] = (byte) 'o';
        buffer[5] = 0;

        String result = TarUtils.parseName(buffer, 0, 6);

        assertEquals("hello", result);
    }

    // Test formatNameBytes() method

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];

        int result = TarUtils.formatNameBytes("hello", buffer, 0, 5);

        assertEquals(5, result);
        assertEquals((byte) 'h', buffer[0]);
        assertEquals((byte) 'e', buffer[1]);
        assertEquals((byte) 'l', buffer[2]);
        assertEquals((byte) 'l', buffer[3]);
        assertEquals((byte) 'o', buffer[4]);
        assertEquals(0, buffer[5]);
    }

    // Test formatUnsignedOctalString() method

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];

        TarUtils.formatUnsignedOctalString(12345, buffer, 0, 5);

        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) '5', buffer[4]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringOverflow() {
        byte[] buffer = new byte[3];

        TarUtils.formatUnsignedOctalString(12345, buffer, 0, 3);
    }

    // Test formatOctalBytes() method

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];

        int result = TarUtils.formatOctalBytes(12345, buffer, 0, 6);

        assertEquals(6, result);
        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) '5', buffer[4]);
        assertEquals((byte) ' ', buffer[5]);
        assertEquals(0, buffer[6]);
    }

    // Test formatLongOctalBytes() method

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];

        int result = TarUtils.formatLongOctalBytes(12345L, buffer, 0, 6);

        assertEquals(6, result);
        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) '5', buffer[4]);
        assertEquals((byte) ' ', buffer[5]);
    }

    // Test formatCheckSumOctalBytes() method

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];

        int result = TarUtils.formatCheckSumOctalBytes(12345L, buffer, 0, 6);

        assertEquals(6, result);
        assertEquals((byte) '1', buffer[0]);
        assertEquals((byte) '2', buffer[1]);
        assertEquals((byte) '3', buffer[2]);
        assertEquals((byte) '4', buffer[3]);
        assertEquals((byte) '5', buffer[4]);
        assertEquals(0, buffer[5]);
        assertEquals((byte) ' ', buffer[6]);
    }

    // Test computeCheckSum() method

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = (byte) 'h';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 'l';
        buffer[3] = (byte) 'l';
        buffer[4] = (byte) 'o';

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(532, result);
    }

}