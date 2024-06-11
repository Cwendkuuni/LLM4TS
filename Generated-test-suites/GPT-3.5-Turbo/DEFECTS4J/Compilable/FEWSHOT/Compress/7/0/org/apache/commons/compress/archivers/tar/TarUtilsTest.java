package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] {48, 48, 48, 48, 48, 48, 48, 48, 48, 48}; // 0000000000
        assertEquals(0, TarUtils.parseOctal(buffer, 0, 10));
        
        buffer = new byte[] {48, 49, 50, 51, 52, 53, 54, 55, 56, 57}; // 0123456789
        assertEquals(123456789, TarUtils.parseOctal(buffer, 0, 10));
        
        buffer = new byte[] {49, 54, 78, 49, 71, 65, 73, 76, 48, 0}; // 16N1GAIL0
        assertEquals(389232933280L, TarUtils.parseOctal(buffer, 0, 10));
        
        buffer = new byte[] {49, 50, 51, 52, 53, 54, 55, 56, 57, 97}; // 123456789a
        try {
            TarUtils.parseOctal(buffer, 0, 10);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 9 in '123456789a'", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] {104, 101, 108, 108, 111, 46, 116, 120, 116, 0}; // hello.txt
        assertEquals("hello.txt", TarUtils.parseName(buffer, 0, 10));
        
        buffer = new byte[] {102, 111, 111, 98, 97, 114, 0, 0, 0, 0}; // foobar
        assertEquals("foobar", TarUtils.parseName(buffer, 0, 6));
        
        buffer = new byte[] {116, 101, 115, 116, 95, 95, 95, 0, 0, 0}; // test___
        assertEquals("test", TarUtils.parseName(buffer, 0, 10));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello.txt";
        byte[] buffer = new byte[10];
        TarUtils.formatNameBytes(name, buffer, 0, 10);
        assertEquals("hello.txt", new String(buffer));
        
        name = "foobar";
        buffer = new byte[6];
        TarUtils.formatNameBytes(name, buffer, 0, 6);
        assertEquals("foobar", new String(buffer));
        
        name = "test";
        buffer = new byte[10];
        TarUtils.formatNameBytes(name, buffer, 0, 10);
        assertEquals("test\0\0\0\0\0\0", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(0, buffer, 0, 10);
        assertEquals("0         ", new String(buffer));
        
        buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(123456789, buffer, 0, 8);
        assertEquals("463561725", new String(buffer));
        
        buffer = new byte[15];
        try {
            TarUtils.formatUnsignedOctalString(389232933280L, buffer, 0, 15);
        } catch (IllegalArgumentException e) {
            assertEquals("389232933280=3327005160400 will not fit in octal number buffer of length 15", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(0, buffer, 0, 10);
        assertEquals("0         ", new String(buffer));
        
        buffer = new byte[8];
        TarUtils.formatOctalBytes(123456789, buffer, 0, 8);
        assertEquals("463561725", new String(buffer));
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalBytes(0, buffer, 0, 10);
        assertEquals("0        ", new String(buffer));
        
        buffer = new byte[9];
        TarUtils.formatLongOctalBytes(123456789, buffer, 0, 9);
        assertEquals("463561725", new String(buffer));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];
        TarUtils.formatCheckSumOctalBytes(0, buffer, 0, 10);
        assertEquals("0         ", new String(buffer));
        
        buffer = new byte[9];
        TarUtils.formatCheckSumOctalBytes(123456789, buffer, 0, 9);
        assertEquals("463561725", new String(buffer));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] {104, 101, 108, 108, 111, 46, 116, 120, 116, 0}; // hello.txt
        assertEquals(803, TarUtils.computeCheckSum(buffer));
        
        buffer = new byte[] {102, 111, 111, 98, 97, 114, 0, 0, 0, 0}; // foobar
        assertEquals(364, TarUtils.computeCheckSum(buffer));
        
        buffer = new byte[] {116, 101, 115, 116, 95, 95, 95, 0, 0, 0}; // test___
        assertEquals(500, TarUtils.computeCheckSum(buffer));
    }
}