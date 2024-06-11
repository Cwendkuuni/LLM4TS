
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
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String string = "<div>";
        String escapedString = Entities.escape(string, Entities.EscapeMode.base);
        assertEquals("&lt;div&gt;", escapedString);
    }

    @Test
    public void testEscapeWithUnsupportedCharacters() {
        String string = "©";
        String escapedString = Entities.escape(string, Entities.EscapeMode.xhtml);
        assertEquals("&copy;", escapedString);
    }

    @Test
    public void testUnescape() {
        String string = "&lt;div&gt;";
        String unescapedString = Entities.unescape(string);
        assertEquals("<div>", unescapedString);
    }

    @Test
    public void testUnescapeStrictMode() {
        String string = "&lt;div&gt;";
        String unescapedString = Entities.unescape(string, true);
        assertEquals("<div>", unescapedString);
    }
}
