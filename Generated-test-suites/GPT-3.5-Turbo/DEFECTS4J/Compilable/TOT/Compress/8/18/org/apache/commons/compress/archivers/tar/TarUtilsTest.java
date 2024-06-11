package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        long expected = 7;

        buffer[0] = (byte) '0';
        buffer[1] = (byte) '0';
        buffer[2] = (byte) '7';
        buffer[3] = (byte) ' ';
        buffer[4] = 0;
        buffer[5] = (byte) ' ';
        buffer[6] = (byte) ' ';
        buffer[7] = (byte) ' ';

        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        String expected = "test";

        buffer[0] = (byte) 't';
        buffer[1] = (byte) 'e';
        buffer[2] = (byte) 's';
        buffer[3] = (byte) 't';
        buffer[4] = 0;
        buffer[5] = (byte) ' ';
        buffer[6] = (byte) ' ';
        buffer[7] = (byte) ' ';

        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int expectedOffset = 4;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expectedOffset, result);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        assertEquals((byte) 0, buffer[4]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 7;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals('0', buffer[offset]);
        assertEquals('0', buffer[offset + 1]);
        assertEquals('0', buffer[offset + 2]);
        assertEquals('0', buffer[offset + 3]);
        assertEquals('0', buffer[offset + 4]);
        assertEquals('0', buffer[offset + 5]);
        assertEquals('0', buffer[offset + 6]);
        assertEquals('7', buffer[offset + 7]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 7;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        int expectedOffset = 7;
        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(expectedOffset, result);
        assertEquals('0', buffer[offset]);
        assertEquals('0', buffer[offset + 1]);
        assertEquals('0', buffer[offset + 2]);
        assertEquals('0', buffer[offset + 3]);
        assertEquals('0', buffer[offset + 4]);
        assertEquals('0', buffer[offset + 5]);
        assertEquals('7', buffer[offset + 6]);
        assertEquals((byte) ' ', buffer[offset + 7]);
        assertEquals((byte) 0, buffer[offset + 8]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 7;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        int expectedOffset = 8;
        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(expectedOffset, result);
        assertEquals('0', buffer[offset]);
        assertEquals('0', buffer[offset + 1]);
        assertEquals('0', buffer[offset + 2]);
        assertEquals('0', buffer[offset + 3]);
        assertEquals('0', buffer[offset + 4]);
        assertEquals('0', buffer[offset + 5]);
        assertEquals((byte) ' ', buffer[offset + 6]);
        assertEquals((byte) '7', buffer[offset + 7]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 7;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        int expectedOffset = 8;
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(expectedOffset, result);
        assertEquals('0', buffer[offset]);
        assertEquals('0', buffer[offset + 1]);
        assertEquals('0', buffer[offset + 2]);
        assertEquals('0', buffer[offset + 3]);
        assertEquals('0', buffer[offset + 4]);
        assertEquals('0', buffer[offset + 5]);
        assertEquals((byte) 0, buffer[offset + 6]);
        assertEquals((byte) ' ', buffer[offset + 7]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        long expected = 45;

        buffer[0] = (byte) 1;
        buffer[1] = (byte) 2;
        buffer[2] = (byte) 3;
        buffer[3] = (byte) 4;
        buffer[4] = (byte) 5;
        buffer[5] = (byte) 6;
        buffer[6] = (byte) 7;
        buffer[7] = (byte) 8;
        buffer[8] = (byte) 9;
        buffer[9] = (byte) 10;

        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

}