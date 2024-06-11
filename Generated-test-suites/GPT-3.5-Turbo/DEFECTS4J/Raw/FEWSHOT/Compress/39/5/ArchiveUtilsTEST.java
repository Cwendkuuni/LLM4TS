package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("file1", false, 2000);
        ArchiveEntry entry2 = new ArchiveEntry("testfiles", true, 100);
        
        assertEquals("-    2000 file1", ArchiveUtils.toString(entry1));
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected1 = "test";
        String expected2 = "test123";
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test123".getBytes();
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected1, buffer1));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected2, buffer2));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected1, buffer2));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected2, buffer1));
    }
    
    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = "test".getBytes();
        
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expected = "test";
        
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }
    
    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "test123".getBytes();
        int offset = 1;
        int length = 3;
        String expected = "est";
        
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        byte[] buffer3 = "test123".getBytes();
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }
    
    @Test
    public void testIsEqualWithLength() {
        byte[] buffer1 = "test".getBytes();
        int offset1 = 1;
        int length1 = 2;
        byte[] buffer2 = "tes".getBytes();
        int offset2 = 0;
        int length2 = 3;
        byte[] buffer3 = "123".getBytes();
        int offset3 = 0;
        int length3 = 3;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
        assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer3, offset3, length3));
    }
    
    @Test
    public void testIsEqualIgnoringTrailingNulls() {
        byte[] buffer1 = "test\0\0\0".getBytes();
        int offset1 = 0;
        int length1 = 7;
        byte[] buffer2 = "test".getBytes();
        int offset2 = 0;
        int length2 = 4;
        byte[] buffer3 = "test\0\0\0".getBytes();
        int offset3 = 0;
        int length3 = 4;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer3, offset3, length3, true));
    }
    
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test".getBytes();
        int offset1 = 0;
        int length1 = 4;
        byte[] buffer2 = "test".getBytes();
        int offset2 = 0;
        int length2 = 4;
        
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, offset1, length1, buffer2, offset2, length2));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array1 = new byte[]{0, 0, 0, 0};
        byte[] array2 = new byte[]{5, 0, 0, 0};
        
        assertTrue(ArchiveUtils.isArrayZero(array1, 4));
        assertFalse(ArchiveUtils.isArrayZero(array2, 4));
    }
    
    @Test
    public void testSanitize() {
        String input = "test\u0000\u0000\u0000";
        String expected = "test???";
        
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }
}