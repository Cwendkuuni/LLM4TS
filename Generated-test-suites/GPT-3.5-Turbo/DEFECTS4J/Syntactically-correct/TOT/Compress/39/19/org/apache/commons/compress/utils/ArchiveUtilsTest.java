
package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry() {
            public boolean isDirectory() {
                return false;
            }
            public long getSize() {
                return 2000;
            }
            public String getName() {
                return "main.c";
            }
        };
        String expectedOutput1 = "-    2000 main.c";
        assertEquals(expectedOutput1, ArchiveUtils.toString(entry1));
        
        ArchiveEntry entry2 = new ArchiveEntry() {
            public boolean isDirectory() {
                return true;
            }
            public long getSize() {
                return 100;
            }
            public String getName() {
                return "testfiles";
            }
        };
        String expectedOutput2 = "d     100 testfiles";
        assertEquals(expectedOutput2, ArchiveUtils.toString(entry2));
    }
    
    @Test
    public void testMatchAsciiBuffer() throws UnsupportedEncodingException {
        String expectedBuffer = "hello world";
        byte[] buffer1 = expectedBuffer.getBytes("US-ASCII");
        String buffer2 = "hello world";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expectedBuffer, buffer1));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expectedBuffer, buffer1, 0, buffer1.length));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expectedBuffer, buffer2.getBytes("US-ASCII")));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expectedBuffer, buffer2.getBytes("US-ASCII"), 0, buffer2.getBytes("US-ASCII").length));
        
        String expectedBuffer2 = "hello worldddd";
        byte[] buffer3 = expectedBuffer2.getBytes("US-ASCII");
        String buffer4 = "hello world";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expectedBuffer2, buffer3));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expectedBuffer2, buffer3, 0, buffer3.length));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expectedBuffer2, buffer4.getBytes("US-ASCII")));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expectedBuffer2, buffer4.getBytes("US-ASCII"), 0, buffer4.getBytes("US-ASCII").length));
        
    }
    
    @Test
    public void testToAsciiBytes() throws UnsupportedEncodingException {
        String inputString = "hello world";
        byte[] expectedBytes = inputString.getBytes("US-ASCII");
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
    }
    
    @Test
    public void testToAsciiString() throws UnsupportedEncodingException {
        byte[] inputBytes = "hello world".getBytes("US-ASCII");
        String expectedString = "hello world";
        assertEquals(expectedString, ArchiveUtils.toAsciiString(inputBytes));
        assertEquals(expectedString, ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = "hello world".getBytes();
        byte[] buffer2 = "hello world".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer1));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }
    
    @Test
    public void testIsEqualWithTrailingNulls() {
        byte[] buffer1 = "hello world\0\0\0\0\0".getBytes();
        byte[] buffer2 = "hello world".getBytes();
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array1 = new byte[5];
        byte[] array2 = new byte[] {1, 2, 3, 4, 5};
        assertTrue(ArchiveUtils.isArrayZero(array1, 5));
        assertFalse(ArchiveUtils.isArrayZero(array2, 5));
    }
    
    @Test
    public void testSanitize() {
        String input = "hello \nworld\t";
        String expectedOutput = "hello ?world?";
        assertEquals(expectedOutput, ArchiveUtils.sanitize(input));
        
        String input2 = "h\n\0";
        String expectedOutput2 = "h?";
        assertEquals(expectedOutput2, ArchiveUtils.sanitize(input2));
    }

}
