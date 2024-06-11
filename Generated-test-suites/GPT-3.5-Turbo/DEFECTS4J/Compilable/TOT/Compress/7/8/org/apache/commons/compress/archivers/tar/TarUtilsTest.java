package org.apache.commons.compress.archivers.tar;
import org.junit.Test;

import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = "012345".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(53, result);
    }

    @Test
    public void testParseOctal_withLeadingSpaces() {
        byte[] buffer = "  01234".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(42, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidDigit() {
        byte[] buffer = "0123X5".getBytes();
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctal_trailingNull() {
        byte[] buffer = "0123\0005".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseOctal_trailingSpace() {
        byte[] buffer = "0123   ".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseName_validInput() {
        byte[] buffer = "Hello\000World".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Hello", result);
    }

    @Test
    public void testParseName_trailingNull() {
        byte[] buffer = "Hello\000World\000".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Hello", result);
    }

    @Test
    public void testFormatNameBytes_validInput() {
        String name = "Example";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(offset + name.length(), result);
        assertEquals("Example\0\0\0\0\0", new String(buf));
    }

    @Test
    public void testFormatNameBytes_inputExceedsBuffer() {
        String name = "VeryLongName";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(offset + length, result);
        assertEquals("VeryLongNa", new String(buf));
    }

    @Test
    public void testFormatUnsignedOctalString_zeroValue() {
        long value = 0;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals("00000", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString_validValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals("00123", new String(buffer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_valueTooBig() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_validValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertEquals("00123 ", new String(buffer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_valueTooBig() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes_validValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertEquals("00123 ", new String(buffer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_valueTooBig() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_validValue() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertEquals("00123 ", new String(buffer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_valueTooBig() {
        long value = 12345;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4, 5};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(15, result);
    }

}