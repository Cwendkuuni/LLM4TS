import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_withNullMessage_shouldReturnNull() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilter_withEmptyMessage_shouldReturnEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilter_withRegularMessage_shouldFilterHTMLCharacters() {
        assertEquals("This is an example &gt; of filtering &lt; HTML tags", HTMLFilter.filter("This is an example > of filtering < HTML tags"));
    }
    
    @Test
    public void testFilter_withMessageContainingAllSpecialCharacters_shouldFilterCorrectly() {
        assertEquals("&lt;&gt;&amp;&quot;", HTMLFilter.filter("<>&\""));
    }
    
    @Test
    public void testFilter_withMessageContainingOtherSpecialCharacters_shouldNotBeFiltered() {
        assertEquals("Hello World!123", HTMLFilter.filter("Hello World!123"));
    }

}