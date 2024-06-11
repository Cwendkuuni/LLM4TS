import org.jsoup.nodes.Entities.EscapeMode;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        assertEquals(Character.valueOf('<'), character);
    }

    @Test
    public void testEscape() {
        Entities.EscapeMode escapeMode = EscapeMode.xhtml;
        Document.OutputSettings out = new Document.OutputSettings(escapeMode);

        String escapedString = Entities.escape("Hello &amp; World", out);
        assertEquals("Hello &amp;amp; World", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("Hello &amp;amp; World");
        assertEquals("Hello &amp; World", unescapedString);
    }
}