import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_nullInput_returnNull() {
        assertEquals(null, HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_emptyInput_returnEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_noSpecialCharacters_returnSameString() {
        String input = "This is a normal message without any special characters.";
        assertEquals(input, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_containsLessThanSymbol_replaceWithEscapedSymbol() {
        String input = "This string contains a < symbol.";
        String expected = "This string contains a &lt; symbol.";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_containsGreaterThanSymbol_replaceWithEscapedSymbol() {
        String input = "This string contains a > symbol.";
        String expected = "This string contains a &gt; symbol.";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_containsAmpersandSymbol_replaceWithEscapedSymbol() {
        String input = "This string contains an & symbol.";
        String expected = "This string contains an &amp; symbol.";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_containsDoubleQuoteSymbol_replaceWithEscapedSymbol() {
        String input = "This string contains a \" symbol.";
        String expected = "This string contains a &quot; symbol.";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_containsMultipleSpecialCharacters_replaceWithEscapedSymbols() {
        String input = "<>&\"This string contains multiple special characters.";
        String expected = "&lt;&gt;&amp;&quot;This string contains multiple special characters.";
        assertEquals(expected, HTMLFilter.filter(input));
    }
}