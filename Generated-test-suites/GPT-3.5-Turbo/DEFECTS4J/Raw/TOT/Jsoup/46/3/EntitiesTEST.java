import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.assertEquals;

public class EntitiesTest {

    private Entities.EscapeMode escapeMode;

    @Before
    public void setup() {
        escapeMode = Entities.EscapeMode.xhtml;
    }

    @Test
    public void testIsNamedEntityWithKnownEntity() {
        boolean result = Entities.isNamedEntity("lt");
        assertEquals(true, result);
    }

    @Test
    public void testIsNamedEntityWithUnknownEntity() {
        boolean result = Entities.isNamedEntity("unknown");
        assertEquals(false, result);
    }

    @Test
    public void testIsBaseNamedEntityWithKnownEntity() {
        boolean result = Entities.isBaseNamedEntity("amp");
        assertEquals(true, result);
    }

    @Test
    public void testIsBaseNamedEntityWithUnknownEntity() {
        boolean result = Entities.isBaseNamedEntity("unknown");
        assertEquals(false, result);
    }

    @Test
    public void testGetCharacterByNameWithKnownEntity() {
        Character result = Entities.getCharacterByName("gt");
        assertEquals(Character.valueOf('>'), result);
    }

    @Test
    public void testGetCharacterByNameWithUnknownEntity() {
        Character result = Entities.getCharacterByName("unknown");
        assertEquals(null, result);
    }

    @Test
    public void testEscapeWithNormalStringInAttribute() {
        String result = Entities.escape("Hello, world!", escapeMode);
        assertEquals("Hello, world!", result);
    }

    @Test
    public void testEscapeWithCharacterToBeEncodedInAttribute() {
        String result = Entities.escape("\"", escapeMode);
        assertEquals("&quot;", result);
    }

    @Test
    public void testEscapeWithCharacterThatDoesNotRequireEncoding() {
        String result = Entities.escape("A", escapeMode);
        assertEquals("A", result);
    }

    @Test
    public void testEscapeWithWhitespaceStringAndNormalizeWhiteFalse() {
        String result = Entities.escape("     ", escapeMode);
        assertEquals("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", result);
    }

    @Test
    public void testEscapeWithWhitespaceStringAndNormalizeWhiteTrue() {
        String result = Entities.escape("     ", escapeMode);
        assertEquals(" ", result);
    }

    @Test
    public void testUnescapeWithEscapedString() {
        String result = Entities.unescape("&lt;Hello, world!&gt;");
        assertEquals("<Hello, world!>", result);
    }

    @Test
    public void testUnescapeWithUnescapedString() {
        String result = Entities.unescape("Hello, world!");
        assertEquals("Hello, world!", result);
    }

    @Test
    public void testUnescapeWithStrictEnabledAndUnescapedString() {
        String result = Entities.unescape("Hello, world!", true);
        assertEquals("Hello, world!", result);
    }

    @Test
    public void testUnescapeWithStrictEnabledAndEscapedString() {
        String result = Entities.unescape("&lt;Hello, world!&gt;", true);
        assertEquals("<Hello, world!>", result);
    }

}