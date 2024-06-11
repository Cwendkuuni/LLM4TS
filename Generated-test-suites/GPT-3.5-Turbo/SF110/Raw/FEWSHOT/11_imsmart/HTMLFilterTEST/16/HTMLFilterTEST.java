import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    
    @Test
    public void testFilter() {
        // Test case 1: null input
        assertNull(HTMLFilter.filter(null));
        
        // Test case 2: empty input
        assertEquals("", HTMLFilter.filter(""));
        
        // Test case 3: input without any special characters
        assertEquals("hello world", HTMLFilter.filter("hello world"));
        
        // Test case 4: input with '<' character
        assertEquals("&lt;hello world", HTMLFilter.filter("<hello world"));
        
        // Test case 5: input with '>' character
        assertEquals("hello world&gt;", HTMLFilter.filter("hello world>"));
        
        // Test case 6: input with '&' character
        assertEquals("hello &amp; world", HTMLFilter.filter("hello & world"));
        
        // Test case 7: input with '\"' character
        assertEquals("hello &quot;world", HTMLFilter.filter("hello \"world"));
        
        // Test case 8: input with multiple special characters
        assertEquals("&lt;hello &amp; world&quot;", HTMLFilter.filter("<hello & world\""));
    }
}