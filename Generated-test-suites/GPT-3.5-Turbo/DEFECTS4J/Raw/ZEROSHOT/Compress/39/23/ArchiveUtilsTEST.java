import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry));
        
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = new byte[10];
        buffer[0] = 97;
        buffer[1] = 98;
        buffer[2] = 99;
        assertTrue(ArchiveUtils.matchAsciiBuffer("abc", buffer, 0, 3));
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] result = ArchiveUtils.toAsciiBytes("abc");
        byte[] expected = new byte[]{ 97, 98, 99 };
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testToAsciiString() {
        byte[] buffer = new byte[]{ 97, 98, 99 };
        String result = ArchiveUtils.toAsciiString(buffer);
        assertEquals("abc", result);
    }
    
    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] buffer = new byte[]{ 97, 98, 99 };
        String result = ArchiveUtils.toAsciiString(buffer, 0, 2);
        assertEquals("ab", result);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{ 97, 98, 99 };
        byte[] buffer2 = new byte[]{ 97, 98, 99 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{ 97, 98, 99, 0 };
        byte[] buffer2 = new byte[]{ 97, 98, 99 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = new byte[]{ 97, 98, 99 };
        byte[] buffer2 = new byte[]{ 97, 98, 99 };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3));
    }
    
    @Test
    public void testIsEqualWithNullCheck() {
        byte[] buffer1 = new byte[]{ 97, 98, 99 };
        byte[] buffer2 = new byte[]{ 97, 98, 99 };
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 3));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[]{ 0, 0, 0 };
        assertTrue(ArchiveUtils.isArrayZero(array, 3));
    }
    
    @Test
    public void testSanitize() {
        String result = ArchiveUtils.sanitize("abc");
        assertEquals("abc", result);
        
        result = ArchiveUtils.sanitize("\"\n\t");
        assertEquals("???", result);
    }
}