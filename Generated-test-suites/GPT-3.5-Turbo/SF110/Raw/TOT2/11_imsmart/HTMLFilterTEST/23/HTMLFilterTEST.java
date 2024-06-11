import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_NullMessage() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilter_EmptyMessage() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilter_ValidMessage() {
        String input = "<p>Hello, World!</p>";
        String expected = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        assertEquals(expected, HTMLFilter.filter(input));
    }
    
    @Test
    public void testFilter_EdgeCases() {
        // Test special characters
        String inputSpecialChars = "<>&\"";
        String expectedSpecialChars = "&lt;&gt;&amp;&quot;";
        assertEquals(expectedSpecialChars, HTMLFilter.filter(inputSpecialChars));
        
        // Test long message
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            stringBuilder.append("a");
        }
        String longMessage = stringBuilder.toString();
        assertEquals(longMessage, HTMLFilter.filter(longMessage)); // No HTML tags
        
        // Test message with only special characters
        String inputOnlySpecialChars = "<>&\"";
        String expectedOnlySpecialChars = "&lt;&gt;&amp;&quot;";
        assertEquals(expectedOnlySpecialChars, HTMLFilter.filter(inputOnlySpecialChars));
    }
    
    @Test
    public void testFilter_ExceptionHandling() {
        // Test message with invalid characters
        String inputInvalidChars = "hello\b world";
        String expectedInvalidChars = "hello\b world"; // Backspace character remains unchanged
        assertEquals(expectedInvalidChars, HTMLFilter.filter(inputInvalidChars));
    }
}