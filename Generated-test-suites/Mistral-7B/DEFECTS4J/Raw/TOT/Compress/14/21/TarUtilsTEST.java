package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ByteUtils;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * Unit tests for TarUtils class.
 */
public class TarUtilsTest {

    /** Test parseOctal method with valid input. */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = "0775\0".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0775L, result);
    }

    /** Test parseOctal method with missing trailing space/NUL. */
    @Test
    public void testParseOctalMissingTrailingSpaceOrNull() {
        byte[] buffer = "0775".getBytes();
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /** Test parseOctal method with invalid byte. */
    @Test
    public void testParseOctalInvalidByte() {
        byte[] buffer = "077A".getBytes();
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /** Test parseOctalOrBinary method with valid binary input. */
    @Test
    public void testParseOctalOrBinaryValidBinaryInput() {
        byte[] buffer = { 0x0, (byte) 0x81, 0x0, 0x3e };
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x00003e81L, result);
    }

    /** Test parseOctalOrBinary method with valid octal input. */
    @Test
    public void testParseOctalOrBinaryValidOctalInput() {
        byte[] buffer = "0755\0".getBytes();
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0755L, result);
    }

    /** Test parseOctalOrBinary method with missing trailing space/NUL in binary input. */
    @Test
    public void testParseOctalOrBinaryMissingTrailingSpaceOrNullBinaryInput() {
        byte[] buffer = { 0x0, (byte) 0x81 };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /** Test parseOctalOrBinary method with invalid byte in octal input. */
    @Test
    public void testParseOctalOrBinaryInvalidByteOctalInput() {
        byte[] buffer = "077A\0".getBytes();
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /** Test parseOctalOrBinary method with binary input exceeding maximum signed long value. */
    @Test
    public void testParseOctalOrBinaryExceedsMaximumSignedLongValue() {
        byte[] buffer = { 0x0, (byte) 0x80, 0x7f, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /** Test parseBoolean method with true input. */
    @Test
    public void testParseBooleanTrueInput() {
        byte[] buffer = { 1 };
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);
    }

    /** Test parseBoolean method with false input. */
    @Test
    public void testParseBooleanFalseInput() {
        byte[] buffer = { 0 };
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertFalse(result);
    }

    /** Test parseName method with valid input. */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = "filename\0".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("filename", result);
    }

    /** Test parseName method with missing trailing null. */
    @Test
    public void testParseNameMissingTrailingNull() {
        byte[] buffer = "filename".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, 0, buffer.length));
    }

    /** Test formatNameBytes method with valid input. */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "filename";
        byte[] buffer = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String parsedName = new String(buffer, 0, offset);
        assertEquals("filename", parsedName);
    }

    /** Test formatNameBytes method with buffer too small. */
    @Test
    public void testFormatNameBytesBufferTooSmall() {
        String name = "filename";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String parsedName = new String(buffer, 0, offset);
        assertEquals("filename", parsedName);
        assertEquals(13, offset); // 11 chars + 2 NULs
    }

    /** Test formatUnsignedOctalString method with valid input. */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 7654321L;
        byte[] buffer = new byte[11];
        int length = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("017605631\0", new String(buffer, 0, length));
    }

    /** Test formatUnsignedOctalString method with value too large for buffer. */
    @Test
    public void testFormatUnsignedOctalStringValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int length = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String message = "Value " + value + " is too large for " + length + " byte buffer.";
        assertThrows(IllegalArgumentException.class, () -> formatUnsignedOctalString(value, buffer, 0, buffer.length));
        assertEquals(message, TarUtils.exceptionMessage(buffer, 0, buffer.length, 0, buffer[0]));
    }

    /** Test formatOctalBytes method with valid input. */
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 7654321L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String parsedValue = new String(buffer, 0, offset - 2); // remove trailing space and NUL
        assertEquals("017605631", parsedValue);
    }

    /** Test formatOctalBytes method with value too large for buffer. */
    @Test
    public void testFormatOctalBytesValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String message = "Value " + value + " is too large for " + length + " byte field.";
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, 0, buffer.length));
        assertEquals(message, TarUtils.exceptionMessage(buffer, 0, buffer.length, 0, buffer[0]));
    }

    /** Test formatLongOctalBytes method with valid input. */
    @Test
    public void testFormatLongOctalBytesValidInput() {
        long value = 7654321L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String parsedValue = new String(buffer, 0, offset - 1); // remove trailing space
        assertEquals("017605631", parsedValue);
    }

    /** Test formatLongOctalBytes method with value too large for buffer. */
    @Test
    public void testFormatLongOctalBytesValueTooLarge() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String message = "Value " + value + " is too large for " + length + " byte field.";
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length));
        assertEquals(message, TarUtils.exceptionMessage(buffer, 0, buffer.length, 0, buffer[0]));
    }

    /** Test formatLongOctalOrBinaryBytes method with valid input. */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidInput() {
        long value = 7654321L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String parsedValue;
        if ((buffer[0] & 0x80) == 0) {
            parsedValue = new String(buffer, 0, offset - 2); // remove trailing space and NUL
            assertEquals("017605631", parsedValue);
        } else {
            parsedValue = ByteUtils.bytesToHexString(buffer, 0, offset);
            assertEquals("0x003e05630576", parsedValue);
        }
    }

    /** Test formatLongOctalOrBinaryBytes method with value too large for binary buffer. */
    @Test
    public void testFormatLongOctalOrBinaryBytesValueTooLargeBinary() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String message = "Value " + value + " is too large for " + length + " byte field.";
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length));
        assertEquals(message, TarUtils.exceptionMessage(buffer, 0, buffer.length, 0, buffer[0]));
    }

    /** Test formatCheckSumOctalBytes method with valid input. */
    @Test
    public void testFormatCheckSumOctalBytesValidInput() {
        long checksum = 1234567890L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, buffer.length);
        String parsedChecksum = new String(buffer, 0, offset - 2); // remove trailing space and NUL
        assertEquals("012345677446\0", parsedChecksum);
    }

    /** Test computeCheckSum method with valid input. */
    @Test
    public void testComputeCheckSumValidInput() {
        byte[] buffer = "filename\0775\0020May 15 12:34:56\0filename\0".getBytes();
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        while (bb.hasRemaining()) {
            computedChecksum += bb.get() & 0xFF;
        }
        assertEquals(checksum, computedChecksum);
    }
}