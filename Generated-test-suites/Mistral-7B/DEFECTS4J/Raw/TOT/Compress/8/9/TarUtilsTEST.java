package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '0', '0'}; // valid octal number
        long expectedResult = 0175500L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);

        byte[] bufferInvalid = new byte[]{'0', 'x', '5', '5', '0', '0', '0'}; // invalid octal number
        try {
            TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferEmpty = new byte[0]; // empty buffer
        try {
            TarUtils.parseOctal(bufferEmpty, 0, bufferEmpty.length);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null; // null buffer
        try {
            TarUtils.parseOctal(bufferNull, 0, 0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] bufferValid = new byte[]{'a', 'b', 'c', 'd', 'e', '0'}; // valid name
        String expectedResult = "abcd e";
        String result = TarUtils.parseName(bufferValid, 0, bufferValid.length);
        assertEquals(expectedResult, result);

        byte[] bufferInvalid = new byte[]{'a', 'b', 'c', 'd', 'e', 'x'}; // invalid name
        try {
            TarUtils.parseName(bufferInvalid, 0, bufferValid.length);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferEmpty = new byte[0]; // empty buffer
        try {
            TarUtils.parseName(bufferEmpty, 0, bufferEmpty.length);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null; // null buffer
        try {
            TarUtils.parseName(bufferNull, 0, 0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String nameValid = "abcd e"; // valid name
        byte[] bufferExpected = new byte[]{'a', 'b', 'c', 'd', 'e', ' ', 0}; // trailing space and null
        byte[] bufferResult = new byte[nameValid.length() + 2];
        int offset = TarUtils.formatNameBytes(nameValid, bufferResult, 0, bufferResult.length);
        assertArrayEquals(bufferExpected, bufferResult);
        assertEquals(offset, bufferResult.length);

        String nameEmpty = ""; // empty name
        byte[] bufferResultEmpty = new byte[2];
        int offsetEmpty = TarUtils.formatNameBytes(nameEmpty, bufferResultEmpty, 0, bufferResultEmpty.length);
        assertArrayEquals(new byte[]{'', '0', 0}, bufferResultEmpty);
        assertEquals(offsetEmpty, bufferResultEmpty.length);

        String nameNull = null; // null name
        byte[] bufferResultNull = new byte[2];
        int offsetNull = TarUtils.formatNameBytes(nameNull, bufferResultNull, 0, bufferResultNull.length);
        assertArrayEquals(new byte[]{0, 0}, bufferResultNull);
        assertEquals(offsetNull, bufferResultNull.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long valueValid = 01234567L; // valid value
        byte[] bufferExpected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        byte[] bufferResult = new byte[8];
        TarUtils.formatUnsignedOctalString(valueValid, bufferResult, 0, bufferResult.length);
        assertArrayEquals(bufferExpected, bufferResult);

        long valueInvalid = Long.MAX_VALUE + 1; // value longer than buffer size
        try {
            byte[] bufferResultInvalid = new byte[8];
            TarUtils.formatUnsignedOctalString(valueInvalid, bufferResultInvalid, 0, bufferResultInvalid.length);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long valueValid = 01234567L; // valid value
        byte[] bufferExpected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        byte[] bufferResult = new byte[10];
        int offset = TarUtils.formatOctalBytes(valueValid, bufferResult, 0, bufferResult.length);
        assertArrayEquals(bufferExpected, bufferResult);
        assertEquals(offset, bufferResult.length);

        long valueInvalid = Long.MAX_VALUE + 1; // value longer than buffer size
        try {
            byte[] bufferResultInvalid = new byte[10];
            int offsetInvalid = TarUtils.formatOctalBytes(valueInvalid, bufferResultInvalid, 0, bufferResultInvalid.length);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e'}; // valid buffer
        long expectedResult = 0xcdab345L; // arbitrary checksum
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, result);

        byte[] bufferEmpty = new byte[0]; // empty buffer
        long expectedResultEmpty = 0;
        long resultEmpty = TarUtils.computeCheckSum(bufferEmpty);
        assertEquals(expectedResultEmpty, resultEmpty);

        byte[] bufferNull = null; // null buffer
        try {
            TarUtils.computeCheckSum(bufferNull);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}