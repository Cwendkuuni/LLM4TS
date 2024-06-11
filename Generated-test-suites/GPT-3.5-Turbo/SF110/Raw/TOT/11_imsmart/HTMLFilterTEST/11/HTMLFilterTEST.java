import org.junit.*;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        // Typical use case - message with HTML special characters
        String message = "<html>";
        String expectedResult = "&lt;html&gt;";
        assertEquals(expectedResult, HTMLFilter.filter(message));
        
        // Edge case - message with maximum allowed length
        message = generateString(65535, 'a');
        expectedResult = generateString(65535, 'a');
        assertEquals(expectedResult, HTMLFilter.filter(message));
        
        // Error scenario - message is null
        message = null;
        assertNull(HTMLFilter.filter(message));
        
        // Error scenario - empty message
        message = "";
        assertEquals("", HTMLFilter.filter(message));
        
        // Error scenario - message with special characters (%$#&@)
        message = "Hello % World $ Test #";
        expectedResult = "Hello % World $ Test #";
        assertEquals(expectedResult, HTMLFilter.filter(message));
    }
    
    // Helper method to generate string of given length with given character
    private String generateString(int length, char character) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(character);
        }
        return sb.toString();
    }
}