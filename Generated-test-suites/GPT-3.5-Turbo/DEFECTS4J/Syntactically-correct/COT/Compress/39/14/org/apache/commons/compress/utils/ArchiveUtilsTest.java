
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;


public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");
        
        String result = ArchiveUtils.toString(entry);
        
        assertEquals("d     100 testfiles", result);
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = {104, 101, 108, 108, 111};
        
        boolean result = ArchiveUtils.matchAsciiBuffer("hello", buffer);
        
        assertTrue(result);
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] result = ArchiveUtils.toAsciiBytes("hello");
        
        byte[] expected = {104, 101, 108, 108, 111};
        
        assertTrue(Arrays.equals(expected, result));
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {104, 101, 108, 108, 111};
        
        String result = ArchiveUtils.toAsciiString(inputBytes);
        
        assertEquals("hello", result);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = {104, 101, 108, 108, 111};
        byte[] buffer2 = {104, 101, 108, 108, 111};
        
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        
        assertTrue(result);
    }
    
    @Test
    public void testSanitize() {
        String s = "Test\u0000String";
        
        String result = ArchiveUtils.sanitize(s);
        
        assertEquals("Test?String", result);
    }
    
    @Test
    public void testMatchAsciiBuffer_withOffsetAndLength() {
        byte[] buffer = {104, 101, 108, 108, 111};
        
        boolean result = ArchiveUtils.matchAsciiBuffer("hello", buffer, 1, 4);
        
        assertTrue(result);
    }
    
    @Test
    public void testToAsciiString_withOffsetAndLength() {
        byte[] inputBytes = {104, 101, 108, 108, 111};
        
        String result = ArchiveUtils.toAsciiString(inputBytes, 1, 4);
        
        assertEquals("ello", result);
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls() {
        byte[] buffer1 = {104, 101, 108, 108, 111, 0, 0, 0};
        byte[] buffer2 = {104, 101, 108, 108, 111};
        
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = {104, 101, 108, 108, 111, 0, 0, 0};
        byte[] buffer2 = {104, 101, 108, 108, 111};
        
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array = {0, 0, 0, 0, 0, 0, 1};
        
        boolean result = ArchiveUtils.isArrayZero(array, 6);
        
        assertTrue(result);
    }
    
    // Additional test cases for potential edge cases and exception handling scenarios could be added here
    
}
