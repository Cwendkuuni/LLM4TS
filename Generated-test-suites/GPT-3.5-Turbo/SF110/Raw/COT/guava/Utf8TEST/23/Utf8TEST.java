import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
   
    @Test
    public void testEncodedLength() {
        // Test case 1: Empty sequence
        CharSequence seq1 = "";
        int result1 = Utf8.encodedLength(seq1);
        assertEquals(0, result1);
        
        // Test case 2: Sequence with all ASCII characters
        CharSequence seq2 = "Hello World";
        int result2 = Utf8.encodedLength(seq2);
        assertEquals(11, result2);
        
        // Test case 3: Sequence with non-ASCII characters
        CharSequence seq3 = "こんにちは世界";
        int result3 = Utf8.encodedLength(seq3);
        assertEquals(21, result3);
    }
    
    @Test
    public void testIsWellFormed() {
        // Test case 1: Empty byte array
        byte[] bytes1 = {};
        boolean result1 = Utf8.isWellFormed(bytes1);
        assertTrue(result1);
        
        // Test case 2: Well-formed byte array
        byte[] bytes2 = {(byte) 104, (byte) 101, (byte) 108, (byte) 108, (byte) 111};
        boolean result2 = Utf8.isWellFormed(bytes2);
        assertTrue(result2);
        
        // Test case 3: Malformed byte array
        byte[] bytes3 = {(byte) 228, (byte) 184, (byte) 150, (byte) 231, (byte) 149, (byte) 140};
        boolean result3 = Utf8.isWellFormed(bytes3);
        assertFalse(result3);
    }
}