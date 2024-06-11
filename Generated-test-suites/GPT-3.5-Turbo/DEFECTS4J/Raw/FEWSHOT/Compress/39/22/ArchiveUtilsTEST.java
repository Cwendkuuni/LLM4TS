import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry(true, 2000, "main.c");
        ArchiveEntry entry2 = new ArchiveEntry(false, 100, "testfiles");
        
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer1 = ArchiveUtils.toAsciiBytes("foo");
        byte[] buffer2 = ArchiveUtils.toAsciiBytes("bar");
        
        assertTrue(ArchiveUtils.matchAsciiBuffer("foo", buffer1));
        assertTrue(ArchiveUtils.matchAsciiBuffer("bar", buffer2));
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] expected = {102, 111, 111};
        byte[] result = ArchiveUtils.toAsciiBytes("foo");
        
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {102, 111, 111};
        String expected = "foo";
        
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }
    
    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = {102, 111, 111, 98, 97, 114};
        String expected = "bar";
        
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 3, 3));
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = {102, 111, 111};
        byte[] buffer2 = {98, 97, 114};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {102, 111, 111, 0, 0, 0};
        byte[] buffer2 = {102, 111, 111};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = {102, 111, 111, 98, 97, 114};
        byte[] buffer2 = {111, 111, 98};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, 1, 3, buffer2, 0, 3));
    }
    
    @Test
    public void testIsEqualArrayZero() {
        byte[] a = {0, 0, 0, 0, 0};
        int size = 5;
        
        assertTrue(ArchiveUtils.isArrayZero(a, size));
    }
    
    @Test
    public void testSanitize() {
        String s = "ABC" + Character.toString((char) 7) + "DEF" + Character.toString((char) 255);
        String expected = "ABC?DEF?";
        
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }
}