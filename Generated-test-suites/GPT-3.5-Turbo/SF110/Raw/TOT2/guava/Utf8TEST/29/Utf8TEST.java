import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Typical use case
        assertEquals(6, Utf8.encodedLength("Hello"));
        
        // Edge case: Empty string
        assertEquals(0, Utf8.encodedLength(""));
        
        // Error scenario: Null input
        try {
            Utf8.encodedLength(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
        
        // Additional edge case: String with special characters
        assertEquals(15, Utf8.encodedLength("こんにちは"));
    }
    
    @Test
    public void testIsWellFormed() {
        // Typical use case
        assertTrue(Utf8.isWellFormed(new byte[]{0, 1, 2, 3}));
        
        // Edge case: Negative byte
        assertFalse(Utf8.isWellFormed(new byte[]{0, 1, -2, 3}));
        
        // Error scenario: Out of bounds index
        try {
            Utf8.isWellFormed(new byte[]{0, 1, 2, 3}, 0, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        // Additional edge case: Byte sequence with multi-byte characters
        assertTrue(Utf8.isWellFormed(new byte[]{0, -30, -128, -128, -30, -128, -128}));
    }
}