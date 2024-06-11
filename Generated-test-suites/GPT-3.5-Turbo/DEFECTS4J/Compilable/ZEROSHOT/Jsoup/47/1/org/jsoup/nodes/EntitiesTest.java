package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("test"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("test"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("test"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings()));
        assertEquals("&#x20;", Entities.escape(" ", new Document.OutputSettings()));
        assertEquals("&nbsp;", Entities.escape(" ", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&#x20;", Entities.unescape("&#x20;"));
    }
}