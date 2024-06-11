import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_WithNullMessage_ReturnsNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_WithEmptyString_ReturnsEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_WithNoSpecialCharacters_ReturnsSameString() {
        String input = "This is a test";
        assertEquals(input, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_WithSpecialCharacterLessThan_ReturnsFilteredString() {
        String input = "10 < 20";
        String expected = "10 &lt; 20";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_WithSpecialCharacterGreaterThan_ReturnsFilteredString() {
        String input = "30 > 40";
        String expected = "30 &gt; 40";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_WithSpecialCharacterAmpersand_ReturnsFilteredString() {
        String input = "Apples & Bananas";
        String expected = "Apples &amp; Bananas";
        assertEquals(expected, HTMLFilter.filter(input));
    }

    @Test
    public void testFilter_WithSpecialCharacterDoubleQuote_ReturnsFilteredString() {
        String input = "She said, \"Hello\"";
        String expected = "She said, &quot;Hello&quot;";
        assertEquals(expected, HTMLFilter.filter(input));
    }
}