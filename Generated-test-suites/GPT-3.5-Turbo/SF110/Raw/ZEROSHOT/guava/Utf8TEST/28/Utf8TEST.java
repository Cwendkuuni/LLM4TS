import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Test case 1: sequence contains only ASCII characters
        String sequence1 = "Hello World";
        assertEquals(11, Utf8.encodedLength(sequence1));
        
        // Test case 2: sequence contains UTF-8 characters
        String sequence2 = "你好，世界！";
        assertEquals(18, Utf8.encodedLength(sequence2));
        
        // Test case 3: sequence is empty
        String sequence3 = "";
        assertEquals(0, Utf8.encodedLength(sequence3));
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        // Test case 1: sequence contains only ASCII characters
        String sequence1 = "Hello World";
        assertEquals(11, Utf8.encodedLengthGeneral(sequence1, 0));
        
        // Test case 2: sequence contains UTF-8 characters
        String sequence2 = "你好，世界！";
        assertEquals(18, Utf8.encodedLengthGeneral(sequence2, 0));
        
        // Test case 3: sequence is empty
        String sequence3 = "";
        assertEquals(0, Utf8.encodedLengthGeneral(sequence3, 0));
    }
    
    @Test
    public void testIsWellFormed() {
        // Test case 1: bytes are well-formed
        byte[] bytes1 = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        assertTrue(Utf8.isWellFormed(bytes1));
        
        // Test case 2: bytes are not well-formed
        byte[] bytes2 = {72, 101, 108, 108, -128, 87, 111, 114, 108, 100};
        assertFalse(Utf8.isWellFormed(bytes2));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test case 1: bytes are well-formed
        byte[] bytes1 = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        int off1 = 0;
        int len1 = 11;
        assertTrue(Utf8.isWellFormed(bytes1, off1, len1));
        
        // Test case 2: bytes are not well-formed
        byte[] bytes2 = {72, 101, 108, 108, -128, 87, 111, 114, 108, 100};
        int off2 = 0;
        int len2 = 10;
        assertFalse(Utf8.isWellFormed(bytes2, off2, len2));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Test case 1: bytes are well-formed
        byte[] bytes1 = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        int off1 = 0;
        int end1 = 11;
        assertTrue(Utf8.isWellFormedSlowPath(bytes1, off1, end1));
        
        // Test case 2: bytes are not well-formed
        byte[] bytes2 = {72, 101, 108, 108, -128, 87, 111, 114, 108, 100};
        int off2 = 0;
        int end2 = 10;
        assertFalse(Utf8.isWellFormedSlowPath(bytes2, off2, end2));
    }
}