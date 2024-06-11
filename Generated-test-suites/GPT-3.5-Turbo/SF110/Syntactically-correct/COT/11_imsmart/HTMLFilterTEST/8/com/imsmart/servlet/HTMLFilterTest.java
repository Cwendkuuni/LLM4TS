
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_MessageWithTags_ReturnsFilteredString() {
        String message = "<div>Hello, <b>World!</b></div>";
        String expected = "&lt;div&gt;Hello, &lt;b&gt;World!&lt;/b&gt;&lt;/div&gt;";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithAmpersand_ReturnsFilteredString() {
        String message = "Hello &amp; World";
        String expected = "Hello &amp;amp; World";
        assertEquals(expected, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MessageWithQuotes_ReturnsFilteredString() {
        String message = "She said, \"Hello\"";
        String expected = "She said, &quot;Hello&quot;";
        assertEquals(expected, HTMLFilter.filter(message));
    }
}
