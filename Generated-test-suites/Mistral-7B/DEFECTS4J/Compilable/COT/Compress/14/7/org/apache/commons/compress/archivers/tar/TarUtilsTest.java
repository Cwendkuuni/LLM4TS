package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long expected = 0123456754L;
        long result = TarUtils.parseOctal(buffer, 0, 11);
        assertEquals(expected, result);

        byte[] invalidBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8', '9', '0'}; // trailing byte is not a space or NUL
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 10);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] missingTrailerBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}; // missing trailing space or NUL
        try {
            TarUtils.parseOctal(missingTrailerBuffer, 0, 10);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] allNULBuffer = new byte[0]; // all NULs
        long resultAllNUL = TarUtils.parseOctal(allNULBuffer, 0, 0);
        assertEquals(0L, resultAllNUL);

        byte[] leadingNULBuffer = new byte[]{(byte) 0x00, '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}; // leading NUL
        long resultLeadingNUL = TarUtils.parseOctal(leadingNULBuffer, 0, 11);
        assertEquals(0L, resultLeadingNUL);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{(byte) 0x41, '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        long expected = 0x41123456789L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, 10);
        assertEquals(expected, result);

        byte[] invalidBinaryBuffer = new byte[]{(byte) 0x80, '1', '2', '3', '4', '5', '6', '7', '8', '9'}; // leading bit set
        try {
            TarUtils.parseOctalOrBinary(invalidBinaryBuffer, 0, 10);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] invalidOctalBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '9', '9', '9', '0'}; // non-octal digit
        try {
            TarUtils.parseOctalOrBinary(invalidOctalBuffer, 0, 11);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] binaryTooLongBuffer = new byte[]{(byte) 0x00, '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'}; // binary number exceeds maximum signed long value
        try {
            TarUtils.parseOctalOrBinary(binaryTooLongBuffer, 0, 11);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{(byte) 0x01};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(bufferTrue, 0);
        assertEquals(expected, result);

        byte[] bufferFalse = new byte[]{(byte) 0x00};
        boolean expectedFalse = false;
        boolean resultFalse = TarUtils.parseBoolean(bufferFalse, 0);
        assertEquals(expectedFalse, resultFalse);

        byte[] bufferInvalid = new byte[]{(byte) 0x02};
        try {
            TarUtils.parseBoolean(bufferInvalid, 0);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{
            (byte) 't', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't',
            (byte) 'x', ' ', 'e', 'x', 'a', 'm', 'p', 'l', 'e', ' ', 'd', 'i', 'r',
            ' ', 'c', 'h', 'a', 'r', 's', ' ', 't', 'e', 's', 't', '.', 't', 'a',
            'r', '.'
        };
        String expected = "tar.example.test.chars.tar.";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);

        byte[] bufferTrailingNUL = new byte[]{
            (byte) 't', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't',
            (byte) 't', 'x', ' ', 'e', 'x', 'a', 'm', 'p', 'l', 'e', ' ', 'd', 'i', 'r',
            (byte) ' ', 'c', 'h', 'a', 'r', 's', ' ', 't', 'e', 's', 't', '.', 't', 'a',
            'r', '.', 0
        };
        String resultTrailingNUL = TarUtils.parseName(bufferTrailingNUL, 0, bufferTrailingNUL.length);
        assertEquals(expected, resultTrailingNUL);

        byte[] bufferTrailingSpace = new byte[]{
            (byte) 't', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't',
            (byte) 't', 'x', ' ', 'e', 'x', 'a', 'm', 'p', 'l', 'e', ' ', 'd', 'i', 'r',
            (byte) ' ', 'c', 'h', 'a', 'r', 's', ' ', 't', 'e', 's', 't', '.', 't', 'a',
            'r', '.', ' '
        };
        String resultTrailingSpace = TarUtils.parseName(bufferTrailingSpace, 0, bufferTrailingSpace.length);
        assertEquals(expected, resultTrailingSpace);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "example.test.chars";
        byte[] expectedBuffer = new byte[]{
            (byte) 'e', 'x', 'a', 'm', 'p', 'l', 'e', '.', 't', 'e', 's', 't', '.', 'c', 'h', 'a', 'r', 's'
        };
        byte[] resultBuffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, resultBuffer, 0, 20);
        assertArrayEquals(expectedBuffer, resultBuffer);
        assertEquals(offset, 20);

        String nameTooLong = "example.test.chars.this.is.too.long";
        try {
            TarUtils.formatNameBytes(nameTooLong, resultBuffer, 0, 20);
            fail("An ArrayIndexOutOfBoundsException should have been thrown.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567890L;
        byte[] expectedBuffer = new byte[]{(byte) '0', '0', '0', '0', '1', '2', '3', '4', '5', '6', '7'};
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 12);
        assertArrayEquals(expectedBuffer, buffer);

        long valueTooLong = Long.MAX_VALUE + 1;
        try {
            TarUtils.formatUnsignedOctalString(valueTooLong, buffer, 0, 12);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567890L;
        byte[] expectedBuffer = new byte[]{
            (byte) '0', '0', '0', '1', '2', '3', '4', '5', '6', '7', ' ', ' '
        };
        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, 12);
        assertArrayEquals(expectedBuffer, buffer);

        long valueTooLong = Long.MAX_VALUE + 1;
        try {
            TarUtils.formatOctalBytes(valueTooLong, buffer, 0, 12);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 1234567890L;
        byte[] expectedBuffer = new byte[]{(byte) 0x30, '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 10);
        assertArrayEquals(expectedBuffer, buffer);

        long valueTooLong = Long.MAX_VALUE + 1;
        try {
            TarUtils.formatLongOctalOrBinaryBytes(valueTooLong, buffer, 0, 10);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long valueBinary = 123456789012345L;
        byte[] expectedBufferBinary = new byte[]{
            (byte) 0x40, '1', '2', '3', '4', '5', '6', '7', '8', '9', '1'
        };
        byte[] bufferBinary = new byte[11];
        TarUtils.formatLongOctalOrBinaryBytes(valueBinary, bufferBinary, 0, 11);
        assertArrayEquals(expectedBufferBinary, bufferBinary);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0, 'w', 'o', 'r', 'l', 'd'};
        long expected = 233619474849688L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 233619474849688L;
        byte[] expectedBuffer = new byte[]{
            (byte) '0', '1', '2', '3', '6', '6', '1', '3', '2', '2', '1', '1', '0', '1', '1', '1', '1', '1', '1'
        };
        byte[] buffer = new byte[20];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 20);
        assertArrayEquals(expectedBuffer, buffer);
    }
}