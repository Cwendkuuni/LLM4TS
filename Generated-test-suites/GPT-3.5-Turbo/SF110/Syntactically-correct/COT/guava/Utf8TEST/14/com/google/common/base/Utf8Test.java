
import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength_basic() {
        // Test with empty sequence
        assertEquals(0, Utf8.encodedLength(""));
        
        // Test with ASCII characters only
        assertEquals(5, Utf8.encodedLength("Hello"));
        
        // Test with non-ASCII characters
        assertEquals(8, Utf8.encodedLength("こんにちは"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_largeLength() {
        // Test with a length that exceeds the maximum allowed int value
        Utf8.encodedLength("a".repeat(Integer.MAX_VALUE));
    }
    
    @Test
    public void testIsWellFormed_basic() {
        // Test with empty byte array
        assertTrue(Utf8.isWellFormed(new byte[0]));
        
        // Test with well-formed ASCII characters
        assertTrue(Utf8.isWellFormed(new byte[]{72, 101, 108, 108, 111}));
        
        // Test with well-formed non-ASCII characters
        assertTrue(Utf8.isWellFormed(new byte[]{-29, -127, -109, -29, -126, -102}));
    }
    
    @Test
    public void testIsWellFormed_invalidBytes() {
        // Test with invalid byte sequence
        assertFalse(Utf8.isWellFormed(new byte[]{-61, -120}));
        
        // Test with invalid byte sequence with multiple invalid bytes
        assertFalse(Utf8.isWellFormed(new byte[]{-61, -120, -61, -120}));
        
        // Test with invalid continuation byte
        assertFalse(Utf8.isWellFormed(new byte[]{-61, -1}));
    }
    
    @Test
    public void testIsWellFormedOutOfBounds() {
        // Test with byte array that extends beyond the given offset and length
        assertFalse(Utf8.isWellFormed(new byte[]{72, -61, -120}, 1, 2));
    }
}

