package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) 0};
        assertEquals(0177777L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferInvalid = new byte[]{(byte) '0', (byte) '8', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) 0};
        try {
            TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        try {
            TarUtils.parseOctal(bufferNull, 0, bufferNull.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0};
        assertEquals("tar.op.tar.gz", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] bufferEmpty = new byte[]{(byte) 0};
        assertNull(TarUtils.parseName(bufferEmpty, 0, bufferEmpty.length));

        byte[] bufferNull = null;
        try {
            TarUtils.parseName(bufferNull, 0, bufferNull.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.op.tar.gz";
        byte[] buffer = new byte[name.length() + 1];
        int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{
                (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0}, buffer, 0, length);

        byte[] bufferTooSmall = new byte[name.length()];
        try {
            TarUtils.formatNameBytes(name, bufferTooSmall, 0, bufferTooSmall.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        try {
            TarUtils.formatNameBytes(name, bufferNull, 0, bufferNull.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{
                (byte) '0', (byte) '2', (byte) '0', (byte) '1', (byte) '4', (byte) '5', (byte) ' ', (byte) '0'}, buffer, 0, 7);

        long valueTooBig = Long.parseUnsignedLong("123456789123456789123456789123456789123456789L");
        try {
            TarUtils.formatUnsignedOctalString(valueTooBig, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        try {
            TarUtils.formatUnsignedOctalString(value, bufferNull, 0, bufferNull.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        assertArrayEquals(new byte[]{
                (byte) '0', (byte) '2', (byte) '0', (byte) '1', (byte) '4', (byte) '5', (byte) ' ', (byte) '0', (byte) ' ', (byte) '0'}, buffer, 0, offset);

        long valueTooBig = Long.parseUnsignedLong("123456789123456789123456789123456789123456789L");
        try {
            TarUtils.formatOctalBytes(valueTooBig, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        try {
            TarUtils.formatOctalBytes(value, bufferNull, 0, bufferNull.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 9);
        assertArrayEquals(new byte[]{
                (byte) '0', (byte) '2', (byte) '0', (byte) '1', (byte) '4', (byte) '5', (byte) ' ', (byte) '0'}, buffer, 0, offset);

        long valueTooBig = Long.parseUnsignedLong("123456789123456789123456789123456789123456789L");
        try {
            TarUtils.formatLongOctalBytes(valueTooBig, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        try {
            TarUtils.formatLongOctalBytes(value, bufferNull, 0, bufferNull.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 11);
        assertArrayEquals(new byte[]{
                (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) ' ', (byte) '0', (byte) ' ', (byte) '0', (byte) ' ', (byte) '0'}, buffer, 0, offset);

        long valueTooBig = Long.parseUnsignedLong("123456789123456789123456789123456789123456789L");
        try {
            TarUtils.formatCheckSumOctalBytes(valueTooBig, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        try {
            TarUtils.formatCheckSumOctalBytes(value, bufferNull, 0, bufferNull.length);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 1, 2, 3, 4};
        assertEquals(0x0f0f0fL, TarUtils.computeCheckSum(buffer));

        byte[] bufferEmpty = new byte[] {};
        assertEquals(0L, TarUtils.computeCheckSum(bufferEmpty));

        byte[] bufferNull = null;
        try {
            TarUtils.computeCheckSum(bufferNull);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}