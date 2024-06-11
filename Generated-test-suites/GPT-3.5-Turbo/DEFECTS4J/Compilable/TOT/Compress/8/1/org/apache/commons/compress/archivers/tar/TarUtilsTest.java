package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] { ' ', '1', '2', '3', ' ', ' ', ' ', '4', '5', ' ', '\0' };
        int offset = 0;
        int length = 10;
        
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(0123, result);
    }

    @Test
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[] { ' ', '1', '2', 'x', ' ', ' ', ' ', '4', '5', ' ', '\0' };
        int offset = 0;
        int length = 10;
        
        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 120 at offset 3 in ' 12x   45 ' len=10", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] { 'J', 'a', 'v', 'a', ' ', 'C', 'l', 'a', 's', 's', '\0', ' ', ' ', ' ', ' ' };
        int offset = 0;
        int length = 10;
        
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals("Java Class", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "JUnit4";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertEquals(10, newOffset);
        assertEquals("JUnit4" + "\0\0\0\0\0", new String(buf));
    }

    @Test
    public void testFormatNameBytesWithLongName() {
        String name = "LongName";
        byte[] buf = new byte[7];
        int offset = 0;
        int length = 7;
        
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertEquals(7, newOffset);
        assertEquals("LongNam", new String(buf));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertEquals("00123", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalStringWithInvalidValue() {
        long value = 123456789;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        
        try {
            TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("123456789=7267464251 will not fit in octal number buffer of length 5", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        
        int newOffset = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertEquals(5, newOffset);
        assertEquals("000123 ", new String(buf));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        
        int newOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertEquals(4, newOffset);
        assertEquals("00012", new String(buf));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        
        int newOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertEquals(5, newOffset);
        assertEquals("000123 ", new String(buf));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[] { 1, 2, 3, -1, -2, -3 };

        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(6, result);
    }

}