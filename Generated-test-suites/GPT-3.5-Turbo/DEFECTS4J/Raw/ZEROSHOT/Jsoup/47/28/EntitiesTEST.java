import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&#xA0;", Entities.escape("\u00A0", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }

    @Test
    public void testLoadedEntities() {
        Map<String, Character> fullEntities = Entities.loadEntities("entities-full.properties");
        assertNotNull(fullEntities);
        assertFalse(fullEntities.isEmpty());

        Map<String, Character> baseEntities = Entities.loadEntities("entities-base.properties");
        assertNotNull(baseEntities);
        assertFalse(baseEntities.isEmpty());
    }
}