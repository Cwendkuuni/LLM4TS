import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyMessage_ReturnEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialChar_ReturnOriginalString() {
        assertEquals("abcdefg", HTMLFilter.filter("abcdefg"));
    }

    @Test
    public void testFilter_ContainsLTChar_ReturnFilteredString() {
        assertEquals("Hello &lt; World", HTMLFilter.filter("Hello < World"));
    }

    @Test
    public void testFilter_ContainsGTChar_ReturnFilteredString() {
        assertEquals("Hello &gt; World", HTMLFilter.filter("Hello > World"));
    }

    @Test
    public void testFilter_ContainsAmpersandChar_ReturnFilteredString() {
        assertEquals("Hello &amp; World", HTMLFilter.filter("Hello & World"));
    }

    @Test
    public void testFilter_ContainsDoubleQuoteChar_ReturnFilteredString() {
        assertEquals("Hello &quot; World", HTMLFilter.filter("Hello \" World"));
    }

}