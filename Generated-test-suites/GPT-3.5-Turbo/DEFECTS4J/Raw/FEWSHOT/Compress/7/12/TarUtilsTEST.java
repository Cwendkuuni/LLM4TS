package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test input with leading spaces and trailing spaces
        assertEquals(0, TarUtils.parseOctal("       ".getBytes(), 0, 7));
        
        // Test input with leading zeroes and trailing spaces
        assertEquals(0, TarUtils.parseOctal("0000000 ".getBytes(), 0, 7));
        
        // Test input with all octal digits
        assertEquals(1234567, TarUtils.parseOctal("1234567".getBytes(), 0, 7));
        
        // Test input with non-octal digit
        try {
            TarUtils.parseOctal("123456g".getBytes(), 0, 7);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 6 in '123456g'", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        // Test input with only one character
        assertEquals("a", TarUtils.parseName("a\0".getBytes(), 0, 2));
        
        // Test input with multiple characters
        assertEquals("abc", TarUtils.parseName("abc\0".getBytes(), 0, 4));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        
        // Test input with name shorter than buffer
        assertEquals(10, TarUtils.formatNameBytes("abc", buffer, 0, 10));
        assertEquals("abc\0\0\0\0\0\0\0\0\0", new String(buffer));
        
        // Test input with name longer than buffer
        assertEquals(4, TarUtils.formatNameBytes("abcd", buffer, 0, 4));
        assertEquals("abcd", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        
        // Test input with value 0
        TarUtils.formatUnsignedOctalString(0, buffer, 0, 10);
        assertEquals("0000000000", new String(buffer));
        
        // Test input with value greater than buffer size
        try {
            TarUtils.formatUnsignedOctalString(123456789012345l, buffer, 0, 10);
        } catch (IllegalArgumentException e) {
            assertEquals("123456789012345=351746623131 would not fit in octal number buffer of length 10", e.getMessage());
        }
        
        // Test input with value less than buffer size
        TarUtils.formatUnsignedOctalString(12345, buffer, 0, 10);
        assertEquals("0000015431", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        
        // Test input with value 0
        assertEquals(10, TarUtils.formatOctalBytes(0, buffer, 0, 10));
        assertEquals("000000000\0", new String(buffer));
        
        // Test input with value greater than buffer size
        try {
            TarUtils.formatOctalBytes(123456789012345l, buffer, 0, 10);
        } catch (IllegalArgumentException e) {
            assertEquals("123456789012345=351746623131 will not fit in octal number buffer of length 10", e.getMessage());
        }
        
        // Test input with value less than buffer size
        assertEquals(10, TarUtils.formatOctalBytes(12345, buffer, 0, 10));
        assertEquals("000001543 \0", new String(buffer));
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];
        
        // Test input with value 0
        assertEquals(10, TarUtils.formatLongOctalBytes(0, buffer, 0, 10));
        assertEquals("000000000 ", new String(buffer));
        
        // Test input with value greater than buffer size
        try {
            TarUtils.formatLongOctalBytes(123456789012345l, buffer, 0, 10);
        } catch (IllegalArgumentException e) {
            assertEquals("123456789012345=351746623131 will not fit in octal number buffer of length 10", e.getMessage());
        }
        
        // Test input with value less than buffer size
        assertEquals(10, TarUtils.formatLongOctalBytes(12345, buffer, 0, 10));
        assertEquals("000001543 ", new String(buffer));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];
        
        // Test input with value 0
        assertEquals(10, TarUtils.formatCheckSumOctalBytes(0, buffer, 0, 10));
        assertEquals("000000000\0", new String(buffer));
        
        // Test input with value greater than buffer size
        try {
            TarUtils.formatCheckSumOctalBytes(123456789012345l, buffer, 0, 10);
        } catch (IllegalArgumentException e) {
            assertEquals("123456789012345=351746623131 will not fit in octal number buffer of length 10", e.getMessage());
        }
        
        // Test input with value less than buffer size
        assertEquals(10, TarUtils.formatCheckSumOctalBytes(12345, buffer, 0, 10));
        assertEquals("000001543 \0", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        // Test input with all bytes set to 0
        byte[] buffer = new byte[10];
        assertEquals(0, TarUtils.computeCheckSum(buffer));
        
        // Test input with all bytes set to 1
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 1;
        }
        assertEquals(10, TarUtils.computeCheckSum(buffer));
        
        // Test input with a mix of bytes
        buffer[0] = 10;
        buffer[1] = 20;
        buffer[2] = 30;
        buffer[3] = 40;
        buffer[4] = 50;
        buffer[5] = 60;
        buffer[6] = 70;
        buffer[7] = 80;
        buffer[8] = 90;
        buffer[9] = 100;
        assertEquals(550, TarUtils.computeCheckSum(buffer));
    }
}