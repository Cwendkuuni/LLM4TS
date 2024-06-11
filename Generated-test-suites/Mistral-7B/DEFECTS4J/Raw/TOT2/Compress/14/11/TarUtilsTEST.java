package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Test;

/**
 * TarUtilsTest class for testing TarUtils methods.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', ' ', '0'};
        long expectedResult = 017550L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctal method with invalid input - missing trailing space/NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_missingTrailingSpaceOrNUL() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctal method with invalid input - invalid byte detected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', 'A', '5', '0', ' '};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid input - octal number.
     */
    @Test
    public void testParseOctalOrBinary_validOctal() {
        byte[] buffer = new byte[]{'0', '1', '7', '7', '5', '5', '0', ' '};
        long expectedResult = 0177550L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctalOrBinary method with valid input - binary number.
     */
    @Test
    public void testParseOctalOrBinary_validBinary() {
        byte[] buffer = new byte[]{'0', '\u0080', '1', '7', '7', '5', '5', '\u0000'};
        long expectedResult = 12817755L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid input - missing trailing space/NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinary_missingTrailingSpaceOrNUL() {
        byte[] buffer = new byte[]{'0', '\u0080', '1', '7', '7', '5', '5'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with invalid input - binary number exceeding long limit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinary_binaryNumberExceedingLongLimit() {
        byte[] buffer = new byte[]{'0', '\u0080', '1', '7', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBoolean_validInput() {
        byte[] buffer = new byte[]{'1'};
        boolean expectedResult = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBoolean_invalidInput() {
        byte[] buffer = new byte[]{'0'};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName_validInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', '\u0000'};
        String expectedResult = "tar.txt";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseName method with invalid input - missing trailing null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseName_missingTrailingNull() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 't', 'x'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes_validInput() {
        String name = "test.txt";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        String formattedName = new String(buffer, 0, offset);
        assertEquals("test.txt", formattedName);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString_validInput() {
        long value = 017550L;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("01234567550", new String(buffer, 0, 12));
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytes_validInput() {
        long value = 017550L;
        byte[] buffer = new byte[14];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(14, offset);
        String formattedName = new String(buffer, 0, offset);
        assertEquals("01234567550\u0020", formattedName);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input - octal number.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes_validOctal() {
        long value = 0177550L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        String formattedName = new String(buffer, 0, offset);
        assertEquals("0177550", formattedName);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input - binary number.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes_validBinary() {
        long value = 12817755L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        String formattedName = new String(buffer, 0, offset);
        assertEquals("\u0080\u0080\u0080\u0080\u0080\u0080\u0080\u0080\u0080\u0080\u0080\u0080", formattedName);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with invalid input - invalid length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalOrBinaryBytes_invalidLength() {
        long value = 0177550L;
        byte[] buffer = new byte[9];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSum_validInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.'};
        long expectedCheckSum = 1691635051L;
        long calculatedCheckSum = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedCheckSum, calculatedCheckSum);
    }

    /**
     * Test parseOctal method with empty buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_emptyBuffer() {
        byte[] buffer = new byte[0];
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with empty buffer.
     */
    @Test
     public void testParseOctalOrBinary_emptyBuffer() {
         byte[] buffer = new byte[0];
         TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
     }
}