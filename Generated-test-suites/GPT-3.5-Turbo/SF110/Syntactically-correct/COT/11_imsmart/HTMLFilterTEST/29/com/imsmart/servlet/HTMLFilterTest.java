import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilter_NoSpecialCharacters_ReturnsOriginalString() {
        assertEquals("Hello world", HTMLFilter.filter("Hello world"));
    }
    
    @Test
    public void testFilter_AmpersandCharacter_ReplacesWithAmpersandEntity() {
        assertEquals("This &amp; That", HTMLFilter.filter("This & That"));
    }
    
    @Test
    public void testFilter_LessThanCharacter_ReplacesWithLessThanEntity() {
        assertEquals("3 &lt; 5", HTMLFilter.filter("3 < 5"));
    }
    
    @Test
    public void testFilter_GreaterThanCharacter_ReplacesWithGreaterThanEntity() {
        assertEquals("5 &gt; 3", HTMLFilter.filter("5 > 3"));
    }
    
    @Test
    public void testFilter_QuoteCharacter_ReplacesWithQuoteEntity() {
        assertEquals("He said, &quot;Hello&quot;", HTMLFilter.filter("He said, \"Hello\""));
    }
    
    // Potential edge cases and exception handling scenarios:
    // 1. Message with special characters at the beginning or end
    // 2. Large input message with special characters
    
    @Test
    public void testFilter_SpecialCharactersAtBeginningAndEnd_ReturnsCorrectResult() {
        assertEquals("&lt;Hello world&gt;", HTMLFilter.filter("<Hello world>"));
    }
    
    @Test
    public void testFilter_LargeInputWithSpecialCharacters_ReturnsCorrectResult() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("This is a test & < > \" ");
        }
        String input = sb.toString();
        String expected = "This is a test &amp; &lt; &gt; &quot; ".repeat(10000);
        
        assertEquals(expected, HTMLFilter.filter(input));
    }
}