package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarConstants.NAME_LENGTH;
import static org.apache.commons.compress.archivers.tar.TarConstants.OCTAL_STRING_LENGTH;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '4', (byte) ' ', (byte) '5', (byte) '1', (byte) '3', (byte) ' ', (byte) '7', (byte) '6', (byte) '4', (byte) '4', (byte) ' ', (byte) '6', (byte) '5', (byte) '1', (byte) '2', (byte) ' ', (byte) '0', (byte) '1', (byte) '0', (byte) ' ', (byte) '1', (byte) '2', (byte) ' ', (byte) '7', (byte) '7'};

        long expected = 07554513L * 0101277L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '4', (byte) 'X', (byte) '4', (byte) ' ', (byte) '6', (byte) '5', (byte) '1', (byte) '2', (byte) ' ', (byte) '0', (byte) '1', (byte) '0', (byte) ' ', (byte) '1', (byte) '2', (byte) ' ', (byte) '7', (byte) '7'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'd', (byte) 'i', (byte) 'r', (byte) ' ', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'b', (byte) 'z', (byte) '2'};
        String expected = "hello dir.gz.tar.bz2";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello dir.gz.tar.bz2";
        byte[] buffer = new byte[NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, NAME_LENGTH);
        String actual = new String(buffer, 0, offset);
        assertEquals(name, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 3573057775L;
        byte[] buffer = new byte[OCTAL_STRING_LENGTH + 2];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, OCTAL_STRING_LENGTH + 2);
        String expected = "077751531531536775153153";
        String actual = new String(buffer, 0, OCTAL_STRING_LENGTH + 2);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 3573057775L;
        byte[] buffer = new byte[OCTAL_STRING_LENGTH + 2];
        TarUtils.formatOctalBytes(value, buffer, 0, OCTAL_STRING_LENGTH + 2);
        String expected = "077751531531536775   ";
        String actual = new String(buffer, 0, OCTAL_STRING_LENGTH + 2);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{48, 73, 71, 76, 65, 54, 53, 48, 49, 49, 50, 48, 48, 49, 50, 51, 50, 48, 48, 48, 48};
        long actual = TarUtils.computeCheckSum(buffer);
        long expected = 0x6193503864326040L;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSumFail() {
        byte[] buffer = new byte[]{48, 73, 71, 76, 65, 54, 53, 48, 49, 49, 50, 48, 48, 49, 50, 51, 50, 48, 48, 48, 48, 49};
        long actual = TarUtils.computeCheckSum(buffer);
        assertNotEquals(expected, actual);
    }
}