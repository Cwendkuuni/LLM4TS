package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{' ', ' ', ' ', '0', '1', '0'};
        int offset = 0;
        int length = 6;
        long expected = 010;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'1', '2', '3', '4'};
        int offset = 0;
        int length = 4;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'e', 's', 't', ' ', 'n', 'a', 'm', 'e', 0};
        int offset = 0;
        int length = 10;
        String expected = "test name";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseNameWithTrailingNulls() {
        byte[] buffer = new byte[]{'t', 'e', 's', 't', ' ', 'n', 'a', 'm', 'e', 0, 0, 0};
        int offset = 0;
        int length = 10;
        String expected = "test name";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test name";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int expectedOffset = 10;
        int resultOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expectedOffset, resultOffset);
        assertArrayEquals(new byte[]{'t', 'e', 's', 't', ' ', 'n', 'a', 'm', 'e', 0}, buf);
    }

    @Test
    public void testFormatNameBytesWithLongName() {
        String name = "this is a very long name";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int expectedOffset = 10;
        int resultOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expectedOffset, resultOffset);
        assertArrayEquals(new byte[]{'t', 'h', 'i', 's', ' ', 'i', 's', ' ', ' ', ' '}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 8;
        byte[] buffer = new byte[2];
        int offset = 0;
        int length = 2;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{56, 48}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidValue() {
        long value = 10;
        byte[] buffer = new byte[1];
        int offset = 0;
        int length = 1;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 8;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        int expectedOffset = 4;
        int resultOffset = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(expectedOffset, resultOffset);
        assertArrayEquals(new byte[]{56, 32, 0, 0}, buf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesValueTooLarge() {
        long value = 10000;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 8;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        int expectedOffset = 3;
        int resultOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(expectedOffset, resultOffset);
        assertArrayEquals(new byte[]{56, 32, 32}, buf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytesValueTooLarge() {
        long value = 10000;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        TarUtils.formatLongOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 8;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        int expectedOffset = 4;
        int resultOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(expectedOffset, resultOffset);
        assertArrayEquals(new byte[]{56, 0, 32, 0}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{1, 2, 3};
        long expectedSum = 6;
        long resultSum = TarUtils.computeCheckSum(buf);
        assertEquals(expectedSum, resultSum);
    }

}