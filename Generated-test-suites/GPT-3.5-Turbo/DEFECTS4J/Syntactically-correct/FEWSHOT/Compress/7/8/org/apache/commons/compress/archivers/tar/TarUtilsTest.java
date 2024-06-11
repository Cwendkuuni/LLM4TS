package org.apache.commons.compress.archivers.tar;

import org.junit.Test;

import static org.junit.Assert.*;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{48, 50, 51};
        assertEquals(83, TarUtils.parseOctal(buffer, 0, 3));
        buffer = new byte[]{48, 54, 55};
        assertEquals(47, TarUtils.parseOctal(buffer, 0, 3));
        buffer = new byte[]{48, 48, 56};
        assertEquals(8, TarUtils.parseOctal(buffer, 0, 3));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{97, 98, 99, 100, 101};
        assertEquals("abcde", TarUtils.parseName(buffer, 0, 5));
        buffer = new byte[]{116, 101, 115, 116};
        assertEquals("test", TarUtils.parseName(buffer, 0, 4));
        buffer = new byte[]{104, 101, 108, 108, 111};
        assertEquals("hello", TarUtils.parseName(buffer, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buffer = new byte[5];
        assertEquals(3, TarUtils.formatNameBytes(name, buffer, 0, 5));
        assertArrayEquals(new byte[]{97, 98, 99, 0, 0}, buffer);

        name = "test";
        buffer = new byte[6];
        assertEquals(4, TarUtils.formatNameBytes(name, buffer, 0, 6));
        assertArrayEquals(new byte[]{116, 101, 115, 116, 0, 0}, buffer);

        name = "hello";
        buffer = new byte[7];
        assertEquals(5, TarUtils.formatNameBytes(name, buffer, 0, 7));
        assertArrayEquals(new byte[]{104, 101, 108, 108, 111, 0, 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 83;
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);
        assertArrayEquals(new byte[]{56, 51, 0}, buffer);

        value = 47;
        buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);
        assertArrayEquals(new byte[]{55, 54, 0}, buffer);

        value = 8;
        buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);
        assertArrayEquals(new byte[]{56, 48, 0}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringException() {
        long value = 999;
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 3);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 83;
        byte[] buffer = new byte[4];
        assertEquals(4, TarUtils.formatOctalBytes(value, buffer, 0, 4));
        assertArrayEquals(new byte[]{56, 51, 32, 0}, buffer);

        value = 47;
        buffer = new byte[4];
        assertEquals(4, TarUtils.formatOctalBytes(value, buffer, 0, 4));
        assertArrayEquals(new byte[]{55, 54, 32, 0}, buffer);

        value = 8;
        buffer = new byte[4];
        assertEquals(4, TarUtils.formatOctalBytes(value, buffer, 0, 4));
        assertArrayEquals(new byte[]{56, 48, 32, 0}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 83;
        byte[] buffer = new byte[3];
        assertEquals(3, TarUtils.formatLongOctalBytes(value, buffer, 0, 3));
        assertArrayEquals(new byte[]{56, 51, 32}, buffer);

        value = 47;
        buffer = new byte[2];
        assertEquals(2, TarUtils.formatLongOctalBytes(value, buffer, 0, 2));
        assertArrayEquals(new byte[]{55, 32}, buffer);

        value = 8;
        buffer = new byte[5];
        assertEquals(5, TarUtils.formatLongOctalBytes(value, buffer, 0, 5));
        assertArrayEquals(new byte[]{56, 32, 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 83;
        byte[] buffer = new byte[4];
        assertEquals(4, TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 4));
        assertArrayEquals(new byte[]{56, 51, 0, 32}, buffer);

        value = 47;
        buffer = new byte[4];
        assertEquals(4, TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 4));
        assertArrayEquals(new byte[]{55, 54, 0, 32}, buffer);

        value = 8;
        buffer = new byte[4];
        assertEquals(4, TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 4));
        assertArrayEquals(new byte[]{56, 48, 0, 32}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{56, 51, 32, 65, 98};
        assertEquals(282, TarUtils.computeCheckSum(buffer));

        buffer = new byte[]{55, 54, 32, 84};
        assertEquals(268, TarUtils.computeCheckSum(buffer));

        buffer = new byte[]{56, 48, 32, 68};
        assertEquals(304, TarUtils.computeCheckSum(buffer));
    }
}