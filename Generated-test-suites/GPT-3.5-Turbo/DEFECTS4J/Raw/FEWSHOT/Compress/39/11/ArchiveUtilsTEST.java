package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {
    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("main.c", false, 2000);
        ArchiveEntry entry2 = new ArchiveEntry("testfiles", true, 100);
        
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer1 = "hello".getBytes();
        byte[] buffer2 = "world".getBytes();
        
        assertTrue(ArchiveUtils.matchAsciiBuffer("hello", buffer1));
        assertFalse(ArchiveUtils.matchAsciiBuffer("world", buffer1));
        assertFalse(ArchiveUtils.matchAsciiBuffer("hello", buffer2));
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] expected = "hello".getBytes();
        byte[] actual = ArchiveUtils.toAsciiBytes("hello");
        
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "hello".getBytes();
        String expected = "hello";
        String actual = ArchiveUtils.toAsciiString(inputBytes);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "hello world".getBytes();
        String expected = "world";
        String actual = ArchiveUtils.toAsciiString(inputBytes, 6, 5);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = "hello".getBytes();
        byte[] buffer2 = "hello".getBytes();
        byte[] buffer3 = "world".getBytes();
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {0, 0, 0, 0};
        byte[] buffer2 = {0, 0, 0};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 3, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 3, false));
    }
    
    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "hello world".getBytes();
        byte[] buffer2 = "world".getBytes();
        
        assertTrue(ArchiveUtils.isEqual(buffer1, 6, 5, buffer2, 0, 5));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNullsOffsetAndLength() {
        byte[] buffer1 = {0, 0, 0, 0};
        byte[] buffer2 = {0};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 1, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 1, false));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array1 = {0, 0, 0, 0};
        byte[] array2 = {0, 0, 1, 0};
        
        assertTrue(ArchiveUtils.isArrayZero(array1, 4));
        assertFalse(ArchiveUtils.isArrayZero(array2, 4));
    }
    
    @Test
    public void testSanitize() {
        String input = "abc\ndef\tghi";
        String expected = "abc?def?ghi";
        String actual = ArchiveUtils.sanitize(input);
        
        assertEquals(expected, actual);
    }
}