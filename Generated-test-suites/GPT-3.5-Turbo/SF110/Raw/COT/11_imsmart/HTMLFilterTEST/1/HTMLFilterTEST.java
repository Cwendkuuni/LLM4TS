import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnsNull() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }
    
    @Test
    public void testFilter_EmptyString_ReturnsEmptyString() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }
    
    @Test
    public void testFilter_NoTags_ReturnsSameString() {
        String input = "This is a test message.";
        String result = HTMLFilter.filter(input);
        assertEquals(input, result);
    }
    
    @Test
    public void testFilter_WithTags_ReturnsFilteredString() {
        String input = "<p>Test message</p>";
        String expected = "&lt;p&gt;Test message&lt;/p&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_WithAmpersand_ReturnsFilteredString() {
        String input = "AT&amp;T";
        String expected = "AT&amp;amp;T";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_WithQuotationMark_ReturnsFilteredString() {
        String input = "She said, \"Hello\"";
        String expected = "She said, &quot;Hello&quot;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
}