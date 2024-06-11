package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ByteArrayInputStream;
import java.util.ByteArrayOutputStream;
import java.util.IOException;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test valid octal strings
        long[] testCases = new long[] { 010, 0127, 0277, 03777, 040101, 05555, 0777777, 0177777, 0123456, 07777777777L };
        for (long testCase : testCases) {
            byte[] input = new byte[] { (byte) 0, (byte) '0', (byte) testCase / 8, (byte) (testCase % 8) };
            int length = input.length;
            long result = TarUtils.parseOctal(input, 0, length);
            assertEquals(testCase, result);
        }

        // Test invalid octal strings (leading NUL)
        byte[] input1 = new byte[] { 0, 0, 1, '0' };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(input1, 0, input1.length));

        byte[] input2 = new byte[] { 0, 0, 0, '0' };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(input2, 0, input2.length));

        // Test invalid octal strings (non-octal characters)
        byte[] input3 = new byte[] { 0, 0, 1, 'X' };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(input3, 0, input3.length));

        byte[] input4 = new byte[] { 0, 0, 1, '0', 'A' };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(input4, 0, input4.length));

        // Test invalid octal strings (trailing non-NUL)
        byte[] input5 = new byte[] { 0, 0, 1, '0', ' ' };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(input5, 0, input5.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        // Test valid octal strings
        long[] testCases = new long[] { 010, 0127, 0277, 03777, 040101, 05555, 0777777, 0177777, 0123456, 07777777777L };
        for (long testCase : testCases) {
            byte[] input = new byte[] { (byte) 0, (byte) testCase / 8, (byte) (testCase % 8) };
            int length = input.length;
            long result = TarUtils.parseOctalOrBinary(input, 0, length);
            assertEquals(testCase, result);
        }

        // Test binary strings
        long[] testCasesBinary = new long[] { 0x0101010101010101L, 0xffffffffffffffffL, 0x8000000000000080L };
        for (long testCase : testCasesBinary) {
            byte[] input = new byte[] { (byte) 0x80, 0x01, (byte) (testCase >> 24), (byte) (testCase >> 16),
                    (byte) (testCase >> 8), (byte) testCase };
            int length = input.length;
            long result = TarUtils.parseOctalOrBinary(input, 0, length);
            assertEquals(testCase, result);
        }

        // Test octal and binary mixed
        byte[] input = new byte[] { 0x0, (byte) 0, 0x4, 0x1, 0x5, 0x5 };
        int length = input.length;
        long result = TarUtils.parseOctalOrBinary(input, 0, length);
        assertEquals(0x141, result);

        // Test invalid octal strings (leading NUL)
        byte[] input1 = new byte[] { 0, 0, 1, 0 };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(input1, 0, input1.length));

        byte[] input2 = new byte[] { 0, 0, 0, 0, 1, 0 };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(input2, 0, input2.length));

        // Test invalid octal strings (trailing NUL)
        byte[] input3 = new byte[] { 0, 0, 1, 0, 0, ' ' };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(input3, 0, input3.length));

        byte[] input4 = new byte[] { 0, 0, 1, 0, 0, 0, ' ' };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(input4, 0, input4.length));
    }

    @Test
    public void testParseBoolean() {
        byte[] inputTrue = new byte[] { 1 };
        byte[] inputFalse = new byte[] { 0 };

        assertEquals(true, TarUtils.parseBoolean(inputTrue, 0));
        assertEquals(false, TarUtils.parseBoolean(inputFalse, 0));
    }

    @Test
    public void testParseName() {
        byte[] input1 = new byte[] { 'h', 'e', 'l', 'l', 'o' };
        String expected1 = "hello";
        assertEquals(expected1, TarUtils.parseName(input1, 0, input1.length));

        byte[] input2 = new byte[] { 'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', '.', 't', 'a', 'r' };
        String expected2 = "Tar.file";
        assertEquals(expected2, TarUtils.parseName(input2, 0, input2.length));

        byte[] input3 = new byte[] { 0 };
        assertNull(TarUtils.parseName(input3, 0, input3.length));

        byte[] input4 = new byte[] { (byte) 0x7F, 'a' };
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(input4, 0, input4.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name1 = "hello";
        byte[] expected1 = new byte[] { 'h', 'e', 'l', 'l', 'o' };
        byte[] input1 = new byte[5];
        int result1 = TarUtils.formatNameBytes(name1, input1, 0, input1.length);
        assertEquals(result1, 5);
        assertArrayEquals(expected1, input1);

        String name2 = "Tar.file.";
        byte[] expected2 = new byte[] { 'T', 'a', 'r', '.', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', 0, '.' };
        byte[] input2 = new byte[13];
        int result2 = TarUtils.formatNameBytes(name2, input2, 0, input2.length);
        assertEquals(result2, 13);
        assertArrayEquals(expected2, input2);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long testCases = 0x0012345678L;
        byte[] expected = new byte[] { '0', '0', '0', '1', '2', '3', '4', '5', '6', '7' };
        byte[] input = new byte[10];

        TarUtils.formatUnsignedOctalString(testCases, input, 0, input.length);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x0012345678L;
        byte[] expected = new byte[] { '0', '0', '0', '1', '2', '3', '4', '5', '6', '7', ' ', 0 };
        byte[] input = new byte[12];

        TarUtils.formatOctalBytes(value, input, 0, input.length);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x001234567890ABCDEF01L;
        byte[] expected = new byte[] { '0', '0', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'E', 'F', '0', '1', ' ', 0 };
        byte[] input = new byte[20];

        TarUtils.formatLongOctalBytes(value, input, 0, input.length);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() throws IOException {
        long value = 0x001234567890ABCDEF01L;
        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        TarUtils.formatLongOctalOrBinaryBytes(value, expectedStream.toByteArray(), 0, expectedStream.size());
        byte[] expected = expectedStream.toByteArray();

        byte[] input = new byte[20];
        TarUtils.formatLongOctalOrBinaryBytes(value, input, 0, input.length);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] input = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
        long result = TarUtils.computeCheckSum(input);
        long checksum = 0;
        for (int i = 0; i < input.length; ++i) {
            checksum += TarUtils.BYTE_MASK & input[i];
        }
        assertEquals(checksum, result);
    }
}