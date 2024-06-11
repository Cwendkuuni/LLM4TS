import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String input = "& < > \"";
        String expectedOutput = "&amp; &lt; &gt; &quot;";
        String result = Entities.escape(input, null);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testUnescape() {
        String input = "&amp; &lt; &gt; &quot;";
        String expectedOutput = "& < > \"";
        String result = Entities.unescape(input);
        assertEquals(expectedOutput, result);
    }
    
    // Edge cases and exception handling scenarios
    @Test
    public void testEscapeWithNullInput() {
        String result = Entities.escape(null, null);
        assertNull(result);
    }

    @Test
    public void testEscapeWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String result = Entities.escape(input, null);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testUnescapeWithNullInput() {
        String result = Entities.unescape(null);
        assertNull(result);
    }

    @Test
    public void testUnescapeWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String result = Entities.unescape(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testUnescapeWithStrictFlag() {
        String input = "&amp;";
        String expectedOutput = "&amp;";
        String result = Entities.unescape(input, true);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testEscapeWithInvalidCharacterEncoding() {
        String input = "€";
        String expectedOutput = "&euro;";
        String result = Entities.escape(input, null);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testUnescapeWithInvalidNamedEntity() {
        String input = "&xyz;";
        String expectedOutput = "&xyz;";
        String result = Entities.unescape(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testEscapeWithSpecialCharacterInAttribute() {
        String input = "<";
        String expectedOutput = "<";
        String result = Entities.escape(input, new org.jsoup.nodes.Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testEscapeWithSpecialCharacterInAttributeQuoted() {
        String input = "\"";
        String expectedOutput = "&quot;";
        String result = Entities.escape(input, new org.jsoup.nodes.Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testEscapeWithNonAsciiCharacterInAttributeInNonXhtmlMode() {
        String input = "€";
        String expectedOutput = "&euro;";
        String result = Entities.escape(input, new org.jsoup.nodes.Document.OutputSettings().escapeMode(Entities.EscapeMode.base));
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testEscapeWithNonAsciiCharacterInAttributeInXhtmlMode() {
        String input = "€";
        String expectedOutput = "&#x20AC;";
        String result = Entities.escape(input, new org.jsoup.nodes.Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals(expectedOutput, result);
    }
}