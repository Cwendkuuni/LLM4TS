package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 48; // ASCII value for '0'
        buffer[1] = 50; // ASCII value for '2'
        buffer[2] = 53; // ASCII value for '5'
        buffer[3] = 0; // Trailing space or NUL
        
        assertEquals(25, TarUtils.parseOctal(buffer, 0, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[10];
        buffer[0] = 48; // ASCII value for '0'
        buffer[1] = 50; // ASCII value for '2'
        buffer[2] = 53; // ASCII value for '5'
        buffer[3] = 56; // ASCII value for '8', which is an invalid byte
        
        TarUtils.parseOctal(buffer, 0, 4);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 102; // ASCII value for 'f'
        buffer[1] = 111; // ASCII value for 'o'
        buffer[2] = 111; // ASCII value for 'o'
        buffer[3] = 0; // Trailing null
        
        assertEquals("foo", TarUtils.parseName(buffer, 0, 4));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        String name = "foo";
        int offset = 0;
        int length = 10;
        
        int newOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
        
        assertEquals("foo", new String(buffer, offset, newOffset - offset));
        assertEquals(3, newOffset - offset);
        assertEquals(0, buffer[newOffset]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        long value = 25;
        int offset = 0;
        int length = 10;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertEquals("0000000025", new String(buffer, offset, length));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_valueTooLarge() {
        byte[] buffer = new byte[10];
        long value = 9999999999L; // Value that cannot fit in the buffer
        int offset = 0;
        int length = 10;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        long value = 25;
        int offset = 0;
        int length = 10;
        
        int newOffset = TarUtils.formatOctalBytes(value, buffer, offset, length);
        
        assertEquals("0000000025 ", new String(buffer, offset, newOffset - offset));
        assertEquals(10, newOffset - offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];
        long value = 25;
        int offset = 0;
        int length = 10;
        
        int newOffset = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        
        assertEquals("0000000025", new String(buffer, offset, newOffset - offset));
        assertEquals(10, newOffset - offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];
        long value = 25;
        int offset = 0;
        int length = 10;
        
        int newOffset = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        
        assertEquals("0000000025 ", new String(buffer, offset, newOffset - offset));
        assertEquals(10, newOffset - offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = 5;
        buffer[1] = 6;
        buffer[2] = 7;
        buffer[3] = 8;
        buffer[4] = 9;
        buffer[5] = 10;
        buffer[6] = 11;
        buffer[7] = 12;
        buffer[8] = 13;
        buffer[9] = 14;

        assertEquals(80, TarUtils.computeCheckSum(buffer));
    }
}