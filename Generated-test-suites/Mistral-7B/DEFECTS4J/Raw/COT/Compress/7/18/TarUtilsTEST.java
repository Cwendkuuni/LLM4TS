package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        int offset = 0;
        int length = buffer.length;

        long expectedResult = 017550L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctal method with invalid input (non-octal digit).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', 'x', '0'};
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'.'.byteValue(), 'm', 'y', ' ', 'J', 'u', 'n', 'i', 't', 's', '.', 't', 'a', 'r',
            '.', 'g', 'z', ' ', 'I', 'n', 'f', 'o', 'r', 'm', 'a', 't', 'i', 'c', 's'};
        int offset = 0;
        int length = buffer.length;

        String expectedResult = "my.junit.tar.gz Informatics";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseName method with trailing null byte.
     */
    @Test
    public void testParseName_TrailingNull() {
        byte[] buffer = new byte[]{'m', 'y', '.', 'j', 'u', 'n', 'i', 't', 's', '.', 't', 'a', 'r', '.', 'g', 'z', ' ',
            'I', 'n', 'f', 'o', 'r', 'm', 'a', 't', 'i', 'c', 's', '.'};
        int offset = 0;
        int length = buffer.length;

        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("my.junit.tar.gz Informatics", result);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "my.junit.tar.gz Informatics";
        byte[] buffer = new byte[32];
        int offset = 0;
        int length = name.length();

        int expectedResult = formatNameBytes(name, buffer, offset, length);
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        assertEquals(expectedResult, result);
        assertArrayEquals(buffer, expectedBuffer(name, length));
    }

    /**
     * Test formatNameBytes method with name longer than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "my.junit.tar.gz Informatics12345";
        byte[] buffer = new byte[32];
        int offset = 0;
        int length = name.length();

        TarUtils.formatNameBytes(name, buffer, offset, length);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 1234567;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        byte[] expectedBuffer = new byte[]{
            (byte) '0', (byte) '1', (byte) '0', (byte) '0',
            (byte) '0', (byte) '1', (byte) '7', (byte) '7'};

        assertArrayEquals(expectedBuffer, buffer);
    }

    /**
     * Test formatUnsignedOctalString method with value longer than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueLongerThanBuffer() {
        long value = 1234567890;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

        byte[] expectedBuffer = new byte[]{
            (byte) '0', (byte) '1', (byte) '0', (byte) '0', (byte) '1',
            (byte) '0', (byte) '1', (byte) '7', (byte) ' ', (byte) '0'};

        assertArrayEquals(expectedBuffer, buffer);
        assertEquals(offset + 10, result);
    }

    /**
     * Test formatOctalBytes method with value longer than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_ValueLongerThanBuffer() {
        long value = 1234567890;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};

        long expectedResult = 1231L;
        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctal with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_NullInput() {
        TarUtils.parseOctal(null, 0, 0);
    }

    /**
     * Test parseName with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseName_NullInput() {
        TarUtils.parseName(null, 0, 0);
    }

    /**
     * Test formatNameBytes with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytes_NullName() {
        TarUtils.formatNameBytes(null, new byte[10], 0, 10);
    }

    /**
     * Test parseName with empty string.
     */
    @Test
    public void testParseName_EmptyString() {
        byte[] buffer = new byte[0];
        int offset = 0;
        int length = 0;

        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("", result);
    }

    /**
     * Test formatNameBytes with boundary case.
     */
    @Test
    public void testFormatNameBytes_BoundaryCase() {
        String name = "name";
        byte[] buffer = new byte[1];
        int offset = 0;
        int length = name.length();

        int expectedResult = formatNameBytes(name, buffer, offset, length);
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        assertEquals(expectedResult, result);
        assertEquals(1, buffer.length);
    }

    // Add test methods for other methods in TarUtils as needed

    private byte[] expectedBuffer(String name, int length) {
        String encodedName = TarUtils.parseName(new byte[0], 0, 0);
        TarUtils.formatNameBytes(encodedName, new byte[length], 0, length);

        return new byte[length];
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }
}