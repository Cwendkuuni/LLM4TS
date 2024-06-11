package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_withValidOctalString() {
        byte[] buffer = "0123\0".getBytes();
        int offset = 0;
        int length = buffer.length;
        long expected = 83;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_withTrailingSpace() {
        byte[] buffer = "1234      ".getBytes();
        int offset = 0;
        int length = buffer.length;
        long expected = 668;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_withInvalidCharacter() {
        byte[] buffer = "1234A0".getBytes();
        int offset = 0;
        int length = buffer.length;
        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 4 in '1234A0'", e.getMessage());
        }
    }

    @Test
    public void testParseName_withValidName() {
        byte[] buffer = "test_file\0".getBytes();
        int offset = 0;
        int length = buffer.length;
        String expected = "test_file";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName_withNoNullTermination() {
        byte[] buffer = "file_without_null_termination".getBytes();
        int offset = 0;
        int length = buffer.length;
        String expected = "file_without_null_termination";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_withValidName() {
        String name = "test_name";
        byte[] buffer = new byte[20];
        int offset = 0;
        int length = buffer.length;
        int expectedOffset = 10;
        int resultOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expectedOffset, resultOffset);
        String result = new String(buffer, offset, length).trim();
        assertEquals(name, result);
    }

    @Test
    public void testFormatNameBytes_withLongName() {
        String name = "this_is_a_very_long_name_that_needs_to_be_truncated";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        int expectedOffset = 10;
        int resultOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expectedOffset, resultOffset);
        String expected = "this_is_a_";
        String result = new String(buffer, offset, length).trim();
        assertEquals(expected, result);
    }

    @Test
    public void testFormatUnsignedOctalString_withZeroValue() {
        long value = 0;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "0000\0";
        String result = new String(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatUnsignedOctalString_withNonZeroValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "0012\0";
        String result = new String(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatUnsignedOctalString_withValueExceedingBufferLength() {
        long value = 123456;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        try {
            TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("123456=362100 will not fit in octal number buffer of length 5", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes_withNonZeroValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        int expectedOffset = 5;
        int resultOffset = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(expectedOffset, resultOffset);
        String expected = "00123 ";
        String result = new String(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalBytes_withNonZeroValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        int expectedOffset = 5;
        int resultOffset = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(expectedOffset, resultOffset);
        String expected = "000123";
        String result = new String(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes_withNonZeroValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        int expectedOffset = 5;
        int resultOffset = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(expectedOffset, resultOffset);
        String expected = "00123 ";
        String result = new String(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum_withValidBuffer() {
        byte[] buffer = "test_buffer".getBytes();
        long expected = 1202;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum_withEmptyBuffer() {
        byte[] buffer = new byte[0];
        long expected = 0;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

}