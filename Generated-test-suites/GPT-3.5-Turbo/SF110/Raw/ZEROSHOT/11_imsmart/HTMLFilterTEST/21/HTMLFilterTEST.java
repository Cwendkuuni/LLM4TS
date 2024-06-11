import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    
    @Test
    public void testFilterNullMessage() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilterEmptyMessage() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilterNoSpecialCharacters() {
        String message = "This is a test";
        assertEquals(message, HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilterWithLessThanSymbol() {
        String message = "This is < a test";
        assertEquals("This is &lt; a test", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilterWithGreaterThanSymbol() {
        String message = "This is > a test";
        assertEquals("This is &gt; a test", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilterWithAmpersandSymbol() {
        String message = "This is & a test";
        assertEquals("This is &amp; a test", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilterWithQuotes() {
        String message = "This is \"a\" test";
        assertEquals("This is &quot;a&quot; test", HTMLFilter.filter(message));
    }
    
    @Test
    public void testFilterWithAllSpecialCharacters() {
        String message = "<This is \"&>\" a test";
        assertEquals("&lt;This is &quot;&amp;&gt;&quot; a test", HTMLFilter.filter(message));
    }
}