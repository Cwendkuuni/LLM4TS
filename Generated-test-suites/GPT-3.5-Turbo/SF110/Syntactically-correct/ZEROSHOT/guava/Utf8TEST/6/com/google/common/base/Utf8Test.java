import org.junit.*;
import static org.junit.Assert.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Test case 1
        String sequence1 = "Hello";
        int expected1 = 5;
        int actual1 = Utf8.encodedLength(sequence1);
        assertEquals(expected1, actual1);
        
        // Test case 2
        String sequence2 = "こんにちは";
        int expected2 = 15;
        int actual2 = Utf8.encodedLength(sequence2);
        assertEquals(expected2, actual2);
        
        // Test case 3
        String sequence3 = "";
        int expected3 = 0;
        int actual3 = Utf8.encodedLength(sequence3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        // Test case 1
        String sequence1 = "Hello";
        int start1 = 0;
        int expected1 = 5;
        int actual1 = Utf8.encodedLengthGeneral(sequence1, start1);
        assertEquals(expected1, actual1);
        
        // Test case 2
        String sequence2 = "こんにちは";
        int start2 = 5;
        int expected2 = 6;
        int actual2 = Utf8.encodedLengthGeneral(sequence2, start2);
        assertEquals(expected2, actual2);
        
        // Test case 3
        String sequence3 = "";
        int start3 = 0;
        int expected3 = 0;
        int actual3 = Utf8.encodedLengthGeneral(sequence3, start3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testIsWellFormed() {
        // Test case 1
        byte[] bytes1 = { 72, 101, 108, 108, 111 };
        boolean expected1 = true;
        boolean actual1 = Utf8.isWellFormed(bytes1);
        assertEquals(expected1, actual1);
        
        // Test case 2
        byte[] bytes2 = { -30, -128, -101, -29, -127, -97 };
        boolean expected2 = true;
        boolean actual2 = Utf8.isWellFormed(bytes2);
        assertEquals(expected2, actual2);
        
        // Test case 3
        byte[] bytes3 = { -30, -128, -101, 0, -29, -127, -97 };
        boolean expected3 = false;
        boolean actual3 = Utf8.isWellFormed(bytes3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test case 1
        byte[] bytes1 = { 72, 101, 108, 108, 111 };
        int off1 = 0;
        int len1 = 5;
        boolean expected1 = true;
        boolean actual1 = Utf8.isWellFormed(bytes1, off1, len1);
        assertEquals(expected1, actual1);
        
        // Test case 2
        byte[] bytes2 = { -30, -128, -101, -29, -127, -97 };
        int off2 = 0;
        int len2 = 6;
        boolean expected2 = true;
        boolean actual2 = Utf8.isWellFormed(bytes2, off2, len2);
        assertEquals(expected2, actual2);
        
        // Test case 3
        byte[] bytes3 = { -30, -128, -101, 0, -29, -127, -97 };
        int off3 = 0;
        int len3 = 7;
        boolean expected3 = false;
        boolean actual3 = Utf8.isWellFormed(bytes3, off3, len3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Test case 1
        byte[] bytes1 = { 72, 101, 108, 108, 111 };
        int off1 = 0;
        int end1 = 5;
        boolean expected1 = true;
        boolean actual1 = Utf8.isWellFormedSlowPath(bytes1, off1, end1);
        assertEquals(expected1, actual1);
        
        // Test case 2
        byte[] bytes2 = { -30, -128, -101, -29, -127, -97 };
        int off2 = 0;
        int end2 = 6;
        boolean expected2 = true;
        boolean actual2 = Utf8.isWellFormedSlowPath(bytes2, off2, end2);
        assertEquals(expected2, actual2);
        
        // Test case 3
        byte[] bytes3 = { -30, -128, -101, 0, -29, -127, -97 };
        int off3 = 0;
        int end3 = 7;
        boolean expected3 = false;
        boolean actual3 = Utf8.isWellFormedSlowPath(bytes3, off3, end3);
        assertEquals(expected3, actual3);
    }
}