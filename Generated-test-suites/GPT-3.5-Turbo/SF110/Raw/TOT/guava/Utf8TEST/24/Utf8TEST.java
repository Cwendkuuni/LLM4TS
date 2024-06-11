import static org.junit.Assert.*;
import org.junit.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Typical use case: check the encoded length of a string with ASCII characters only
        String input = "Hello, World!";
        int expected = 13;
        int result = Utf8.encodedLength(input);
        assertEquals(expected, result);
        
        // Edge case: check the encoded length of an empty string
        input = "";
        expected = 0;
        result = Utf8.encodedLength(input);
        assertEquals(expected, result);
        
        // Error scenario: check that an IllegalArgumentException is thrown when the encoded length exceeds Integer.MAX_VALUE
        input = "áéíóú";
        try {
            Utf8.encodedLength(input);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "UTF-8 length does not fit in int: 4294967302";
            String resultMessage = e.getMessage();
            assertEquals(expectedMessage, resultMessage);
        }
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        // Typical use case: check the encoded length of a string with non-ASCII characters
        String input = "áéíóú";
        int start = 0;
        int expected = 10;
        int result = Utf8.encodedLengthGeneral(input, start);
        assertEquals(expected, result);
        
        // Edge case: check the encoded length of a string with surrogate pairs
        input = "😀";
        expected = 4;
        result = Utf8.encodedLengthGeneral(input, start);
        assertEquals(expected, result);
        
        // Error scenario: check that an IllegalArgumentException is thrown when an unpaired surrogate is found
        input = "abc\uD83D";
        start = 3;
        try {
            Utf8.encodedLengthGeneral(input, start);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Unpaired surrogate at index 3";
            String resultMessage = e.getMessage();
            assertEquals(expectedMessage, resultMessage);
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Typical use case: check that a well-formed byte array returns true
        byte[] bytes = {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33};
        boolean expected = true;
        boolean result = Utf8.isWellFormed(bytes);
        assertEquals(expected, result);
        
        // Edge case: check that an empty byte array returns true
        bytes = new byte[0];
        expected = true;
        result = Utf8.isWellFormed(bytes);
        assertEquals(expected, result);
        
        // Error scenario: check that an ill-formed byte array returns false
        bytes = new byte[] {-61, -87, -90, -120};
        expected = false;
        result = Utf8.isWellFormed(bytes);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Typical use case: check that a well-formed byte array with offset and length returns true
        byte[] bytes = {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33};
        int off = 0;
        int len = 6;
        boolean expected = true;
        boolean result = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, result);
        
        // Edge case: check that an empty byte array with offset and length returns true
        bytes = new byte[0];
        off = 0;
        len = 0;
        expected = true;
        result = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, result);
        
        // Error scenario: check that an ill-formed byte array with offset and length returns false
        bytes = new byte[] {-61, -87, -90, -120};
        off = 0;
        len = 4;
        expected = false;
        result = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Typical use case: check that a well-formed byte array in the slow path returns true
        byte[] bytes = {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33};
        int off = 0;
        int end = 13;
        boolean expected = true;
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, result);
        
        // Edge case: check that an empty byte array in the slow path returns true
        bytes = new byte[0];
        off = 0;
        end = 0;
        expected = true;
        result = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, result);
        
        // Error scenario: check that an ill-formed byte array in the slow path returns false
        bytes = new byte[] {-61, -87, -90, -120};
        off = 0;
        end = 4;
        expected = false;
        result = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, result);
    }
}