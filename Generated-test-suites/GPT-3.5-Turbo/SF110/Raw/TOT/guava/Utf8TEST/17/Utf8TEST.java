import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_withEmptyString_returnsZero() {
        // Arrange
        String input = "";
        
        // Act
        int result = Utf8.encodedLength(input);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testEncodedLength_withAsciiString_returnsStringLength() {
        // Arrange
        String input = "Hello World";
        
        // Act
        int result = Utf8.encodedLength(input);
        
        // Assert
        assertEquals(input.length(), result);
    }
    
    @Test
    public void testEncodedLength_withNonAsciiString_returnsCorrectLength() {
        // Arrange
        String input = "こんにちは";
        
        // Act
        int result = Utf8.encodedLength(input);
        
        // Assert
        assertEquals(15, result);
    }
    
    @Test
    public void testEncodedLength_withSurrogatePair_returnsCorrectLength() {
        // Arrange
        String input = "𠜎";
        
        // Act
        int result = Utf8.encodedLength(input);
        
        // Assert
        assertEquals(4, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_withInvalidUtf8Length_throwsIllegalArgumentException() {
        // Arrange
        String input = "𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎𠜎";
        
        // Act
        Utf8.encodedLength(input);
    }
    
    @Test
    public void testIsWellFormed_withWellFormedByteArray_returnsTrue() {
        // Arrange
        byte[] input = new byte[] { 65, 66, 67, 68 };
        
        // Act
        boolean result = Utf8.isWellFormed(input);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_withNonWellFormedByteArray_returnsFalse() {
        // Arrange
        byte[] input = new byte[] { -23, -82, -77, 65 };
        
        // Act
        boolean result = Utf8.isWellFormed(input);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_withPartialByteArray_returnsFalse() {
        // Arrange
        byte[] input = new byte[] { 65, 66, -42, -120, -42, -121 };
        
        // Act
        boolean result = Utf8.isWellFormed(input, 2, 4);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_withEmptyByteArray_returnsTrue() {
        // Arrange
        byte[] input = new byte[0];
        
        // Act
        boolean result = Utf8.isWellFormed(input);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_withValidUtf8ByteArray_returnsTrue() {
        // Arrange
        byte[] input = new byte[] { -30, -128, -96, -30, -128, -95 };
        int off = 0;
        int end = 6;
        
        // Act
        boolean result = Utf8.isWellFormedSlowPath(input, off, end);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_withInvalidUtf8ByteArray_returnsFalse() {
        // Arrange
        byte[] input = new byte[] { -30, -128, -96, -1, 65 };
        int off = 0;
        int end = 5;
        
        // Act
        boolean result = Utf8.isWellFormedSlowPath(input, off, end);
        
        // Assert
        assertFalse(result);
    }
}