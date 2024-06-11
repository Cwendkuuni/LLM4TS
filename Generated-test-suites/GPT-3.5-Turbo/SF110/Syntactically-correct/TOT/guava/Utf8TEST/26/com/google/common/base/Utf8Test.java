
import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength_typical() {
        // Typical use case
        String input = "Hello World";
        
        int expected = 11;
        int actual = Utf8.encodedLength(input);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEncodedLength_edge() {
        // Empty string
        String input1 = "";
        int expected1 = 0;
        int actual1 = Utf8.encodedLength(input1);
        assertEquals(expected1, actual1);
        
        // String with all ASCII characters
        String input2 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_-+={[}]|\\:;\"'<>,.?/~`";
        int expected2 = 93;
        int actual2 = Utf8.encodedLength(input2);
        assertEquals(expected2, actual2);
        
        // String with non-ASCII characters
        String input3 = "你好";
        int expected3 = 6;
        int actual3 = Utf8.encodedLength(input3);
        assertEquals(expected3, actual3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_error() {
        // String length exceeding int limit
        String input = "a".repeat(Integer.MAX_VALUE);
        Utf8.encodedLength(input);
        
        // Unpaired surrogate
        String input2 = "\uD800";
        Utf8.encodedLength(input2);
    }
    
    @Test
    public void testIsWellFormed_typical() {
        // Typical use case
        byte[] input = { 72, 101, 108, 108, 111 };
        
        assertTrue(Utf8.isWellFormed(input));
    }
    
    @Test
    public void testIsWellFormed_edge() {
        // Empty byte array
        byte[] input1 = {};
        assertTrue(Utf8.isWellFormed(input1));
        
        // One byte character
        byte[] input2 = { 65 };  // 'A' in ASCII
        assertTrue(Utf8.isWellFormed(input2));
        
        // Multibyte character
        byte[] input3 = { -30, -128, -100 };  // '你' in UTF-8
        assertTrue(Utf8.isWellFormed(input3));
    }
    
    @Test
    public void testIsWellFormed_error() {
        // Invalid byte sequence
        byte[] input1 = { -90, 65 };  // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(input1));
        
        byte[] input2 = { -30, 40, -100 };  // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(input2));
        
        byte[] input3 = { -50, -115, -100 };  // Invalid UTF-8 sequence
        assertFalse(Utf8.isWellFormed(input3));
    }
}
