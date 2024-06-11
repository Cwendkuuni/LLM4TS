import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    // toString() method test cases
    @Test
    public void testToStringForDirectoryEntry() {
        ArchiveEntry entry = createDirectoryEntry("test");
        String expected = "d     0 test";
        String actual = ArchiveUtils.toString(entry);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringForFileEntry() {
        ArchiveEntry entry = createFileEntry("main.c", 2000);
        String expected = "-  2000 main.c";
        String actual = ArchiveUtils.toString(entry);
        assertEquals(expected, actual);
    }
    
    	
    // matchAsciiBuffer() method test cases
    @Test
    public void testMatchAsciiBufferForMatchedBuffers() {
        byte[] buffer = {97, 98, 99, 100}; // "abcd"
        assertTrue(ArchiveUtils.matchAsciiBuffer("abcd", buffer));
    }
    
    @Test
    public void testMatchAsciiBufferForMismatchedBuffers() {
        byte[] buffer = {97, 98, 99, 100}; // "abcd"
        assertFalse(ArchiveUtils.matchAsciiBuffer("efgh", buffer));
    }
    
    @Test
    public void testMatchAsciiBufferForPartialBuffer() {
        byte[] buffer = {97, 98, 99, 100}; // "abcd"
        assertFalse(ArchiveUtils.matchAsciiBuffer("abcde", buffer));
    }

    // toAsciiBytes() method test case
    @Test
    public void testToAsciiBytes() {
        byte[] expected = {97, 98, 99, 100}; // "abcd"
        byte[] actual = ArchiveUtils.toAsciiBytes("abcd");
        assertArrayEquals(expected, actual);
    }

    // toAsciiString() method test case
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {97, 98, 99, 100}; // "abcd"
        String expected = "abcd";
        String actual = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, actual);
    }

    // isEqual() method test case
    @Test
    public void testIsEqualForEqualBuffers() {
        byte[] buffer1 = {97, 98, 99, 100}; // "abcd"
        byte[] buffer2 = {97, 98, 99, 100}; // "abcd"
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqualForMismatchedBuffers() {
        byte[] buffer1 = {97, 98, 99, 100}; // "abcd"
        byte[] buffer2 = {101, 102, 103, 104}; // "efgh"
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    // isEqualWithNull() method test case
    @Test
    public void testIsEqualWithNullForEqualBuffers() {
        byte[] buffer1 = {97, 0, 98, 0}; // "a\0b\0"
        byte[] buffer2 = {97, 0, 98, 0}; // "a\0b\0"
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 4));
    }
    
    @Test
    public void testIsEqualWithNullForMismatchedBuffers() {
        byte[] buffer1 = {97, 0, 98, 0}; // "a\0b\0"
        byte[] buffer2 = {97, 98, 99, 100}; // "abcd"
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 4));
    }

    // isArrayZero() method test case
    @Test
    public void testIsArrayZeroForZeroBytes() {
        byte[] a = {0, 0, 0, 0}; // "\0\0\0\0"
        assertTrue(ArchiveUtils.isArrayZero(a, 4));
    }
    
    @Test
    public void testIsArrayZeroForNotZeroBytes() {
        byte[] a = {1, 2, 3, 0}; // "\1\2\3\0"
        assertFalse(ArchiveUtils.isArrayZero(a, 4));
    }

    // sanitize() method test case
    @Test
    public void testSanitizeForPrintableString() {
        String s = "abcd";
        String expected = "abcd";
        String actual = ArchiveUtils.sanitize(s);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSanitizeForNonPrintableString() {
        String s = "\u0001\u0002\u0003\u0004"; // non-printable characters
        String expected = "????"; // all characters replaced by '?'
        String actual = ArchiveUtils.sanitize(s);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSanitizeForLongString() {
        String s = generateLongString(300); // 300 characters long string
        String expected = generateLongString(255); // truncated to 255 characters
        String actual = ArchiveUtils.sanitize(s);
        assertEquals(expected, actual);
    }

    // Helper methods
    private ArchiveEntry createDirectoryEntry(String name) {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName(name);
        entry.setIsDirectory(true);
        entry.setSize(0);
        return entry;
    }
    
    private ArchiveEntry createFileEntry(String name, long size) {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName(name);
        entry.setIsDirectory(false);
        entry.setSize(size);
        return entry;
    }

    private String generateLongString(int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, 'a');
        return new String(chars);
    }
}