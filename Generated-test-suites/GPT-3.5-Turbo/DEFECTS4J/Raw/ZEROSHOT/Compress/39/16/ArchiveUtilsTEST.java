import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("main.c", 2000, false);
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));
        
        ArchiveEntry entry2 = new ArchiveEntry("testfiles", 100, true);
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = expected.getBytes();
        
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer("test2", buffer));
    }
    
    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = inputString.getBytes();
        
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
   }
    
    @Test
    public void testToAsciiString() throws UnsupportedEncodingException {
        byte[] inputBytes = "test".getBytes();
        String expected = new String(inputBytes, "US-ASCII");
        
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }
    
    @Test
    public void testToAsciiStringWithOffsetAndLength() throws UnsupportedEncodingException {
        byte[] inputBytes = "test".getBytes();
        String expected = new String(inputBytes, 1, 2, "US-ASCII");
        
        String result = ArchiveUtils.toAsciiString(inputBytes, 1, 2);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test1".getBytes();
        byte[] buffer3 = "test2".getBytes();
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }
    
    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test1".getBytes();
        byte[] buffer3 = "test2".getBytes();
        
        assertTrue(ArchiveUtils.isEqual(buffer1, 1, 3, buffer2, 1, 3));
        assertFalse(ArchiveUtils.isEqual(buffer1, 1, 3, buffer3, 1, 3));
    }
    
    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test\0\0\0".getBytes();
        
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }
    
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test\0\0\0".getBytes();
        
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length-1));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[5];
        
        assertTrue(ArchiveUtils.isArrayZero(buffer, 5));
        
        buffer[3] = 1;
        assertFalse(ArchiveUtils.isArrayZero(buffer, 5));
    }
    
    @Test
    public void testSanitize() {
        String inputString = "Test\u0000String";
        String expected = "Test?String";
        
        String result = ArchiveUtils.sanitize(inputString);
        assertEquals(expected, result);
    }

}