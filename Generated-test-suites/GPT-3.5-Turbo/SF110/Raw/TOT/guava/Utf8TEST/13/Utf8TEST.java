import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Typical use case
        String input1 = "hello world";
        int expected1 = 11;
        int result1 = Utf8.encodedLength(input1);
        assertEquals(expected1, result1);

        // Edge case: empty string
        String input2 = "";
        int expected2 = 0;
        int result2 = Utf8.encodedLength(input2);
        assertEquals(expected2, result2);

        // Error scenario: input string contains Unicode characters larger than \u0800
        String input3 = "hello world 你好";
        int expected3 = 16;
        int result3 = Utf8.encodedLength(input3);
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        // Typical use case
        String input1 = "hello world";
        int start1 = 0;
        int expected1 = 11;
        int result1 = Utf8.encodedLengthGeneral(input1, start1);
        assertEquals(expected1, result1);

        // Edge case: empty string
        String input2 = "";
        int start2 = 0;
        int expected2 = 0;
        int result2 = Utf8.encodedLengthGeneral(input2, start2);
        assertEquals(expected2, result2);

        // Error scenario: input string contains unpaired surrogate
        String input3 = "hello world\uD835";
        int start3 = 0;
        try {
            Utf8.encodedLengthGeneral(input3, start3);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 11", e.getMessage());
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Typical use case: well-formed bytes
        byte[] input1 = { 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100 };
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormed(input1);
        assertEquals(expected1, result1);

        // Edge case: empty byte array
        byte[] input2 = {};
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormed(input2);
        assertEquals(expected2, result2);

        // Error scenario: byte array contains malformed characters
        byte[] input3 = { -61, -95, -31, -121, -111, 32, 119, 111, 114, 108, 100 };
        boolean expected3 = false;
        boolean result3 = Utf8.isWellFormed(input3);
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Typical use case: well-formed bytes with given offset and length
        byte[] input1 = { -61, -95, -31, -121, -111, 32, 119, 111, 114, 108, 100 };
        int off1 = 0;
        int len1 = 11;
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormed(input1, off1, len1);
        assertEquals(expected1, result1);

        // Edge case: byte array with length 1
        byte[] input2 = { -61 };
        int off2 = 0;
        int len2 = 1;
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormed(input2, off2, len2);
        assertEquals(expected2, result2);

        // Error scenario: byte array with malformed characters
        byte[] input3 = { -61, -95, -31, -121, -111, 32, 119, 111, 114, 108, 100 };
        int off3 = 0;
        int len3 = 12;
        boolean expected3 = false;
        boolean result3 = Utf8.isWellFormed(input3, off3, len3);
        assertEquals(expected3, result3);
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Typical use case: well-formed bytes in slow path
        byte[] input1 = { -61, -95, -31, -121, -111, 32, 119, 111, 114, 108, 100 };
        int off1 = 0;
        int end1 = 11;
        boolean expected1 = true;
        boolean result1 = Utf8.isWellFormedSlowPath(input1, off1, end1);
        assertEquals(expected1, result1);

        // Edge case: byte array with length 1
        byte[] input2 = { -61 };
        int off2 = 0;
        int end2 = 1;
        boolean expected2 = true;
        boolean result2 = Utf8.isWellFormedSlowPath(input2, off2, end2);
        assertEquals(expected2, result2);

        // Error scenario: byte array with malformed characters
        byte[] input3 = { -61, -95, -31, -121, -111, 32, 119, 111, 114, 108, 100 };
        int off3 = 0;
        int end3 = 12;
        boolean expected3 = false;
        boolean result3 = Utf8.isWellFormedSlowPath(input3, off3, end3);
        assertEquals(expected3, result3);
    }

}