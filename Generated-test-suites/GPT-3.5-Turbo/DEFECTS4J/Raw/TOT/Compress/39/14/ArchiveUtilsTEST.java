import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    private static final byte[] EMPTY_BUFFER = new byte[0];
    private static final byte[] BUFFER_WITH_CONTENT = new byte[] { 1, 2, 3, 4, 5 };

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntryImpl("testFile1", false, 100);
        String expected = "- 100 testFile1";
        assertEquals(expected, ArchiveUtils.toString(entry));
        
        entry = new ArchiveEntryImpl("testDirectory", true, 200);
        expected = "d 200 testDirectory";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }
    
    @Test
    public void testMatchAsciiBuffer_withOffset() {
        assertTrue(ArchiveUtils.matchAsciiBuffer("abc", "abc".getBytes(), 0, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer("abc", "xyz".getBytes(), 0, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer("abc", "xyzabc".getBytes(), 3, 3));
    }
    
    @Test
    public void testMatchAsciiBuffer_withoutOffset() {
        assertTrue(ArchiveUtils.matchAsciiBuffer("abc", "abc".getBytes()));
        assertFalse(ArchiveUtils.matchAsciiBuffer("abc", "xyz".getBytes()));
        assertFalse(ArchiveUtils.matchAsciiBuffer("abc", "xyzabc".getBytes()));
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] expected = "abc".getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes("abc");
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testToAsciiString_withOffset() {
        byte[] bytes = "abc".getBytes();
        String expected = "c";
        String result = ArchiveUtils.toAsciiString(bytes, 2, 1);
        assertEquals(expected, result);
    }
    
    @Test
    public void testToAsciiString_withoutOffset() {
        byte[] bytes = "abc".getBytes();
        String expected = "abc";
        String result = ArchiveUtils.toAsciiString(bytes);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { 1, 2, 3 };
        byte[] buffer2 = new byte[] { 1, 2, 3 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        buffer2 = new byte[] { 3, 2, 1 };
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqual_withIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[] { 1, 2, 3 };
        byte[] buffer2 = new byte[] { 1, 2, 3, 0, 0 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
        
        buffer2 = new byte[] { 3, 2, 1, 0, 0 };
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
        
        buffer1 = new byte[] { 1, 2, 3, 0, 0 };
        buffer2 = new byte[] { 1, 2, 3 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
        
        buffer2 = new byte[] { 3, 2, 1 };
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] { 0, 0, 0, 0 };
        byte[] buffer2 = new byte[] { 0, 0, 0 };
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 3));
        
        buffer2 = new byte[] { 0, 0, 0, 0 };
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 3));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[] { 0, 0, 0, 0 };
        assertTrue(ArchiveUtils.isArrayZero(array, 4));
        
        array = new byte[] { 0, 0, 1, 0 };
        assertFalse(ArchiveUtils.isArrayZero(array, 4));
    }
    
    @Test
    public void testSanitize() {
        String input = "abc\u0000def";
        String expected = "abc?def";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
        
        input = "\u0001\u0002\u0003";
        expected = "???";
        result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
        
        input = "abc\u0004";
        expected = "abc?";
        result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
        
        input = "";
        expected = "";
        result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }
}

class ArchiveEntryImpl implements ArchiveEntry {

    private String name;
    private boolean isDirectory;
    private long size;
    
    public ArchiveEntryImpl(String name, boolean isDirectory, long size) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
    }
    
    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public long getSize() {
        return size;
    }
}