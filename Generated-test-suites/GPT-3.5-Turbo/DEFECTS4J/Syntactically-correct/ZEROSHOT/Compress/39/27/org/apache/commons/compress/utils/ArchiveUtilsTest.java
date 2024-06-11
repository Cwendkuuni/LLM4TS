import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");
        
        String expected = "d     100 testfiles";
        String result = ArchiveUtils.toString(entry);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = {'t', 'e', 's', 't'};
        
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        
        assertTrue(result);
    }
    
    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = {'t', 'e', 's', 't'};
        
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {'t', 'e', 's', 't'};
        String expected = "test";
        
        String result = ArchiveUtils.toAsciiString(inputBytes);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = {'t', 'e', 's', 't'};
        byte[] buffer2 = {'t', 'e', 's', 't'};
        
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = {'t', 'e', 's', 't'};
        byte[] buffer2 = {'t', 'e', 's', 't'};
        
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] a = {0, 0, 0, 0};
        int size = 4;
        
        boolean result = ArchiveUtils.isArrayZero(a, size);
        
        assertTrue(result);
    }
    
    @Test
    public void testSanitize() {
        String s = "test";
        String expected = "test";
        
        String result = ArchiveUtils.sanitize(s);
        
        assertEquals(expected, result);
    }
    
}