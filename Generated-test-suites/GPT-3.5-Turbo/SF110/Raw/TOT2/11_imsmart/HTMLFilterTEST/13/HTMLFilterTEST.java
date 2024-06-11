import static org.junit.Assert.*;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_SimpleString() {
        assertEquals("Hello World", HTMLFilter.filter("Hello World"));
    }

    @Test
    public void testFilter_HTMLCharacters() {
        assertEquals("&lt;script&gt;", HTMLFilter.filter("<script>"));
        assertEquals("&gt;iframe&lt;", HTMLFilter.filter(">iframe<"));
        assertEquals("&amp;span&amp;", HTMLFilter.filter("&span&"));
        assertEquals("&quot;div&quot;", HTMLFilter.filter("\"div\""));
    }

    @Test
    public void testFilter_EdgeCases() {
        // Test for input with max integer number characters
        String maxInput = new String(new char[Integer.MAX_VALUE]).replace("\0", "X");
        assertNotNull(HTMLFilter.filter(maxInput));
        
        // Test for special characters
        assertEquals("&nbsp;&amp;&lt;&gt;&amp;quote;&semi;", HTMLFilter.filter(" &<<>&quote;;"));
        
        // Test for long input
        String longInput = new String(new char[10000]).replace("\0", "A");
        assertNotNull(HTMLFilter.filter(longInput));
    }

    @Test(expected = NullPointerException.class)
    public void testFilter_ExceptionHandling() {
        HTMLFilter.filter(null);
    }
}