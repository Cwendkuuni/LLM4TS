package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_typicalCase() {
        byte[] buffer = "123".getBytes();
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_nullFound() {
        byte[] buffer = "123\0".getBytes();
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctal_trailingSpaceFound() {
        byte[] buffer = "123 ".getBytes();
        long expected = 123;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidDigit() {
        byte[] buffer = "123A".getBytes();
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_typicalCase() {
        byte[] buffer = "example".getBytes();
        String expected = "example";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName_nullFound() {
        byte[] buffer = "example\0".getBytes();
        String expected = "example";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_typicalCase() {
        String name = "example";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = 10;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expected, result);

        for (int i = 0; i < length; i++) {
            if (i < name.length()) {
                assertEquals((byte) name.charAt(i), buffer[i]);
            } else {
                assertEquals((byte) 0, buffer[i]);
            }
        }
    }

    @Test
    public void testFormatNameBytes_nameLongerThanBuffer() {
        String name = "example";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        int expected = 5;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expected, result);

        for (int i = 0; i < length; i++) {
            if (i < name.length()) {
                assertEquals((byte) name.charAt(i), buffer[i]);
            } else {
                assertEquals((byte) 0, buffer[i]);
            }
        }
    }

    @Test
    public void testFormatNameBytes_bufferLongerThanName() {
        String name = "example";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int expected = 10;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expected, result);

        for (int i = 0; i < length; i++) {
            if (i < name.length()) {
                assertEquals((byte) name.charAt(i), buffer[i]);
            } else {
                assertEquals((byte) 0, buffer[i]);
            }
        }
    }

    @Test
    public void testFormatUnsignedOctalString_typicalCase() {
        long value = 123456;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        String expected = "000123456";
        String result = new String(buffer);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_valueTooLarge() {
        long value = 99999999;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_typicalCase() {
        long value = 123456;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        int expected = 8;
        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(expected, result);

        String expectedString = "00123456 ";
        String resultString = new String(buffer);
        assertEquals(expectedString, resultString);
    }

    @Test
    public void testFormatLongOctalBytes_typicalCase() {
        long value = 123456;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        int expected = 8;
        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(expected, result);

        String expectedString = "00123456 ";
        String resultString = new String(buffer);
        assertEquals(expectedString, resultString);
    }

    @Test
    public void testFormatCheckSumOctalBytes_typicalCase() {
        long value = 123456;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        int expected = 8;
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(expected, result);

        String expectedString = "00123456  ";
        String resultString = new String(buffer);
        assertEquals(expectedString, resultString);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4, 5};
        long expected = 15;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}