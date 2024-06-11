import static org.junit.Assert.*;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnNull() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }

    @Test
    public void testFilter_EmptyMessage_ReturnEmptyString() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnSameMessage() {
        String message = "Hello World";
        String result = HTMLFilter.filter(message);
        assertEquals(message, result);
    }

    @Test
    public void testFilter_ContainsLessThanCharacter_ReplacesWithEscapedCode() {
        String message = "This is <test>";
        String expected = "This is &lt;test&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_ContainsGreaterThanCharacter_ReplacesWithEscapedCode() {
        String message = "This is >test<";
        String expected = "This is &gt;test&lt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_ContainsAmpersandCharacter_ReplacesWithEscapedCode() {
        String message = "This is &amp;test&";
        String expected = "This is &amp;amp;test&amp;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_ContainsDoubleQuoteCharacter_ReplacesWithEscapedCode() {
        String message = "This is \"test\"";
        String expected = "This is &quot;test&quot;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_ContainsAllSpecialCharacters_ReplacesWithEscapedCodes() {
        String message = "<>&\"\'";
        String expected = "&lt;&gt;&amp;&quot;&apos;";
        String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }
}