import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Typical use case
        assertEquals(5, Utf8.encodedLength("Hello"));
        
        // Edge case
        assertEquals(0, Utf8.encodedLength(""));
        
        // Error scenario
        try {
            Utf8.encodedLength(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("UTF-8 length does not fit in int: -4294967296", e.getMessage());
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Typical use case
        byte[] bytes1 = {72, 101, 108, 108, 111};
        assertTrue(Utf8.isWellFormed(bytes1));
        
        // Edge case
        byte[] bytes2 = {};
        assertTrue(Utf8.isWellFormed(bytes2));
        
        // Error scenario
        byte[] bytes3 = {(byte)-128, (byte)0};
        assertFalse(Utf8.isWellFormed(bytes3));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Typical use case
        byte[] bytes1 = {72, 101, 108, 108, 111};
        assertTrue(Utf8.isWellFormed(bytes1, 0, 5));
        
        // Edge case
        byte[] bytes2 = {};
        assertTrue(Utf8.isWellFormed(bytes2, 0, 0));
        
        // Error scenario
        byte[] bytes3 = {(byte)-128, (byte)0};
        assertFalse(Utf8.isWellFormed(bytes3, 0, 2));
    }
}