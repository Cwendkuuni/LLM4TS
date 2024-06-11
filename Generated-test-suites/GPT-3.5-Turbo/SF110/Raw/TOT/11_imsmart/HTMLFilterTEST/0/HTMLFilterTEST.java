
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String filtered = HTMLFilter.filter(null);
        assertNull(filtered);
    }

    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        String filtered = HTMLFilter.filter("");
        assertEquals("", filtered);
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnsSameString() {
        String message = "This is a test message.";
        String filtered = HTMLFilter.filter(message);
        assertEquals(message, filtered);
    }

    @Test
    public void testFilter_ContainsLessThanSymbol_ReplacesWithEntities() {
        String message = "This message < contains a less than symbol.";
        String expected = "This message &lt; contains a less than symbol.";
        String filtered = HTMLFilter.filter(message);
        assertEquals(expected, filtered);
    }

    @Test
    public void testFilter_ContainsGreaterThanSymbol_ReplacesWithEntities() {
        String message = "This message > contains a greater than symbol.";
        String expected = "This message &gt; contains a greater than symbol.";
        String filtered = HTMLFilter.filter(message);
        assertEquals(expected, filtered);
    }

    @Test
    public void testFilter_ContainsAmpersandSymbol_ReplacesWithEntities() {
        String message = "This message & contains an ampersand symbol.";
        String expected = "This message &amp; contains an ampersand symbol.";
        String filtered = HTMLFilter.filter(message);
        assertEquals(expected, filtered);
    }

    @Test
    public void testFilter_ContainsDoubleQuoteSymbol_ReplacesWithEntities() {
        String message = "This message \" contains a double quote symbol.";
        String expected = "This message &quot; contains a double quote symbol.";
        String filtered = HTMLFilter.filter(message);
        assertEquals(expected, filtered);
    }

    @Test
    public void testFilter_ContainsMultipleSpecialCharacters_ReplacesWithEntities() {
        String message = "This message <>&\" contains multiple special characters.";
        String expected = "This message &lt;&gt;&amp;&quot; contains multiple special characters.";
        String filtered = HTMLFilter.filter(message);
        assertEquals(expected, filtered);
    }
}

