import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");
        
        String expected = "-    2000 main.c";
        
        assertEquals(expected, ArchiveUtils.toString(entry));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = new byte[]{116, 101, 115, 116};
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }
    
    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = new byte[]{116, 101, 115, 116};
        
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }
    
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{116, 101, 115, 116};
        String expected = "test";
        
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{116, 101, 115, 116};
        byte[] buffer2 = new byte[]{116, 101, 115, 116};
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testSanitize() {
        String s = "test";
        String expected = "test";
        
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }
}