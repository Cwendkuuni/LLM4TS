package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TarUtilsTest class provides test cases for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] testBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '0'};
        long expectedResult = 012345675L;
        long actualResult = TarUtils.parseOctal(testBuffer, 0, testBuffer.length);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseOctal method with invalid input - missing trailing NUL or space.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInputMissingTrailingSpaceOrNUL() {
        byte[] testBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        TarUtils.parseOctal(testBuffer, 0, testBuffer.length);
    }

    /**
     * Test parseOctal method with invalid input - invalid byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInputInvalidByte() {
        byte[] testBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8', '9'};
        TarUtils.parseOctal(testBuffer, 0, testBuffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid input - octal.
     */
    @Test
    public void testParseOctalOrBinaryValidInputOctal() {
        byte[] testBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long expectedResult = 01234567L;
        long actualResult = TarUtils.parseOctalOrBinary(testBuffer, 0, testBuffer.length);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseOctalOrBinary method with valid input - binary.
     */
    @Test
    public void testParseOctalOrBinaryValidInputBinary() {
        byte[] testBuffer = new byte[]{(byte) 0x81, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        long expectedResult = 2561020304050607L;
        long actualResult = TarUtils.parseOctalOrBinary(testBuffer, 0, testBuffer.length);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseOctalOrBinary method with invalid input - missing trailing NUL or space.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidInputMissingTrailingSpaceOrNUL() {
        byte[] testBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        TarUtils.parseOctalOrBinary(testBuffer, 0, testBuffer.length);
    }

    /**
     * Test parseOctalOrBinary method with invalid input - binary number exceeds maximum signed long value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidInputBinaryTooLarge() {
        byte[] testBuffer = new byte[]{(byte) 0x80, (byte) 0x80, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        TarUtils.parseOctalOrBinary(testBuffer, 0, testBuffer.length);
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanValidInput() {
        byte[] testBuffer = new byte[]{(byte) 0x01};
        boolean expectedResult = true;
        boolean actualResult = TarUtils.parseBoolean(testBuffer, 0);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test
    public void testParseBooleanInvalidInput() {
        byte[] testBuffer = new byte[]{0};
        TarUtils.parseBoolean(testBuffer, 0);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseNameValidInput() {
        byte[] testBuffer = new byte[]{'t', 'a', 'r', '.', 'a', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String expectedResult = "tar.archive.gz";
        String actualResult = TarUtils.parseName(testBuffer, 0, testBuffer.length);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseName method with invalid input - missing trailing null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalidInputMissingTrailingNUL() {
        byte[] testBuffer = new byte[]{'t', 'a', 'r', '.', 'a', 'r'};
        TarUtils.parseName(testBuffer, 0, testBuffer.length);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String testName = "testFile.txt";
        byte[] expectedBuffer = new byte[]{'t', 'e', 's', 't', 'F', 'i', 'l', 'e', '.', 't', 'x', 't'};
        byte[] buffer = new byte[expectedBuffer.length];
        int offset = TarUtils.formatNameBytes(testName, buffer, 0, buffer.length);
        assertArrayEquals(expectedBuffer, buffer);
        assertEquals(offset, buffer.length);
    }

    /**
     * Test formatNameBytes method with invalid input - name longer than buffer.
     */
    @Test
    public void testFormatNameBytesInvalidInputLongerName() {
        String testName = "testFile1234567890.txt";
        byte[] buffer = new byte[10];
        TarUtils.formatNameBytes(testName, buffer, 0, buffer.length);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long testValue = 1234567;
        byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        byte[] buffer = new byte[expectedBuffer.length];
        TarUtils.formatUnsignedOctalString(testValue, buffer, 0, buffer.length);
        assertArrayEquals(expectedBuffer, buffer);
    }

    /**
     * Test formatUnsignedOctalString method with invalid input - value too large for buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidInputValueTooLarge() {
        long testValue = 1234567890;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(testValue, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytesValidInput() {
        long testValue = 1234567;
        byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', ' ', '0'};
        byte[] buffer = new byte[expectedBuffer.length];
        int offset = TarUtils.formatOctalBytes(testValue, buffer, 0, buffer.length);
        assertArrayEquals(expectedBuffer, buffer);
        assertEquals(offset, buffer.length);
    }

    /**
     * Test formatOctalBytes method with invalid input - value too large for buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesInvalidInputValueTooLarge() {
        long testValue = 1234567890;
        byte[] buffer = new byte[8];
        TarUtils.formatOctalBytes(testValue, buffer, 0, buffer.length);
    }
}