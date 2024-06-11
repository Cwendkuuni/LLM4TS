package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] { 0, '0', '1', '2', '3', '4', '5', '6', '7' };
        assertEquals(01012345L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferInvalid = new byte[] { 0, '0', '1', '2', '3', '8', '4', '5', '6' };
        try {
            TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNoTrailing = new byte[] { 0, '0', '1', '2', '3', '4', '5', '6' };
        try {
            TarUtils.parseOctal(bufferNoTrailing, 0, bufferNoTrailing.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        try {
            TarUtils.parseOctal(bufferNull, 0, bufferNull.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = "hello world".getBytes(StandardCharsets.UTF_8);
        assertEquals("hello world", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] bufferEmpty = new byte[0];
        assertNull(TarUtils.parseName(bufferEmpty, 0, bufferEmpty.length));

        byte[] bufferTooLong = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        assertEquals("...", TarUtils.parseName(bufferTooLong, 0, bufferTooLong.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello world";
        byte[] buffer = new byte[21];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("hello world", new String(buffer, 0, offset));

        String nameEmpty = "";
        byte[] bufferEmpty = new byte[1];
        int offsetEmpty = TarUtils.formatNameBytes(nameEmpty, bufferEmpty, 0, bufferEmpty.length);
        assertEquals("", new String(bufferEmpty, 0, offsetEmpty));

        String nameTooLong = "abcdefghijklmnopqrstuvwxyz";
        byte[] bufferTooLong = new byte[1 + nameTooLong.length()];
        int offsetTooLong = TarUtils.formatNameBytes(nameTooLong, bufferTooLong, 0, bufferTooLong.length);
        assertEquals(nameTooLong, new String(bufferTooLong, 0, offsetTooLong));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { '0', '0', '5', '1', '2', '3', '4', '5', ' ', '0' }, buffer);

        long valueZero = 0;
        byte[] bufferZero = new byte[1];
        TarUtils.formatUnsignedOctalString(valueZero, bufferZero, 0, bufferZero.length);
        assertArrayEquals(new byte[] { '0' }, bufferZero);

        long valueLong = 123456789012345L;
        byte[] bufferLong = new byte[21];
        TarUtils.formatUnsignedOctalString(valueLong, bufferLong, 0, bufferLong.length);
        assertArrayEquals(new byte[] { '0', '1', '3', '1', '0', '1', '1', '1', '0', '1', '2', '0', '1', '3', '4', '5', '6', '7', '8', '9', '0' }, bufferLong);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[11];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { '0', '0', '5', '1', '2', '3', '4', '5', ' ', '0', ' ' }, buffer);

        long valueZero = 0;
        byte[] bufferZero = new byte[1];
        TarUtils.formatOctalBytes(valueZero, bufferZero, 0, bufferZero.length);
        assertEquals(0, bufferZero.length);

        long valueLong = 123456789012345L;
        byte[] bufferLong = new byte[21];
        TarUtils.formatOctalBytes(valueLong, bufferLong, 0, bufferLong.length);
        // the trailing space is not included in the length parameter
        assertArrayEquals(new byte[] { '0', '1', '3', '1', '0', '1', '1', '1', '0', '1', '2', '0', '1', '3', '4', '5', '6', '7', '8', '9' }, bufferLong);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { '0', '0', '5', '1', '2', '3', '4', '5', ' ', '0' }, buffer);

        long valueZero = 0;
        byte[] bufferZero = new byte[1];
        TarUtils.formatLongOctalBytes(valueZero, bufferZero, 0, bufferZero.length);
        assertEquals(0, bufferZero.length);

        long valueLong = 123456789012345L;
        byte[] bufferLong = new byte[11];
        TarUtils.formatLongOctalBytes(valueLong, bufferLong, 0, bufferLong.length);
        assertArrayEquals(new byte[] { '0', '1', '3', '1', '0', '1', '1', '1', '0', '1', '2' }, bufferLong);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { '0', '1', '2', '3', '4', '5', ' ', ' ', '0', '0', '5', '1' }, buffer);

        long valueZero = 0;
        byte[] bufferZero = new byte[1];
        TarUtils.formatCheckSumOctalBytes(valueZero, bufferZero, 0, bufferZero.length);
        assertEquals(0, bufferZero.length);

        long valueLong = 123456789012345L;
        byte[] bufferLong = new byte[13];
        TarUtils.formatCheckSumOctalBytes(valueLong, bufferLong, 0, bufferLong.length);
        assertArrayEquals(new byte[] { '0', '1', '3', '1', '0', '1', '1', '1', '0', '1', '2', '0' }, bufferLong);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] { 0x78, 0x12, 0x34, 0x56, 0x78, 0x12, 0x34, 0x56 };
        assertEquals(0x78123456L, TarUtils.computeCheckSum(buffer));

        byte[] bufferEmpty = new byte[0];
        assertEquals(0L, TarUtils.computeCheckSum(bufferEmpty));
    }
}