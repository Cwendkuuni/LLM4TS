import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Test when sequence has length 0
        assertEquals(0, Utf8.encodedLength(""));
        
        // Test when sequence has length 1 and contains a character < 0x80
        assertEquals(1, Utf8.encodedLength("a"));
        
        // Test when sequence has length 1 and contains a character >= 0x80
        assertEquals(2, Utf8.encodedLength("ā"));
        
        // Test when sequence has length > 1 and contains only characters < 0x80
        assertEquals(5, Utf8.encodedLength("hello"));
        
        // Test when sequence has length > 1 and contains at least one character >= 0x80
        assertEquals(6, Utf8.encodedLength("héllo"));
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        String sequence = "hello";
        // Test when start index is 0
        assertEquals(0, Utf8.encodedLengthGeneral(sequence, 0));
        
        // Test when start index is in the middle of the sequence
        assertEquals(5, Utf8.encodedLengthGeneral(sequence, 2));
    }
    
    @Test
    public void testIsWellFormed() {
        byte[] bytes = new byte[] {65, 66, 67};
        // Test when bytes is well formed
        assertTrue(Utf8.isWellFormed(bytes));
        
        // Test when bytes is not well formed
        bytes = new byte[] {65, -1, 67};
        assertFalse(Utf8.isWellFormed(bytes));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] bytes = new byte[] {65, 66, 67, -1};
        int off = 1;
        int len = 2;
        // Test when bytes is well formed within the given offset and length
        assertTrue(Utf8.isWellFormed(bytes, off, len));
        
        // Test when bytes is not well formed within the given offset and length
        off = 0;
        len = 3;
        assertFalse(Utf8.isWellFormed(bytes, off, len));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = new byte[] {65, 66, 67};
        int off = 0;
        int end = 3;
        // Test when bytes is well formed in the slow path
        assertTrue(Utf8.isWellFormedSlowPath(bytes, off, end));
        
        // Test when bytes is not well formed in the slow path
        bytes = new byte[] {65, -1, 67};
        assertFalse(Utf8.isWellFormedSlowPath(bytes, off, end));
    }
}