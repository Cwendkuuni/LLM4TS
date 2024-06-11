package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_allNUL() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(0, result);
    }

    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = new byte[10];
        buffer[0] = '1';
        buffer[1] = '2';
        buffer[2] = '3';
        buffer[3] = '4';
        buffer[4] = '5';
        buffer[5] = '6';
        buffer[6] = '7';
        buffer[7] = '8';
        buffer[8] = '9';
        buffer[9] = (byte) ' ';

        int offset = 0;
        int length = 10;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(123456789, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidInput() {
        byte[] buffer = new byte[10];
        buffer[0] = 'a';

        int offset = 0;
        int length = 10;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_noNullChar() {
        byte[] buffer = new byte[10];
        buffer[0] = 'h';
        buffer[1] = 'e';
        buffer[2] = 'l';
        buffer[3] = 'l';
        buffer[4] = 'o';

        int offset = 0;
        int length = 10;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("hello", result);
    }

    @Test
    public void testParseName_withNullChar() {
        byte[] buffer = new byte[10];
        buffer[0] = 'h';
        buffer[1] = 'e';
        buffer[2] = 'l';
        buffer[3] = 'l';
        buffer[4] = 'o';
        buffer[5] = 0;
        buffer[6] = 'w';
        buffer[7] = 'o';
        buffer[8] = 'r';
        buffer[9] = 'l';

        int offset = 0;
        int length = 10;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes_nameFitsInBuffer() {
        String name = "hello";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(10, result);
        assertEquals("hello\0\0\0\0\0", new String(buf));
    }

    @Test
    public void testFormatNameBytes_nameDoesNotFitInBuffer() {
        String name = "hello world";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(5, result);
        assertEquals("hello", new String(buf));
    }

    @Test
    public void testFormatUnsignedOctalString_valueZero() {
        long value = 0;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals("0000000000", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString_valueNonZero() {
        long value = 123456789;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals("1234567781", new String(buffer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_valueDoesNotFitInBuffer() {
        long value = 123456789;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertEquals(10, result);
        assertEquals("1234567781 ", new String(buf));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;

        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertEquals(10, result);
        assertEquals("1234567781", new String(buf));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertEquals(10, result);
        assertEquals("1234567781 ", new String(buf));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = 'h';
        buf[1] = 'e';
        buf[2] = 'l';
        buf[3] = 'l';
        buf[4] = 'o';

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(532, result);
    }
}