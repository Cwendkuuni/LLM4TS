import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Test case 1: Empty sequence
        assertEquals(0, Utf8.encodedLength(""));

        // Test case 2: Single character sequence
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(1, Utf8.encodedLength("Z"));
        assertEquals(1, Utf8.encodedLength("9"));

        // Test case 3: ASCII characters
        assertEquals(10, Utf8.encodedLength("abcdefghijklmnopqrstuvwxyz"));
        assertEquals(10, Utf8.encodedLength("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));

        // Test case 4: Non-ASCII characters
        assertEquals(6, Utf8.encodedLength("áéíóú"));
        assertEquals(10, Utf8.encodedLength("ßäëïöü"));
        assertEquals(9, Utf8.encodedLength("©®¥¢"));
    }
    
    @Test
    public void testIsWellFormed() {
        // Test case 1: Empty byte array
        assertTrue(Utf8.isWellFormed(new byte[] {}));

        // Test case 2: Well-formed byte array
        assertTrue(Utf8.isWellFormed(new byte[] { 97, 98, 99, 100 })); // "abcd"
        assertTrue(Utf8.isWellFormed(new byte[] { -61, -93, -61, -92, -61, -91, -61, -90 })); // "áéíóú"
        assertTrue(Utf8.isWellFormed(new byte[] { -17, -69, -65, -17, -65, -67 })); // "©®"

        // Test case 3: Malformed byte array
        assertFalse(Utf8.isWellFormed(new byte[] { -61, 93, -61, -92, -61, -91, -61, -90 })); // Invalid continuation byte
        assertFalse(Utf8.isWellFormed(new byte[] { -65, -93, -61, -92, -61, -91, -61, -90 })); // Invalid start byte
        assertFalse(Utf8.isWellFormed(new byte[] { -17, -69, -65, -17, -65, -30 })); // Invalid continuation byte
    }
    
}