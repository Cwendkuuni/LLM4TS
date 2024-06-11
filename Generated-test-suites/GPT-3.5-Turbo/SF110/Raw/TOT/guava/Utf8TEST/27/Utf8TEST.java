import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_emptyString() {
        int result = Utf8.encodedLength("");
        assertEquals(0, result);
    }
    
    @Test
    public void testEncodedLength_oneByteCharacter() {
        int result = Utf8.encodedLength("a");
        assertEquals(1, result);
    }
    
    @Test
    public void testEncodedLength_multipleByteCharacter() {
        int result = Utf8.encodedLength("€");
        assertEquals(3, result);
    }
    
    @Test
    public void testEncodedLength_invalidCharacter() {
        try {
            Utf8.encodedLength("\uD83D\uDE00"); // Invalid surrogate pair
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 0", e.getMessage());
        }
    }
    
    @Test
    public void testIsWellFormed_emptyByteArray() {
        byte[] bytes = new byte[0];
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_validByteArray() {
        byte[] bytes = {0, 65, 127, -1};
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_invalidByteArray() {
        byte[] bytes = {0, -1, 127, -1};
        boolean result = Utf8.isWellFormed(bytes);
        assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_invalidByteArrayWithSlowPath() {
        byte[] bytes = {0, -33, -64, -65};
        boolean result = Utf8.isWellFormed(bytes);
        assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_partialByteArray() {
        byte[] bytes = {0, -33, -64};
        boolean result = Utf8.isWellFormed(bytes);
        assertFalse(result);
    }
    
}