import org.junit.*;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }

    @Test
    public void testFilter_EmptyString_ReturnsEmptyString() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnsInputString() {
        String input = "This is a test";
        String result = HTMLFilter.filter(input);
        assertEquals(input, result);
    }

    @Test
    public void testFilter_StringWithSpecialCharacters_ReturnsFilteredString() {
        String input = "Hello <world> &amp; 'universe'";
        String expected = "Hello &lt;world&gt; &amp;amp; &apos;universe&apos;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_StringWithDoubleQuotes_ReturnsFilteredString() {
        String input = "This is a \"test\"";
        String expected = "This is a &quot;test&quot;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    // Edge Case: Maximum allowed length input
    @Test
    public void testFilter_MaxLengthInput_ReturnsFilteredString() {
        StringBuilder sb = new StringBuilder();
        // Create a string with maximum allowed length
        for (int i = 0; i < 65535; i++) {
            sb.append("a");
        }
        String input = sb.toString();
        String result = HTMLFilter.filter(input);
        assertEquals(input, result);
    }

    // Error Scenario: Message exceeding maximum allowed length
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFilter_MessageExceedsMaxLength_ThrowsException() {
        StringBuilder sb = new StringBuilder();
        // Create a string with length exceeding maximum allowed length
        for (int i = 0; i < 65536; i++) {
            sb.append("a");
        }
        String input = sb.toString();
        HTMLFilter.filter(input);
    }
}