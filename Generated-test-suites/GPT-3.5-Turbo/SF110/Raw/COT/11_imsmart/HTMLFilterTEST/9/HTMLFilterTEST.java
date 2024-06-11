
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullString_ReturnsNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyString_ReturnsEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnsSameString() {
        assertEquals("Hello World", HTMLFilter.filter("Hello World"));
    }

    @Test
    public void testFilter_ContainsLessThanSymbol_ReplacesWithEscapedSequence() {
        assertEquals("This is &lt; a test", HTMLFilter.filter("This is < a test"));
    }

    @Test
    public void testFilter_ContainsGreaterThanSymbol_ReplacesWithEscapedSequence() {
        assertEquals("This is &gt; a test", HTMLFilter.filter("This is > a test"));
    }

    @Test
    public void testFilter_ContainsAmpersandSymbol_ReplacesWithEscapedSequence() {
        assertEquals("This is &amp; a test", HTMLFilter.filter("This is & a test"));
    }

    @Test
    public void testFilter_ContainsDoubleQuoteSymbol_ReplacesWithEscapedSequence() {
        assertEquals("This is &quot; a test", HTMLFilter.filter("This is \" a test"));
    }
}
