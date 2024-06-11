package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = {48, 49, 50, 51}; // ASCII representation of "0123"
        int offset = 0;
        int length = 4;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(123L, result);
    }

    @Test
    public void testParseOctal_missingTrailingSpace() {
        byte[] buffer = {49, 48, 49, 49}; // ASCII representation of "1011"
        int offset = 0;
        int length = 4;

        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 49 at offset 0 in '1011' len=4", e.getMessage());
        }
    }

    @Test
    public void testParseName_validInput() {
        byte[] buffer = {65, 66, 67, 0}; // ASCII representation of "ABC"
        int offset = 0;
        int length = 4;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("ABC", result);
    }

    @Test
    public void testParseName_trailingNull() {
        byte[] buffer = {65, 66, 67, 68, 0}; // ASCII representation of "ABCD"
        int offset = 0;
        int length = 4;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes_validInput() {
        String name = "ABC";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[]{65, 66, 67, 0, 0}, buf);
    }

    @Test
    public void testFormatNameBytes_truncatedOutput() {
        String name = "ABCDEF";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[]{65, 66, 67, 0, 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString_validInput() {
        long value = 123L;
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(new byte[]{49, 50, 51}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString_truncatedOutput() {
        long value = 123456L;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        try {
            TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("123456=3611000 will not fit in octal number buffer of length 5", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes_validInput() {
        long value = 123L;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[]{49, 50, 51, 32, 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes_validInput() {
        long value = 123L;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;

        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertEquals(4, result);
        assertArrayEquals(new byte[]{49, 50, 51, 32}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes_validInput() {
        long value = 123L;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[]{49, 50, 51, 0, 32}, buf);
    }

    @Test
    public void testComputeCheckSum_validInput() {
        byte[] buf = {65, 66, 67}; // ASCII representation of "ABC"

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(198L, result);
    }
}