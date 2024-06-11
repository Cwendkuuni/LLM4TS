import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Test cases
        String sequence1 = "hello";
        int expected1 = 5;
        int result1 = Utf8.encodedLength(sequence1);
        assertEquals(expected1, result1);
        
        String sequence2 = "world";
        int expected2 = 5;
        int result2 = Utf8.encodedLength(sequence2);
        assertEquals(expected2, result2);
        
        String sequence3 = "";
        int expected3 = 0;
        int result3 = Utf8.encodedLength(sequence3);
        assertEquals(expected3, result3);
        
        // Additional test cases
        
        // Testing for character with code point greater than 128
        String sequence4 = "©";
        int expected4 = 2;
        int result4 = Utf8.encodedLength(sequence4);
        assertEquals(expected4, result4);
        
        // Testing for surrogate pairs
        String sequence5 = "𐐷";
        int expected5 = 4;
        int result5 = Utf8.encodedLength(sequence5);
        assertEquals(expected5, result5);
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        // Test cases
        String sequence1 = "hello";
        int start1 = 0;
        int expected1 = 5;
        int result1 = Utf8.encodedLengthGeneral(sequence1, start1);
        assertEquals(expected1, result1);
        
        String sequence2 = "world";
        int start2 = 0;
        int expected2 = 5;
        int result2 = Utf8.encodedLengthGeneral(sequence2, start2);
        assertEquals(expected2, result2);
        
        String sequence3 = "";
        int start3 = 0;
        int expected3 = 0;
        int result3 = Utf8.encodedLengthGeneral(sequence3, start3);
        assertEquals(expected3, result3);
        
        // Additional test cases
        
        // Testing for character with code point greater than 128
        String sequence4 = "©";
        int start4 = 0;
        int expected4 = 2;
        int result4 = Utf8.encodedLengthGeneral(sequence4, start4);
        assertEquals(expected4, result4);
        
        // Testing for surrogate pairs
        String sequence5 = "𐐷";
        int start5 = 0;
        int expected5 = 4;
        int result5 = Utf8.encodedLengthGeneral(sequence5, start5);
        assertEquals(expected5, result5);
    }
    
    @Test
    public void testIsWellFormed() {
        // Test cases
        byte[] bytes1 = {(byte)0x68, (byte)0x65, (byte)0x6c, (byte)0x6c, (byte)0x6f};
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormed(bytes1);
        assertEquals(expected1, result1);
        
        byte[] bytes2 = {(byte)0x77, (byte)0x6f, (byte)0x72, (byte)0x6c, (byte)0x64};
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormed(bytes2);
        assertEquals(expected2, result2);
        
        byte[] bytes3 = new byte[] {};
        boolean expected3 = true;
        boolean result3 = Utf8.isWellFormed(bytes3);
        assertEquals(expected3, result3);
        
        // Additional test cases
        
        // Testing for invalid byte with value -1
        byte[] bytes4 = {(byte)0x68, (byte)0x65, (byte)0x6c, (byte)0x6c, (byte)-1};
        boolean expected4 = false;
        boolean result4 = Utf8.isWellFormed(bytes4);
        assertEquals(expected4, result4);
        
        // Testing for valid byte sequence
        byte[] bytes5 = {(byte)0xc2, (byte)0xa9};
        boolean expected5 = true;
        boolean result5 = Utf8.isWellFormed(bytes5);
        assertEquals(expected5, result5);
    }
    
    @Test
    public void testIsWellFormedWithOffAndLen() {
        // Test cases
        byte[] bytes1 = {(byte)0x68, (byte)0x65, (byte)0x6c, (byte)0x6c, (byte)0x6f};
        int off1 = 0;
        int len1 = 5;
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormed(bytes1, off1, len1);
        assertEquals(expected1, result1);
        
        byte[] bytes2 = {(byte)0x77, (byte)0x6f, (byte)0x72, (byte)0x6c, (byte)0x64};
        int off2 = 0;
        int len2 = 5;
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormed(bytes2, off2, len2);
        assertEquals(expected2, result2);
        
        byte[] bytes3 = new byte[] {};
        int off3 = 0;
        int len3 = 0;
        boolean expected3 = true;
        boolean result3 = Utf8.isWellFormed(bytes3, off3, len3);
        assertEquals(expected3, result3);
        
        // Additional test cases
        
        // Testing for invalid range
        byte[] bytes4 = {(byte)0x68, (byte)0x65, (byte)0x6c, (byte)0x6c, (byte)0x6f};
        int off4 = -1;
        int len4 = 5;
        boolean expected4 = false;
        boolean result4 = Utf8.isWellFormed(bytes4, off4, len4);
        assertEquals(expected4, result4);
        
        // Testing for valid range with invalid byte
        byte[] bytes5 = {(byte)0x68, (byte)0x65, (byte)0x6c, (byte)0x6c, (byte)-1};
        int off5 = 0;
        int len5 = 5;
        boolean expected5 = false;
        boolean result5 = Utf8.isWellFormed(bytes5, off5, len5);
        assertEquals(expected5, result5);
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Test cases
        byte[] bytes1 = {(byte)0x68, (byte)0x65, (byte)0x6c, (byte)0x6c, (byte)0x6f};
        int off1 = 0;
        int end1 = 5;
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormedSlowPath(bytes1, off1, end1);
        assertEquals(expected1, result1);
        
        byte[] bytes2 = {(byte)0x77, (byte)0x6f, (byte)0x72, (byte)0x6c, (byte)0x64};
        int off2 = 0;
        int end2 = 5;
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormedSlowPath(bytes2, off2, end2);
        assertEquals(expected2, result2);
        
        byte[] bytes3 = new byte[] {};
        int off3 = 0;
        int end3 = 0;
        boolean expected3 = true;
        boolean result3 = Utf8.isWellFormedSlowPath(bytes3, off3, end3);
        assertEquals(expected3, result3);
        
        // Additional test cases
        
        // Testing for invalid range
        byte[] bytes4 = {(byte)0x68, (byte)0x65, (byte)0x6c, (byte)0x6c, (byte)0x6f};
        int off4 = -1;
        int end4 = 5;
        boolean expected4 = false;
        boolean result4 = Utf8.isWellFormedSlowPath(bytes4, off4, end4);
        assertEquals(expected4, result4);
        
        // Testing for valid range with invalid byte
        byte[] bytes5 = {(byte)0x68, (byte)0x65, (byte)0x6c, (byte)0x6c, (byte)-1};
        int off5 = 0;
        int end5 = 5;
        boolean expected5 = false;
        boolean result5 = Utf8.isWellFormedSlowPath(bytes5, off5, end5);
        assertEquals(expected5, result5);
    }
}