import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_NullInput_ReturnsNull() {
        String result = HTMLFilter.filter(null);
        assertEquals(null, result);
    }
    
    @Test
    public void testFilter_NoSpecialCharacters_ReturnsSameString() {
        String input = "This is a test message";
        String result = HTMLFilter.filter(input);
        assertEquals(input, result);
    }
    
    @Test
    public void testFilter_ContainsLessThanSymbol_ReplacesWithHTMLCode() {
        String input = "This is a < test";
        String expected = "This is a &lt; test";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsGreaterThanSymbol_ReplacesWithHTMLCode() {
        String input = "This is a > test";
        String expected = "This is a &gt; test";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsAmpersandSymbol_ReplacesWithHTMLCode() {
        String input = "This is a & test";
        String expected = "This is a &amp; test";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_ContainsDoubleQuoteSymbol_ReplacesWithHTMLCode() {
        String input = "This is a \" test";
        String expected = "This is a &quot; test";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
}