package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry(true, 2000, "main.c");
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));
        
        ArchiveEntry entry2 = new ArchiveEntry(false, 100, "testfiles");
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
        
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = new byte[4];
        buffer[0] = 116;
        buffer[1] = 101;
        buffer[2] = 115;
        buffer[3] = 116;
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = new byte[4];
        expected[0] = 116;
        expected[1] = 101;
        expected[2] = 115;
        expected[3] = 116;
        
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiStringByteArray() {
        byte[] inputBytes = new byte[4];
        inputBytes[0] = 116;
        inputBytes[1] = 101;
        inputBytes[2] = 115;
        inputBytes[3] = 116;
        
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringByteArrayIntInt() {
        byte[] inputBytes = new byte[4];
        inputBytes[0] = 116;
        inputBytes[1] = 101;
        inputBytes[2] = 115;
        inputBytes[3] = 116;
        
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 0, 4));
    }

    @Test
    public void testIsEqualByteArrayIntIntByteArrayIntIntBoolean() {
        byte[] buffer1 = new byte[4];
        buffer1[0] = 116;
        buffer1[1] = 101;
        buffer1[2] = 115;
        buffer1[3] = 116;
        
        byte[] buffer2 = new byte[4];
        buffer2[0] = 116;
        buffer2[1] = 101;
        buffer2[2] = 115;
        buffer2[3] = 116;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 4, false));
    }

    @Test
    public void testIsEqualByteArrayIntIntByteArrayIntInt() {
        byte[] buffer1 = new byte[4];
        buffer1[0] = 116;
        buffer1[1] = 101;
        buffer1[2] = 115;
        buffer1[3] = 116;
        
        byte[] buffer2 = new byte[4];
        buffer2[0] = 116;
        buffer2[1] = 101;
        buffer2[2] = 115;
        buffer2[3] = 116;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 4));
    }

    @Test
    public void testIsEqualByteArrayByteArray() {
        byte[] buffer1 = new byte[4];
        buffer1[0] = 116;
        buffer1[1] = 101;
        buffer1[2] = 115;
        buffer1[3] = 116;
        
        byte[] buffer2 = new byte[4];
        buffer2[0] = 116;
        buffer2[1] = 101;
        buffer2[2] = 115;
        buffer2[3] = 116;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualByteArrayByteArrayBoolean() {
        byte[] buffer1 = new byte[4];
        buffer1[0] = 116;
        buffer1[1] = 101;
        buffer1[2] = 115;
        buffer1[3] = 116;
        
        byte[] buffer2 = new byte[4];
        buffer2[0] = 116;
        buffer2[1] = 101;
        buffer2[2] = 115;
        buffer2[3] = 116;
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, false));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[4];
        buffer1[0] = 116;
        buffer1[1] = 101;
        buffer1[2] = 0;
        buffer1[3] = 0;
        
        byte[] buffer2 = new byte[4];
        buffer2[0] = 116;
        buffer2[1] = 101;
        buffer2[2] = 115;
        buffer2[3] = 116;
        
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 4));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[4];
        a[0] = 0;
        a[1] = 0;
        a[2] = 0;
        a[3] = 0;
        
        assertTrue(ArchiveUtils.isArrayZero(a, 4));
    }

    @Test
    public void testSanitize() {
        String s = "test\n";
        String expected = "test?";
        
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }

}