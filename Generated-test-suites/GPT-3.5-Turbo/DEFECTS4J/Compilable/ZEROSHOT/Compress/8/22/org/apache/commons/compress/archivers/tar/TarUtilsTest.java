package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[5];
        buffer[0] = 0;
        buffer[1] = ' ';
        buffer[2] = '2';
        buffer[3] = '5';
        buffer[4] = '0';
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(250, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[1];
        buffer[0] = 'A';
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[5];
        buffer[0] = 'N';
        buffer[1] = 'a';
        buffer[2] = 'm';
        buffer[3] = 'e';
        buffer[4] = 0;
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Name", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Name";
        byte[] buffer = new byte[10];
        buffer[0] = '1';
        buffer[1] = '2';
        buffer[2] = '3';
        TarUtils.formatNameBytes(name, buffer, 3, name.length());
        assertEquals('N', buffer[3]);
        assertEquals('a', buffer[4]);
        assertEquals('m', buffer[5]);
        assertEquals('e', buffer[6]);
        assertEquals(0, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(0, buffer[9]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 250;
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals('2', buffer[0]);
        assertEquals('5', buffer[1]);
        assertEquals('0', buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueTooLong() {
        long value = 1000000;
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 250;
        byte[] buffer = new byte[5];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals('2', buffer[0]);
        assertEquals('5', buffer[1]);
        assertEquals('0', buffer[2]);
        assertEquals(' ', buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 250;
        byte[] buffer = new byte[4];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals('2', buffer[0]);
        assertEquals('5', buffer[1]);
        assertEquals('0', buffer[2]);
        assertEquals(' ', buffer[3]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 250;
        byte[] buffer = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals('2', buffer[0]);
        assertEquals('5', buffer[1]);
        assertEquals('0', buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[5];
        buffer[0] = 'N';
        buffer[1] = 'a';
        buffer[2] = 'm';
        buffer[3] = 'e';
        buffer[4] = 0;
        long result = TarUtils.computeCheckSum(buffer);
        long expectedSum = 'N' + 'a' + 'm' + 'e';
        assertEquals(expectedSum, result);
    }

}