
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        // Test case where buffer length is less than 2 bytes
        byte[] buf = {1};
        assertEquals(0, TarUtils.parseOctal(buf, 0, 1));
        
        // Test case where buffer contains all NULs
        byte[] buf1 = {0, 0, 0};
        assertEquals(0, TarUtils.parseOctal(buf1, 0, 3));
        
        // Test case where buffer contains valid octal string
        byte[] buf2 = {'3', '6', '0', '1', 0};
        assertEquals(361, TarUtils.parseOctal(buf2, 0, 4));
        
        // Test case where invalid byte is detected
        byte[] buf3 = {'3', '8', '7', '5', 0};
        try {
            TarUtils.parseOctal(buf3, 0, 4);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 5 at offset 3 in '3875' len=4", e.getMessage());
        }
        
        // Test case where buffer contains trailing space before NUL
        byte[] buf4 = {'3', '3', ' ', 0, 0};
        assertEquals(33, TarUtils.parseOctal(buf4, 0, 5));
    }
    
    @Test
    public void testParseName() {
        // Test case where buffer contains no NULs
        byte[] buf = {'f', 'o', 'o', ' ', 'b', 'a', 'r'};
        assertEquals("foo ", TarUtils.parseName(buf, 0, 5));
        
        // Test case where buffer contains NULs
        byte[] buf1 = {'f', 'o', 'o', 0, 'b', 'a', 'r'};
        assertEquals("foo", TarUtils.parseName(buf1, 0, 7));
    }
    
    @Test
    public void testFormatNameBytes() {
        // Test case where name is shorter than buffer length
        byte[] buf = new byte[5];
        String name = "foo";
        assertEquals(5, TarUtils.formatNameBytes(name, buf, 0, 5));
        assertEquals('f', buf[0]);
        assertEquals('o', buf[1]);
        assertEquals('o', buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(0, buf[4]);
        
        // Test case where name is longer than buffer length
        byte[] buf1 = new byte[3];
        String name1 = "foo";
        assertEquals(3, TarUtils.formatNameBytes(name1, buf1, 0, 3));
        assertEquals('f', buf1[0]);
        assertEquals('o', buf1[1]);
        assertEquals('o', buf1[2]);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        // Test case where value is 0
        byte[] buf = new byte[3];
        TarUtils.formatUnsignedOctalString(0, buf, 0, 3);
        assertEquals('0', buf[0]);
        assertEquals(0, buf[1]);
        assertEquals(0, buf[2]);
        
        // Test case where value is not 0
        byte[] buf1 = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buf1, 0, 5);
        assertEquals('0', buf1[0]);
        assertEquals('1', buf1[1]);
        assertEquals('7', buf1[2]);
        assertEquals(0, buf1[3]);
        assertEquals(0, buf1[4]);
    }
    
    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[5];
        TarUtils.formatOctalBytes(123, buf, 0, 5);
        assertEquals('0', buf[0]);
        assertEquals('1', buf[1]);
        assertEquals('7', buf[2]);
        assertEquals(' ', buf[3]);
        assertEquals(0, buf[4]);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[4];
        TarUtils.formatLongOctalBytes(123, buf, 0, 4);
        assertEquals('7', buf[0]);
        assertEquals(' ', buf[1]);
        assertEquals(0, buf[2]);
        assertEquals(0, buf[3]);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[5];
        TarUtils.formatCheckSumOctalBytes(123, buf, 0, 5);
        assertEquals('0', buf[0]);
        assertEquals('1', buf[1]);
        assertEquals('7', buf[2]);
        assertEquals(0, buf[3]);
        assertEquals(' ', buf[4]);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = {1, 2, 3};
        assertEquals(6, TarUtils.computeCheckSum(buf));
    }
}
