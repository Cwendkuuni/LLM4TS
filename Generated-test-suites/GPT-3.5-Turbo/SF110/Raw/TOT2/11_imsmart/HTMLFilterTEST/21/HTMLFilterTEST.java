import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilterBasic() {
        String input = "<p>This is a test</p>";
        String expected = "&lt;p&gt;This is a test&lt;/p&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilterNullInput() {
        String input = null;
        assertNull(HTMLFilter.filter(input));
    }

    @Test
    public void testFilterEmptyString() {
        String input = "";
        String expected = "";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilterEdgeCases() {
        String input = "<html>This is an & edge case\"</html>";
        String expected = "&lt;html&gt;This is an &amp; edge case&quot;&lt;/html&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilterWithSpecialCharacters() {
        String input = "Special characters: <>&\"";
        String expected = "Special characters: &lt;&gt;&amp;&quot;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
}