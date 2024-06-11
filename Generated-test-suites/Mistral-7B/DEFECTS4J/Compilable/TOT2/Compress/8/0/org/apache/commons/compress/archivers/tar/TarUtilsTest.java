package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {
    // Test for parseOctal method
    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7'};
        long expected = 0175577L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '8', '7'}; // '8' is an invalid byte
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_MissingTrailingSpaceOrNul() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7'}; // missing trailing space or NUL
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    // Test for parseName method
    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'.' , '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', 0};
        String expected = "tar.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseName_EmptyInput() {
        byte[] buffer = new byte[0];
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("", actual);
    }

    // Test for formatNameBytes method
    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "tar.gz";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        assertEquals(name, actual);
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "ThisIsALongNameThatIsLongerThanTheBuffer";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        // The output will be truncated, so we can't check for exact equality, only that the first part is correct
        String expected = "ThisIsALongNameTh";
        assertEquals(expected, actual);
    }

    // Test for formatUnsignedOctalString method
    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 1234567L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, buffer.length);
        String expected = "0123456";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatUnsignedOctalString_ValueLongerThanBuffer() {
        long value = 123456789012345L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, buffer.length);
        String expected = "0123456..."; // The output will be truncated, so we can't check for exact equality, only that the first part is correct
        assertEquals(expected, actual);
    }

    // Test for formatOctalBytes method
    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 1234567L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        String expected = "0123456 ";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatOctalBytes_ValueLongerThanBuffer() {
        long value = 123456789012345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String actual = new String(buffer, 0, offset);
        String expected = "0123456..."; // The output will be truncated, so we can't check for exact equality, only that the first part is correct
        assertEquals(expected, actual);
    }

    // Test for computeCheckSum method
    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long expected = 15 * 256 * 256 * 256 + 2 * 256 * 256 + 3 * 256 + 4 + 5;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}