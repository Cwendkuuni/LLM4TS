
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnsNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyString_ReturnsEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoHTMLCharacters_ReturnsSameString() {
        String message = "This is a test!";
        assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsLessThanCharacter_ReturnsFilteredString() {
        String message = "This is < a test!";
        assertEquals("This is &lt; a test!", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsGreaterThanCharacter_ReturnsFilteredString() {
        String message = "This is > another test!";
        assertEquals("This is &gt; another test!", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsAmpersandCharacter_ReturnsFilteredString() {
        String message = "This & that";
        assertEquals("This &amp; that", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsDoubleQuotes_ReturnsFilteredString() {
        String message = "This \"quote\" is filtered";
        assertEquals("This &quot;quote&quot; is filtered", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsMultipleHTMLCharacters_ReturnsFilteredString() {
        String message = "This < is & a test \"quote\"";
        assertEquals("This &lt; is &amp; a test &quot;quote&quot;", HTMLFilter.filter(message));
    }

}
