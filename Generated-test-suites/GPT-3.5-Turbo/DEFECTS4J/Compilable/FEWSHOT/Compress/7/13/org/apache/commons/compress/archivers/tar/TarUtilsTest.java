package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', '4', '5', '6', '7', '0'};
        assertEquals(012345670, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        byte[] buffer2 = {'1', '2', '3', '4', '5', '6', '7'};
        assertEquals(01234567, TarUtils.parseOctal(buffer2, 0, buffer2.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0, 'f', 'o', 'o'};
        assertEquals("test", TarUtils.parseName(buffer, 0, buffer.length));
        
        byte[] buffer2 = {'t', 'e', 's', 't', 0};
        assertEquals("test", TarUtils.parseName(buffer2, 0, buffer2.length));
    }
    
    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        String name = "test";
        int offset = 0;
        int length = buffer.length;
        int expected = offset + length;
        assertEquals(expected, TarUtils.formatNameBytes(name, buffer, offset, length));
        assertEquals("test", new String(buffer, offset, length));
        
        byte[] buffer2 = new byte[3];
        String name2 = "foo";
        int offset2 = 0;
        int length2 = buffer2.length;
        int expected2 = offset2 + length2;
        assertEquals(expected2, TarUtils.formatNameBytes(name2, buffer2, offset2, length2));
        assertEquals("foo", new String(buffer2, offset2, length2));
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        long value = 12345;
        int offset = 0;
        int length = buffer.length;
        int expected = offset + length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals("0000012345", new String(buffer, offset, length));
        
        byte[] buffer2 = new byte[4];
        long value2 = 12;
        int offset2 = 0;
        int length2 = buffer2.length;
        int expected2 = offset2 + length2;
        TarUtils.formatUnsignedOctalString(value2, buffer2, offset2, length2);
        assertEquals("0012", new String(buffer2, offset2, length2));
    }
    
    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];
        long value = 12345;
        int offset = 0;
        int length = buffer.length;
        int expected = offset + length;
        assertEquals(expected, TarUtils.formatOctalBytes(value, buffer, offset, length));
        assertEquals("0000012345 ", new String(buffer, offset, length));
        
        byte[] buffer2 = new byte[4];
        long value2 = 12;
        int offset2 = 0;
        int length2 = buffer2.length;
        int expected2 = offset2 + length2;
        assertEquals(expected2, TarUtils.formatOctalBytes(value2, buffer2, offset2, length2));
        assertEquals("0012 ", new String(buffer2, offset2, length2));
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];
        long value = 12345;
        int offset = 0;
        int length = buffer.length;
        int expected = offset + length;
        assertEquals(expected, TarUtils.formatLongOctalBytes(value, buffer, offset, length));
        assertEquals("0000012345", new String(buffer, offset, length));
        
        byte[] buffer2 = new byte[4];
        long value2 = 12;
        int offset2 = 0;
        int length2 = buffer2.length;
        int expected2 = offset2 + length2;
        assertEquals(expected2, TarUtils.formatLongOctalBytes(value2, buffer2, offset2, length2));
        assertEquals("0012", new String(buffer2, offset2, length2));
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];
        long value = 12345;
        int offset = 0;
        int length = buffer.length;
        int expected = offset + length;
        assertEquals(expected, TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length));
        assertEquals("0000012345 ", new String(buffer, offset, length));
        
        byte[] buffer2 = new byte[4];
        long value2 = 12;
        int offset2 = 0;
        int length2 = buffer2.length;
        int expected2 = offset2 + length2;
        assertEquals(expected2, TarUtils.formatCheckSumOctalBytes(value2, buffer2, offset2, length2));
        assertEquals("0012 ", new String(buffer2, offset2, length2));
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7};
        assertEquals(28, TarUtils.computeCheckSum(buffer));
        
        byte[] buffer2 = {10, 20, 30, 40, 50, 60, 70};
        assertEquals(260, TarUtils.computeCheckSum(buffer2));
    }
}