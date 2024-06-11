import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    // Test case for null input
    @Test
    public void testFilter_NullInput() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }

    // Test case for empty string input
    @Test
    public void testFilter_EmptyString() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }

    // Test case for input with no special characters
    @Test
    public void testFilter_NoSpecialCharacters() {
        String result = HTMLFilter.filter("Hello World");
        assertEquals("Hello World", result);
    }

    // Test case for input with special characters
    @Test
    public void testFilter_WithSpecialCharacters() {
        String input = "<html> & \"Welcome\"";
        String expected = "&lt;html&gt; &amp; &quot;Welcome&quot;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    // Test case for input with only special characters
    @Test
    public void testFilter_OnlySpecialCharacters() {
        String input = "<>&\"";
        String expected = "&lt;&gt;&amp;&quot;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    // Test case for large input
    @Test
    public void testFilter_LargeInput() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            builder.append("<>");
        }
        String input = builder.toString();
        String expected = "";
        for (int i = 0; i < 100000; i++) {
            expected += "&lt;&gt;";
        }
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
}