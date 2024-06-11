import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HTMLFilterTest {

    @Test
    public void testFilter_NormalString() {
        String input = "<p>Test</p>";
        String expected = "&lt;p&gt;Test&lt;/p&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_NullInput() {
        String input = null;
        assertNull(HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_EmptyString() {
        String input = "";
        String expected = "";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_EdgeCases() {
        String input = "<>";
        String expected = "&lt;&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
        
        input = "&";
        expected = "&amp;";
        result = HTMLFilter.filter(input);
        assertEquals(expected, result);
        
        input = "\"";
        expected = "&quot;";
        result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
}