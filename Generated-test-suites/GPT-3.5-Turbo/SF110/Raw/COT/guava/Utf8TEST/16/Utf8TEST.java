import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Basic functionality test
        assertEquals(4, Utf8.encodedLength("test"));
        
        // Edge case: empty string
        assertEquals(0, Utf8.encodedLength(""));
        
        // Edge case: string with all ASCII characters
        assertEquals(10, Utf8.encodedLength("abcdefghijklmnopqrstuvwxyz"));
        
        // Edge case: string with non-ASCII characters
        assertEquals(7, Utf8.encodedLength("é"));
        
        // Exception test: input length exceeds int range
        try {
            Utf8.encodedLength("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("UTF-8 length does not fit in int: 130", e.getMessage());
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Basic functionality test
        assertTrue(Utf8.isWellFormed(new byte[]{0x74, 0x65, 0x73, 0x74}));
        
        // Edge case: empty byte array
        assertTrue(Utf8.isWellFormed(new byte[]{}));
        
        // Edge case: well-formed UTF-8 byte array
        assertTrue(Utf8.isWellFormed(new byte[]{0xC3, 0xA9}));
        
        // Edge case: ill-formed UTF-8 byte array
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xC3, (byte) 0xFE}));
        
        // Exception test: invalid byte sequence
        try {
            Utf8.isWellFormed(new byte[]{(byte) 0xC3});
            fail("Expected IllegalArgumentException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 1, Size: 1", e.getMessage());
        }
    }
}