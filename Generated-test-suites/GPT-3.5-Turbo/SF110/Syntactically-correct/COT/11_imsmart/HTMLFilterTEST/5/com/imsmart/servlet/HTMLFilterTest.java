
import org.junit.Test;

import static org.junit.Assert.*;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_NullInput_ReturnNull() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }
    
    @Test
    public void testFilter_NoSpecialCharacters_ReturnOriginalString() {
        String input = "This is a test message.";
        String result = HTMLFilter.filter(input);
        assertEquals(input, result);
    }
    
    @Test
    public void testFilter_ContainsLessThanSymbol_ReturnFilteredString() {
        String input = "This message contains < symbol.";
        String expected = "This message contains &lt; symbol.";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsGreaterThanSymbol_ReturnFilteredString() {
        String input = "This message contains > symbol.";
        String expected = "This message contains &gt; symbol.";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsAmpersandSymbol_ReturnFilteredString() {
        String input = "This message contains & symbol.";
        String expected = "This message contains &amp; symbol.";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsDoubleQuoteSymbol_ReturnFilteredString() {
        String input = "This message contains \" symbol.";
        String expected = "This message contains &quot; symbol.";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
}
