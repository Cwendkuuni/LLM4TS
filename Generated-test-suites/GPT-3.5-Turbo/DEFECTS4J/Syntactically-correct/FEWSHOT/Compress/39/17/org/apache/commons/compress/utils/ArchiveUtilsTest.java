import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test", false, 100);
        assertEquals("-     100 test", ArchiveUtils.toString(entry));
        
        entry.setDirectory(true);
        assertEquals("d     100 test", ArchiveUtils.toString(entry));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        assertTrue(ArchiveUtils.matchAsciiBuffer("abc", new byte[] {'a', 'b', 'c'}));
        assertTrue(ArchiveUtils.matchAsciiBuffer("abc", new byte[] {'a', 'b', 'c', (byte)0}));
        assertFalse(ArchiveUtils.matchAsciiBuffer("def", new byte[] {'a', 'b', 'c'}));
        assertFalse(ArchiveUtils.matchAsciiBuffer("def", new byte[] {'a', 'b', 'c', (byte)0}));
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] expected = {'a', 'b', 'c'};
        byte[] result = ArchiveUtils.toAsciiBytes("abc");
        assertTrue(ArchiveUtils.isEqual(expected, result));
    }
    
    @Test
    public void testToAsciiString() {
        String expected = "abc";
        byte[] inputBytes = {'a', 'b', 'c'};
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }
    
    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        String expected = "ab";
        byte[] inputBytes = {'a', 'b', 'c'};
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 0, 2));
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = {'a', 'b', 'c'};
        byte[] buffer2 = {'a', 'b', 'c'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        byte[] buffer3 = {'d', 'e', 'f'};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {'a', 'b', 'c', (byte)0};
        byte[] buffer2 = {'a', 'b', 'c'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
        
        byte[] buffer3 = {'a', 'b', 'c', 'd'};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, true));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array = {'a', 'b', 'c'};
        assertFalse(ArchiveUtils.isArrayZero(array, 3));
        
        byte[] array2 = {0, 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(array2, 3));
    }
    
    @Test
    public void testSanitize() {
        assertEquals("abc", ArchiveUtils.sanitize("abc"));
        assertEquals("?", ArchiveUtils.sanitize("\u001F"));
        assertEquals("?", ArchiveUtils.sanitize("\uD7FF"));
        assertEquals("?", ArchiveUtils.sanitize("\uE000"));
        assertEquals("?", ArchiveUtils.sanitize("\uFFFF"));
    }
}