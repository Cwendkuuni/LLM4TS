package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = "123".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseOctal_NullTerminator() {
        byte[] buffer = "123\0".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseOctal_TrailingSpace() {
        byte[] buffer = "123 ".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidDigit() {
        byte[] buffer = "12a3".getBytes();
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = "test".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("test", result);
    }

    @Test
    public void testParseName_NullTerminator() {
        byte[] buffer = "test\0".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes_ShortName() {
        String name = "test";
        byte[] buf = new byte[10];
        int result = TarUtils.formatNameBytes(name, buf, 0, name.length());
        assertEquals(10, result);
        assertEquals('t', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
        assertEquals('t', buf[3]);
        assertEquals(0, buf[4]);
        assertEquals(0, buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(0, buf[7]);
        assertEquals(0, buf[8]);
        assertEquals(0, buf[9]);
    }

    @Test
    public void testFormatNameBytes_LongName() {
        String name = "verylongname";
        byte[] buf = new byte[10];
        int result = TarUtils.formatNameBytes(name, buf, 0, name.length());
        assertEquals(10, result);
        assertEquals('v', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('r', buf[2]);
        assertEquals('y', buf[3]);
        assertEquals('l', buf[4]);
        assertEquals('o', buf[5]);
        assertEquals('n', buf[6]);
        assertEquals('g', buf[7]);
        assertEquals('n', buf[8]);
        assertEquals('a', buf[9]);
    }

    @Test
    public void testFormatNameBytes_ShortName_Truncated() {
        String name = "test";
        byte[] buf = new byte[3];
        int result = TarUtils.formatNameBytes(name, buf, 0, name.length());
        assertEquals(3, result);
        assertEquals('t', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);
    }

    @Test
    public void testFormatNameBytes_LongName_Truncated() {
        String name = "verylongname";
        byte[] buf = new byte[5];
        int result = TarUtils.formatNameBytes(name, buf, 0, name.length());
        assertEquals(5, result);
        assertEquals('v', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('r', buf[2]);
        assertEquals('y', buf[3]);
        assertEquals('l', buf[4]);
    }

    @Test
    public void testFormatUnsignedOctalString_ZeroValue() {
        long value = 0;
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('0', buf[2]);
        assertEquals('0', buf[3]);
        assertEquals('0', buf[4]);
    }

    @Test
    public void testFormatUnsignedOctalString_NonZeroValue() {
        long value = 123;
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('1', buf[2]);
        assertEquals('7', buf[3]);
        assertEquals('3', buf[4]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueTooLarge() {
        long value = 5678;
        byte[] buf = new byte[4];
        TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int result = TarUtils.formatOctalBytes(value, buf, 0, buf.length);
        assertEquals(5, result);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('1', buf[2]);
        assertEquals('7', buf[3]);
        assertEquals(' ', buf[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int result = TarUtils.formatLongOctalBytes(value, buf, 0, buf.length);
        assertEquals(5, result);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('1', buf[2]);
        assertEquals('7', buf[3]);
        assertEquals(' ', buf[4]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);
        assertEquals(5, result);
        assertEquals('0', buf[0]);
        assertEquals('0', buf[1]);
        assertEquals('1', buf[2]);
        assertEquals('7', buf[3]);
        assertEquals(' ', buf[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = "test".getBytes();
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(448, result);
    }

}