
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");
        
        String expected = "-    2000 main.c";
        String actual = ArchiveUtils.toString(entry);
        assertEquals(expected, actual);
        
        entry.setDirectory(true);
        entry.setName("testfiles");
        
        expected = "d     100 testfiles";
        actual = ArchiveUtils.toString(entry);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchAsciiBuffer_withOffsetAndLength() {
        String expected = "Hello, World!";
        byte[] buffer = expected.getBytes();
        int offset = 0;
        int length = expected.length();
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length));
        
        assertFalse(ArchiveUtils.matchAsciiBuffer("Goodbye, World!", buffer, offset, length));
    }
    
    @Test
    public void testMatchAsciiBuffer_withoutOffsetAndLength() {
        String expected = "Hello, World!";
        byte[] buffer = expected.getBytes();
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        
        assertFalse(ArchiveUtils.matchAsciiBuffer("Goodbye, World!", buffer));
    }
    
    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello, World!";
        byte[] expected = inputString.getBytes();
        
        byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testToAsciiString_withOffsetAndLength() {
        byte[] inputBytes = "Hello, World!".getBytes();
        int offset = 0;
        int length = 5;
        
        String expected = "Hello";
        String actual = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToAsciiString_withoutOffsetAndLength() {
        byte[] inputBytes = "Hello, World!".getBytes();
        
        String expected = "Hello, World!";
        String actual = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsEqual_withOffsetAndLength() {
        byte[] buffer1 = "Hello, World!".getBytes();
        int offset1 = 0;
        int length1 = 5;
        
        byte[] buffer2 = "Hello".getBytes();
        int offset2 = 0;
        int length2 = 5;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
        
        assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true));
    }
    
    @Test
    public void testIsEqual_withoutOffsetAndLength() {
        byte[] buffer1 = "Hello, World!".getBytes();
        
        byte[] buffer2 = "Hello, World!".getBytes();
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "Hello, World!".getBytes();
        int offset1 = 0;
        int length1 = 5;
        
        byte[] buffer2 = "Hello".getBytes();
        int offset2 = 0;
        int length2 = 5;
        
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, offset1, length1, buffer2, offset2, length2));
        
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, offset1, length1, buffer2, offset2, length2, true));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        
        assertTrue(ArchiveUtils.isArrayZero(array, 10));
        
        array[5] = 1;
        
        assertFalse(ArchiveUtils.isArrayZero(array, 10));
    }
    
    @Test
    public void testSanitize() {
        String input = "Hello, World!";
        String expected = "Hello, World!";
        
        String actual = ArchiveUtils.sanitize(input);
        assertEquals(expected, actual);
        
        String input2 = "\u0000Hello,\nWorld!\u001F";
        String expected2 = "?Hello,?World!?";
        
        String actual2 = ArchiveUtils.sanitize(input2);
        assertEquals(expected2, actual2);
    }
}
