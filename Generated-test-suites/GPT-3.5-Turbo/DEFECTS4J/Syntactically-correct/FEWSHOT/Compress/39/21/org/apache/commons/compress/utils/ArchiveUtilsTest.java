import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        ArchiveEntry entry = null;
        String expected = "";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
        
        entry = new ArchiveEntry();
        entry.setDirectory(true);
        entry.setSize(2000);
        entry.setName("main.c");
        expected = "d    2000 main.c";
        result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
        
        entry.setDirectory(false);
        expected = "-    2000 main.c";
        result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", "test".getBytes()));
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", "test".getBytes(), 0, 4));
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", "   test".getBytes(), 3, 4));
        assertFalse(ArchiveUtils.matchAsciiBuffer("TEST", "test".getBytes()));
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] result = ArchiveUtils.toAsciiBytes("test");
        assertEquals("test", new String(result));
    }

    @Test
    public void testToAsciiString() {
        String result = ArchiveUtils.toAsciiString("test".getBytes());
        assertEquals("test", result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        String result = ArchiveUtils.toAsciiString("   test".getBytes(), 3, 4);
        assertEquals("test", result);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        buffer2 = "TEST".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqualWithOffsetsAndLengths() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "   test".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 3, 4));
        
        buffer2 = "   TEST".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 3, 4));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "test\0".getBytes();
        byte[] buffer2 = "test\0\0".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(array, 5));
        
        array[4] = 1;
        assertFalse(ArchiveUtils.isArrayZero(array, 5));
    }
    
    @Test
    public void testSanitize() {
        String s = "test";
        String expected = "test";
        String result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);
        
        s = "test\n\u0007\test";
        expected = "test?\t?test";
        result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);
    }
}