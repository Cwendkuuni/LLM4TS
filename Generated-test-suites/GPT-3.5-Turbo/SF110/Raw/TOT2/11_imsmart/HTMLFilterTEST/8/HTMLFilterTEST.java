import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EscapeCharacters() {
        String input = "<p>Hello, <a href=\"https://www.example.com\">click here</a>!</p>";
        String expectedOutput = "&lt;p&gt;Hello, &lt;a href=&quot;https://www.example.com&quot;&gt;click here&lt;/a&gt;!&lt;/p&gt;";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_EmptyMessage() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters() {
        String input = "This is a plain text message.";
        assertEquals(input, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_OnlySpecialCharacters() {
        String input = "<>&\"'";
        String expectedOutput = "&lt;&gt;&amp;&quot;&apos;";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
}