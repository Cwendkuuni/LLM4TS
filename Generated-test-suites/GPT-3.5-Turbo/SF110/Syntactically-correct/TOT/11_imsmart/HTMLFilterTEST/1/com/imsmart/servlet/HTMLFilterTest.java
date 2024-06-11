import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HTMLFilterTest {

    private HTMLFilter htmlFilter;

    @Before
    public void setUp() {
        htmlFilter = new HTMLFilter();
    }

    @Test
    public void testFilter_NullMessage_ReturnNull() {
        assertNull(htmlFilter.filter(null));
    }

    @Test
    public void testFilter_EmptyMessage_ReturnEmpty() {
        assertEquals("", htmlFilter.filter(""));
    }

    @Test
    public void testFilter_NoSpecialCharacters_ReturnSameMessage() {
        String message = "This is a normal message";
        assertEquals(message, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsLessThanSymbol_EscapeLessThanSymbol() {
        String message = "This message contains a < symbol";
        String expected = "This message contains a &lt; symbol";
        assertEquals(expected, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsGreaterThanSymbol_EscapeGreaterThanSymbol() {
        String message = "This message contains a > symbol";
        String expected = "This message contains a &gt; symbol";
        assertEquals(expected, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsAmpersandSymbol_EscapeAmpersandSymbol() {
        String message = "This message contains an & symbol";
        String expected = "This message contains an &amp; symbol";
        assertEquals(expected, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsDoubleQuoteSymbol_EscapeDoubleQuoteSymbol() {
        String message = "This message contains a \" symbol";
        String expected = "This message contains a &quot; symbol";
        assertEquals(expected, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsMultipleSpecialCharacters_EscapeAllSpecialCharacters() {
        String message = "This message contains <, >, &, and \" symbols";
        String expected = "This message contains &lt;, &gt;, &amp;, and &quot; symbols";
        assertEquals(expected, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsInvalidUnicodeCharacter_ReturnSameMessage() {
        String message = "This message contains an invalid\uFFFFunicode character";
        assertEquals(message, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsControlCharacter_ReturnSameMessage() {
        String message = "This message contains a \n new line character";
        assertEquals(message, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsExtendedAsciiCharacters_ReturnSameMessage() {
        String message = "This message contains special characters: äüö";
        assertEquals(message, htmlFilter.filter(message));
    }

    @Test
    public void testFilter_ContainsMaxLengthMessage_ReturnFilteredMessage() {
        String message = "This message is exactly 65535 characters long";
        StringBuilder sb = new StringBuilder(65535);
        for (int i = 0; i < 65535; i++) {
            sb.append('a');
        }
        String expected = "This message is exactly 65535 characters long";
        assertEquals(expected, htmlFilter.filter(sb.toString()));
    }

    @Test
    public void testFilter_ContainsMaxLengthMessagePlusOneCharacter_ReturnFilteredMessageTruncated() {
        String message = "This message is exactly 65536 characters long";
        StringBuilder sb = new StringBuilder(65536);
        for (int i = 0; i < 65536; i++) {
            sb.append('a');
        }
        String expected = "This message is exactly 65536 characters long";
        assertEquals(expected, htmlFilter.filter(sb.toString()));
    }

}