import static org.junit.Assert.*;
import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        // Basic test case
        String message = "<script>alert(\"Hello, world!\");</script>";
        String expected = "&lt;script&gt;alert(&quot;Hello, world!&quot;);&lt;/script&gt;";
        assertEquals(expected, HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_NullInput() {
        // Test case for null input
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilter_EmptyInput() {
        // Test case for empty input
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilter_EdgeCases() {
        // Test case for special characters in input
        String message = "&&lt;&gt;&quot;";
        String expected = "&amp;&amp;lt;&amp;gt;&amp;quot;";
        assertEquals(expected, HTMLFilter.filter(message));
        
        // Test case for long input
        message = generateLongInput(100000);
        expected = generateLongOutput(100000);
        assertEquals(expected, HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilter_ExceptionHandling() {
        // Test case for exception handling
        // No specific exception to test in this class
    }
    
    // Helper methods for generating long strings
    private String generateLongInput(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("<script>");
        }
        return sb.toString();
    }
    
    private String generateLongOutput(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("&lt;script&gt;");
        }
        return sb.toString();
    }
}