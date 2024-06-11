package org.apache.commons.compress.archivers.tar;

import org.junit.Test;

import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{48, 48, 48, 49, 48}; // "00010"
        long expected = 10;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100}; // "Hello World"
        String expected = "Hello World";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Hello World";
        byte[] buffer = new byte[20];
        int offset = 0;
        int length = 20;
        int expected = 20;
        int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expected, actual);
        assertEquals((byte) 'H', buffer[0]);
        assertEquals((byte) 'e', buffer[1]);
        assertEquals((byte) 'l', buffer[2]);
        assertEquals((byte) 'l', buffer[3]);
        assertEquals((byte) 'o', buffer[4]);
        assertEquals((byte) ' ', buffer[5]);
        assertEquals((byte) 'W', buffer[6]);
        assertEquals((byte) 'o', buffer[7]);
        assertEquals((byte) 'r', buffer[8]);
        assertEquals((byte) 'l', buffer[9]);
        assertEquals((byte) 'd', buffer[10]);
        for (int i = 11; i < 20; i++) {
            assertEquals((byte) 0, buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 10;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals((byte) '1', buffer[9]);
        assertEquals((byte) '0', buffer[8]);
        for (int i = 0; i < 8; i++) {
            assertEquals((byte) '0', buffer[i]);
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 10;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;
        int expected = 12;
        int actual = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(expected, actual);
        assertEquals((byte) '1', buffer[10]);
        assertEquals((byte) '0', buffer[11]);
        assertEquals((byte) ' ', buffer[9]);
        assertEquals((byte) 0, buffer[8]);
        for (int i = 0; i < 8; i++) {
            assertEquals((byte) '0', buffer[i]);
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 10;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 11;
        int expected = 11;
        int actual = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(expected, actual);
        assertEquals((byte) '1', buffer[9]);
        assertEquals((byte) '0', buffer[10]);
        assertEquals((byte) ' ', buffer[8]);
        for (int i = 0; i < 8; i++) {
            assertEquals((byte) '0', buffer[i]);
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 10;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;
        int expected = 12;
        int actual = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(expected, actual);
        assertEquals((byte) '1', buffer[10]);
        assertEquals((byte) '0', buffer[11]);
        assertEquals((byte) 0, buffer[9]);
        assertEquals((byte) ' ', buffer[8]);
        for (int i = 0; i < 8; i++) {
            assertEquals((byte) '0', buffer[i]);
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100}; // "Hello World"
        long expected = 1008;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}

