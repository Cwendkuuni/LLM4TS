package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_normalCase() {
        byte[] buffer = {48, 48, 48, 49, 48}; // "00010"
        int offset = 0;
        int length = 5;
        
        long expected = 10;
        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidByte() {
        byte[] buffer = {48, 48, 48, 52, 48}; // "00040"
        int offset = 0;
        int length = 5;
        
        TarUtils.parseOctal(buffer, offset, length);
    }
    
    @Test
    public void testParseName_normalCase() {
        byte[] buffer = {116, 101, 115, 116, 67, 97, 115, 101, 0, 0}; // "testCase" with trailing null
        int offset = 0;
        int length = 10;
        
        String expected = "testCase";
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParseName_noNullTerminator() {
        byte[] buffer = {116, 101, 115, 116, 67, 97, 115, 101, 0, 116}; // "testCase" with no null terminator
        int offset = 0;
        int length = 10;
        
        String expected = "testCase";
        String result = TarUtils.parseName(buffer, offset, length);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatNameBytes_normalCase() {
        String name = "testCase";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        
        byte[] expected = {116, 101, 115, 116, 67, 97, 115, 101, 0, 0}; // "testCase" with trailing nulls
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatNameBytes_longName() {
        String name = "thisIsALongName";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        
        byte[] expected = {116, 104, 105, 115, 73, 115, 65, 76, 111, 110}; // "thisIsALon"
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatUnsignedOctalString_normalCase() {
        long value = 10;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;
        
        byte[] expected = {48, 48, 48, 49, 48}; // "00010"
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertArrayEquals(expected, buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_bufferTooSmall() {
        long value = 10;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;
        
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }
    
    @Test
    public void testFormatOctalBytes_normalCase() {
        long value = 10;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        
        byte[] expected = {48, 48, 9, 0}; // "0009" with trailing space and null
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatLongOctalBytes_normalCase() {
        long value = 10;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;
        
        byte[] expected = {57, 32, 10}; // "9 " + newline
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes_normalCase() {
        long value = 10;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;
        
        byte[] expected = {48, 48, 9, 32}; // "0009" with trailing null and space
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        
        assertArrayEquals(expected, buf);
        assertEquals(offset + length, result);
    }
    
    @Test
    public void testComputeCheckSum_normalCase() {
        byte[] buf = {116, 101, 115, 116, 67, 97, 115, 101, 0, 0}; // "testCase" with trailing nulls
        
        long expected = 914;
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(expected, result);
    }
}