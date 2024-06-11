package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TarUtilsTest class is used to test the static methods of TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal string.
     */
    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', (byte) 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175577L, result);
    }

    /**
     * Test parseOctal method with leading spaces.
     */
    @Test
    public void testParseOctalLeadingSpaces() {
        byte[] buffer = new byte[]{' ', '0', '7', '5', '5', '7', '7', (byte) 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175577L, result);
    }

    /**
     * Test parseOctal method with trailing spaces.
     */
    @Test
    public void testParseOctalTrailingSpaces() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', ' ', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175577L, result);
    }

    /**
     * Test parseOctal method with invalid input (non-octal digit).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', 'A', '5', '5', '7', '7', '7', (byte) 0};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseName method with valid entry name.
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{'.' , 'a', '.', 'd', 'i', 'r', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("dir.tar.gz", result);
    }

    /**
     * Test parseName method with leading spaces.
     */
    @Test
    public void testParseNameLeadingSpaces() {
        byte[] buffer = new byte[]{' ', '.', 'a', '.', 'd', 'i', 'r', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("dir.tar.gz", result);
    }

    /**
     * Test parseName method with trailing spaces.
     */
    @Test
    public void testParseNameTrailingSpaces() {
        byte[] buffer = new byte[]{'.', 'a', '.', 'd', 'i', 'r', '.', 't', 'a', 'r', '.', 'g', 'z', ' ', ' '};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("dir.tar.gz", result);
    }

    /**
     * Test formatNameBytes method with valid entry name.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "dir.tar.gz";
        byte[] buffer = new byte[32];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'d', 'i', 'r', '.', 't', 'a', 'r', '.', 'g', 'z'}, buffer, 0, name.length());
    }

    /**
     * Test formatNameBytes method with entry name larger than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesBufferTooSmall() {
        String name = "dir.tar.gz.ext1.ext2";
        byte[] buffer = new byte[10];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
    }

    /**
     * Test formatUnsignedOctalString method with valid unsigned octal number.
     */
    @Test
    public void testFormatUnsignedOctalStringValid() {
        long value = 0175577L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '1', '7', '5', '5', '7', '7', '7'}, buffer, 0, 8);
    }

    /**
     * Test formatOctalBytes method with valid unsigned octal number.
     */
    @Test
    public void testFormatOctalBytesValid() {
        long value = 0175577L;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '1', '7', '5', '5', '7', '7', '7', ' ', ' '}, buffer, 0, 11);
    }

    /**
     * Test formatOctalBytes method with value and trailer too large for buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesValueTooLarge() {
        long value = 01111111111111111111111111111111111L;
        byte[] buffer = new byte[11];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }
}