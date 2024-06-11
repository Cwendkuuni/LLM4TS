import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Basic test case
        CharSequence sequence = "Hello, World!";
        int expectedLength = 14;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
        
        // Edge case: empty sequence
        sequence = "";
        expectedLength = 0;
        actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
        
        // Edge case: sequence with special characters
        sequence = "éçñ";
        expectedLength = 6;
        actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
        
        // Exception handling: utf8Length > utf16Length
        sequence = new StringBuilder().appendCodePoint(0x1F602).toString();
        try {
            Utf8.encodedLength(sequence);
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "UTF-8 length does not fit in int: 4294967297";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
        
        // Exception handling: unpaired surrogate
        sequence = new StringBuilder().appendCodePoint(0xD83D).toString();
        try {
            Utf8.encodedLength(sequence);
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Unpaired surrogate at index 0";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Basic test case
        byte[] bytes = new byte[] {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
        
        // Edge case: empty byte array
        bytes = new byte[]{};
        expected = true;
        actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
        
        // Exception handling: invalid byte sequence
        bytes = new byte[] {72, 101, 108, -128, 108, 111};
        expected = false;
        actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Basic test case
        byte[] bytes = new byte[] {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33};
        int off = 0;
        int len = bytes.length;
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, actual);
        
        // Edge case: empty byte array
        bytes = new byte[]{};
        off = 0;
        len = 0;
        expected = true;
        actual = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, actual);
        
        // Exception handling: offset and length out of range
        bytes = new byte[] {72, 101, 108, 108, 111};
        off = 0;
        len = 6;
        try {
            Utf8.isWellFormed(bytes, off, len);
            fail("IllegalArgumentException was not thrown");
        } catch (IndexOutOfBoundsException e) {
            String expectedMessage = "Range [0, 6) out of bounds for length 5";
            String actualMessage = e.getMessage();
            assertEquals(expectedMessage, actualMessage);
        }
        
        // Exception handling: invalid byte sequence
        bytes = new byte[] {72, 101, 108, -128, 108, 111};
        off = 0;
        len = bytes.length;
        expected = false;
        actual = Utf8.isWellFormed(bytes, off, len);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Basic test case
        byte[] bytes = new byte[] {72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33};
        int off = 0;
        int end = bytes.length;
        boolean expected = true;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, actual);
        
        // Edge case: empty byte array
        bytes = new byte[]{};
        off = 0;
        end = 0;
        expected = true;
        actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, actual);
        
        // Exception handling: invalid byte sequence
        bytes = new byte[] {72, 101, 108, -128, 108, 111};
        off = 0;
        end = bytes.length;
        expected = false;
        actual = Utf8.isWellFormedSlowPath(bytes, off, end);
        assertEquals(expected, actual);
    }
}