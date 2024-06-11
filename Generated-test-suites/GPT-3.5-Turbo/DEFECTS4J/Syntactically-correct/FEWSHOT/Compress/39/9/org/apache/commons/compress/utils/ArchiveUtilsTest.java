
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToStringForFile() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("file.txt");
        entry.setSize(2000);
        assertFalse(entry.isDirectory());
        
        String expected = "-    2000 file.txt";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }
    
    @Test
    public void testToStringForDirectory() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("dir");
        entry.setSize(100);
        assertTrue(entry.isDirectory());
        
        String expected = "d     100 dir";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }
    
    @Test
    public void testMatchAsciiBuffer() throws UnsupportedEncodingException {
        String expected = "hello";
        byte[] buffer = expected.getBytes("US-ASCII");
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }
    
    @Test
    public void testMatchAsciiBufferForInvalidAsciiString() throws UnsupportedEncodingException {
        String expected = "h\u00E9llo"; // invalid Ascii character
        byte[] buffer = expected.getBytes("US-ASCII");
        
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }
    
    @Test
    public void testToAsciiBytes() throws UnsupportedEncodingException {
        String input = "hello";
        byte[] expected = input.getBytes("US-ASCII");
        
        byte[] result = ArchiveUtils.toAsciiBytes(input);
        assertTrue(Arrays.equals(expected, result));
    }
    
    @Test
    public void testToAsciiString() throws UnsupportedEncodingException {
        byte[] inputBytes = {104, 101, 108, 108, 111};
        String expected = "hello";
        
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }
    
    @Test
    public void testToAsciiStringWithOffsetAndLength() throws UnsupportedEncodingException {
        byte[] inputBytes = {104, 101, 108, 108, 111};
        String expected = "ell";
        
        String result = ArchiveUtils.toAsciiString(inputBytes, 1, 3);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqualWithDifferentBuffers() {
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {4, 5, 6};
        
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = {1, 2, 3, 4};
        byte[] buffer2 = {1, 2, 3};
        
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {1, 2, 3, 0, 0, 0};
        byte[] buffer2 = {1, 2, 3};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3, true));
    }
    
    @Test
    public void testIsEqualWithDifferentLengthsAndIgnoreTrailingNulls() {
        byte[] buffer1 = {1, 2, 3, 4};
        byte[] buffer2 = {1, 2, 3};
        
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 3, true));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array = {0, 0, 0};
        int size = 3;
        
        assertTrue(ArchiveUtils.isArrayZero(array, size));
    }
    
    @Test
    public void testIsArrayZeroWithNonZeroBytes() {
        byte[] array = {0, 1, 0};
        int size = 3;
        
        assertFalse(ArchiveUtils.isArrayZero(array, size));
    }
    
    @Test
    public void testSanitize() {
        String input = "h\u0001e\u0002l\u0003l\u0004o"; // contains control characters
        String expected = "h?e?l?l?o";
        
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

}
