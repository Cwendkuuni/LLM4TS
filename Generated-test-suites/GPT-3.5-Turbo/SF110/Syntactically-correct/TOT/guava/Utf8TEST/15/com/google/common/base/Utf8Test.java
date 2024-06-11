
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_typical() {
        // Typical use case
        String input = "Hello World!";
        int expectedOutput = 12;
        
        int output = Utf8.encodedLength(input);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEncodedLength_emptyString() {
        // Edge case: Empty string
        String input = "";
        int expectedOutput = 0;
        
        int output = Utf8.encodedLength(input);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEncodedLength_singleChar() {
        // Edge case: Single character
        String input = "A";
        int expectedOutput = 1;
        
        int output = Utf8.encodedLength(input);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEncodedLength_error() {
        // Error scenario: UTF-8 length does not fit in int
        String input = "😀"; // U+1F600
        assertThrows(IllegalArgumentException.class, () -> {
            Utf8.encodedLength(input);
        });
    }
    
    @Test
    public void testEncodedLengthGeneral_typical() {
        // Typical use case
        String input = "Hello World!";
        int start = 0;
        int expectedOutput = 12;
        
        int output = Utf8.encodedLengthGeneral(input, start);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEncodedLengthGeneral_emptyString() {
        // Edge case: Empty string
        String input = "";
        int start = 0;
        int expectedOutput = 0;
        
        int output = Utf8.encodedLengthGeneral(input, start);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEncodedLengthGeneral_singleChar() {
        // Edge case: Single character
        String input = "A";
        int start = 0;
        int expectedOutput = 1;
        
        int output = Utf8.encodedLengthGeneral(input, start);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEncodedLengthGeneral_error() {
        // Error scenario: Unpaired surrogate
        String input = "A\uD800"; // A + unpaired surrogate
        int start = 0;
        assertThrows(IllegalArgumentException.class, () -> {
            Utf8.encodedLengthGeneral(input, start);
        });
    }
    
    @Test
    public void testIsWellFormed_typical() {
        // Typical use case
        byte[] bytes = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 }; // "Hello World!"
        boolean expectedOutput = true;
        
        boolean output = Utf8.isWellFormed(bytes);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testIsWellFormed_emptyArray() {
        // Edge case: Empty array
        byte[] bytes = {};
        boolean expectedOutput = true;
        
        boolean output = Utf8.isWellFormed(bytes);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testIsWellFormed_error() {
        // Error scenario: Invalid UTF-8 byte sequence
        byte[] bytes = { -61, -101 }; // Invalid UTF-8 sequence
        boolean expectedOutput = false;
        
        boolean output = Utf8.isWellFormed(bytes);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testIsWellFormedSlowPath_typical() {
        // Typical use case
        byte[] bytes = { -43, -67, -43, -77 }; // "こんにちは" (Konnichiwa in Japanese)
        int off = 0;
        int end = 4;
        boolean expectedOutput = true;
        
        boolean output = Utf8.isWellFormedSlowPath(bytes, off, end);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testIsWellFormedSlowPath_emptyArray() {
        // Edge case: Empty array
        byte[] bytes = {};
        int off = 0;
        int end = 0;
        boolean expectedOutput = true;
        
        boolean output = Utf8.isWellFormedSlowPath(bytes, off, end);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testIsWellFormedSlowPath_error() {
        // Error scenario: Invalid UTF-8 byte sequence
        byte[] bytes = { -61, -101 }; // Invalid UTF-8 sequence
        int off = 0;
        int end = 2;
        boolean expectedOutput = false;
        
        boolean output = Utf8.isWellFormedSlowPath(bytes, off, end);
        
        assertEquals(expectedOutput, output);
    }
}

