package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {
    
    @Test
    public void testParseOctal() {
        byte[] buffer = "123\0abc".getBytes();
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        long expected = 123;
        assertEquals(expected, actual);
        
        buffer = "123  \0abc".getBytes();
        actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        expected = 123;
        assertEquals(expected, actual);
        
        buffer = "  123  \0abc".getBytes();
        actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        expected = 123;
        assertEquals(expected, actual);
        
        buffer = "123abc".getBytes();
        actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        expected = 123;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "test\0abc".getBytes();
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        String expected = "test";
        assertEquals(expected, actual);
        
        buffer = "test    \0abc".getBytes();
        actual = TarUtils.parseName(buffer, 0, buffer.length);
        expected = "test";
        assertEquals(expected, actual);
        
        buffer = "testabc".getBytes();
        actual = TarUtils.parseName(buffer, 0, buffer.length);
        expected = "test";
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
        int expected = offset + name.length();
        assertEquals(expected, actual);
        
        name = "test";
        buffer = new byte[4];
        offset = 0;
        length = 4;
        actual = TarUtils.formatNameBytes(name, buffer, offset, length);
        expected = offset + length;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String actual = new String(buffer);
        String expected = "00000123";
        assertEquals(expected, actual);
        
        value = 123;
        buffer = new byte[4];
        offset = 0;
        length = 4;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        actual = new String(buffer);
        expected = "0123";
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        int actual = TarUtils.formatOctalBytes(value, buffer, offset, length);
        int expected = offset + length;
        assertEquals(expected, actual);
        
        value = 123;
        buffer = new byte[4];
        offset = 0;
        length = 4;
        actual = TarUtils.formatOctalBytes(value, buffer, offset, length);
        expected = offset + length;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        int actual = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        int expected = offset + length;
        assertEquals(expected, actual);
        
        value = 123;
        buffer = new byte[4];
        offset = 0;
        length = 4;
        actual = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        expected = offset + length;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        int actual = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        int expected = offset + length;
        assertEquals(expected, actual);
        
        value = 123;
        buffer = new byte[4];
        offset = 0;
        length = 4;
        actual = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        expected = offset + length;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "test".getBytes();
        long actual = TarUtils.computeCheckSum(buffer);
        long expected = 448;
        assertEquals(expected, actual);
    }
}
