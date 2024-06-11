import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    
    @Test
    public void testFilterWithNullMessage() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilterWithEmptyMessage() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilterWithSpecialCharacters() {
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
    }
    
    @Test
    public void testFilterWithMixedCharacters() {
        assertEquals("A&lt;B&gt;C&amp;D&quot;E", HTMLFilter.filter("A<B>C&D\"E"));
    }
    
    // Add more test cases for additional scenarios
    
}