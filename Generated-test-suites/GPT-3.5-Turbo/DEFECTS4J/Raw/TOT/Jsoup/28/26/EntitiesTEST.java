
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    private Entities.EscapeMode escapeMode;

    @Before
    public void setUp() {
        escapeMode = Entities.EscapeMode.xhtml;
    }

    @After
    public void tearDown() {
        escapeMode = null;
    }

    @Test
    public void isNamedEntity_ValidEntity_ReturnsTrue() {
        assertTrue(Entities.isNamedEntity("quot"));
    }

    @Test
    public void isNamedEntity_InvalidEntity_ReturnsFalse() {
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void getCharacterByName_ValidName_ReturnsCharacter() {
        Character character = Entities.getCharacterByName("lt");
        assertNotNull(character);
        assertEquals('<', character.charValue());
    }

    @Test
    public void getCharacterByName_InvalidName_ReturnsNull() {
        Character character = Entities.getCharacterByName("invalid");
        assertNull(character);
    }

    @Test
    public void escape_StringWithSpecialCharacters_ReturnsEscapedString() {
        String input = "<div>\"Hello & World'</div>";
        String expected = "&lt;div&gt;&quot;Hello &amp; World&apos;&lt;/div&gt;";
        String result = Entities.escape(input, escapeMode);
        assertEquals(expected, result);
    }

    @Test
    public void escape_StringWithUnencodableCharacters_ReturnsEscapedString() {
        String input = "Hello ∞ World";
        String expected = "Hello &#x221E; World";
        String result = Entities.escape(input, escapeMode);
        assertEquals(expected, result);
    }

    @Test
    public void unescape_StringWithURL_ReturnsUnescapedString() {
        String input = "Visit &lt;a href=&quot;https://example.com&quot;&gt;example.com&lt;/a&gt;";
        String expected = "Visit <a href=\"https://example.com\">example.com</a>";
        String result = Entities.unescape(input);
        assertEquals(expected, result);
    }

    @Test
    public void unescape_StringWithInvalidEntity_ReturnsUnchangedString() {
        String input = "Hello &invalid;";
        String result = Entities.unescape(input);
        assertEquals(input, result);
    }
}
