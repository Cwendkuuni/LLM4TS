import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Typical use case
        assertEquals(6, Utf8.encodedLength("Hello!"));
        
        // Edge case: empty string
        assertEquals(0, Utf8.encodedLength(""));
        
        // Error scenario: invalid input
        try {
            Utf8.encodedLength(null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Typical use case
        assertEquals(6, Utf8.encodedLengthGeneral("Hello!", 0));
        
        // Edge case: empty string
        assertEquals(0, Utf8.encodedLengthGeneral("", 0));
        
        // Edge case: start index outside range
        assertEquals(0, Utf8.encodedLengthGeneral("Hello!", 10));
    }

    @Test
    public void testIsWellFormed() {
        // Typical use case
        assertTrue(Utf8.isWellFormed(new byte[] {72, 101, 108, 108, 111, 33}));
        
        // Edge case: empty byte array
        assertTrue(Utf8.isWellFormed(new byte[] {}));
        
        // Error scenario: invalid input
        try {
            Utf8.isWellFormed(null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Typical use case
        assertTrue(Utf8.isWellFormed(new byte[] {72, 101, 108, 108, 111, 33}, 0, 6));
        
        // Edge case: offset and length outside range
        assertTrue(Utf8.isWellFormed(new byte[] {72, 101, 108, 108, 111, 33}, 10, 5));
        
        // Edge case: empty byte array
        assertTrue(Utf8.isWellFormed(new byte[] {}, 0, 0));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Typical use case
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {72, 101, 108, 108, 111, 33}, 0, 6));
        
        // Edge case: empty byte array
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {}, 0, 0));
        
        // Edge case: invalid byte sequence
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] {72, -60, 108, -120, 111, 33}, 0, 6));
    }
}